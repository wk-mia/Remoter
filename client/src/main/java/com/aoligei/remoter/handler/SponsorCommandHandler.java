package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.exception.SponsorException;
import com.aoligei.remoter.manage.TaskManage;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author wk-mia
 * 2020-9-16
 * 点对点模式客户端-命令发起处理器
 * 负责客户端向服务器发起命令，包括MASTER以及SLAVE的各种业务命令。
 */
@Component
public class SponsorCommandHandler extends AbstractSponsorCommandHandler {

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
    public SponsorCommandHandler(){
        TaskManage.submit(SponsorTask);
    }

    /**
     * 向服务器发起命令
     * @param baseRequest 请求体
     * @throws SponsorException 发起命令异常
     */
    @Override
    public void sponsor(BaseRequest baseRequest) throws SponsorException {
        queue.offer(baseRequest);
    }
}
