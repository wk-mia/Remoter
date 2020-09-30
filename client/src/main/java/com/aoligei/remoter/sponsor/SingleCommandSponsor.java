package com.aoligei.remoter.sponsor;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.constant.SponsorConstants;
import com.aoligei.remoter.exception.SponsorException;
import com.aoligei.remoter.manage.ThreadPoolManage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author wk-mia
 * 2020-9-16
 * 点对点模式客户端-单次命令发起处理器
 * 负责客户端向服务器发起命令，包括MASTER以及SLAVE的各种单次业务命令。
 */
@Component(value = "SingleCommandSponsor")
public class SingleCommandSponsor extends AbstractCommandSponsor {


    /**
     * 请求队列
     * 使用同步阻塞队列，既保证线程安全，又不用处理唤醒线程的逻辑
     */
    private ArrayBlockingQueue<BaseRequest> queue = new ArrayBlockingQueue<>(20);

    /**
     * 请求任务的处理
     */
    private Runnable SponsorTask = new Runnable() {
        @Override
        public void run() {
            /**
             * 不停的监听队列中是否有请求待处理
             */
            while (true){
                BaseRequest request = null;
                try {
                    while ((request = queue.take()) != null){
                        try{
                            sendRequest(request);
                        }catch (SponsorException e){
                            logError(request,e.getMessage());
                        }
                    }
                }catch (InterruptedException e){
                    logError(this,e.getMessage());
                }
            }
        }
    };

    /**
     * 构造器
     * 提交任务
     */
    public SingleCommandSponsor(){
        ThreadPoolManage.submit(SponsorTask);
    }

    /**
     * 向服务器发起命令
     * @param request 请求体
     * @throws SponsorException 发起命令异常
     */
    @Override
    protected void particularSponsor(BaseRequest request) throws SponsorException {
        queue.offer(request);
    }

}
