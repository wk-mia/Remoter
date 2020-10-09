package com.aoligei.remoter.manage;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.business.RequestProcessor;
import com.aoligei.remoter.util.AccessConfigUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author wk-mia
 * 2020-9-28
 * 周期性任务管理器：心跳任务、屏幕截图、声音等
 */
@Component
public class CycleTaskManage {

    /**通道上下文*/
    private ChannelHandlerContext context;


    /**客户端身份管理器*/
    private TerminalManage terminalManage;
    /**请求组装器*/
    private RequestProcessor processor;
    @Autowired
    private CycleTaskManage(TerminalManage terminalManage,RequestProcessor processor){
        this.terminalManage = terminalManage;
        this.processor = processor;
    }

    /**任务池*/
    private final Map<Runnable, ScheduledFuture> tasks = new ConcurrentHashMap<>();



    /**
     * 心跳任务
     */
    private Runnable HeartbeatTask = new Runnable() {
        @Override
        public void run() {
            BaseRequest request = processor.buildHeartbeatRequest();
            if(usable()){
                context.writeAndFlush(request);
            }
        }
    };

    /**
     * 屏幕截图任务
     */
    private Runnable ScreenShotTask = new Runnable() {
        @Override
        public void run() {

        }
    };



    /**
     * 任务初始化
     */
    public void offer(ChannelHandlerContext context){
        this.context = context;
        if(terminalManage.getRemotingFlag()){
            /**当前正在远程工作中*/
        }else {
            if(tasks.get(HeartbeatTask) == null || tasks.get(HeartbeatTask).isCancelled()){
                tasks.put(HeartbeatTask,startHeartbeat());
            }
        }
    }

    /**
     * 开始发送心跳
     * 初始化延时设置为0
     * @return 返回任务执行器
     */
    private ScheduledFuture<?> startHeartbeat(){
        int intervals = AccessConfigUtil.getNumber(AccessConfigUtil.Config.PARAM,"task.heartbeat.intervals");
        return context.executor().scheduleAtFixedRate(HeartbeatTask,0,intervals, TimeUnit.SECONDS);
    }

    /**
     * 通道是否可用
     * @return 可用：true
     */
    private boolean usable(){
        return this.context != null
                && this.context.channel() != null
                && this.context.channel().isActive()
                && this.context.channel().isOpen();
    }
}
