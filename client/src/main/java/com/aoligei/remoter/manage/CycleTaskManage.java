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

    /**前一个截屏*/
    private byte[] previousScreen;



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
    private ScreenShot ScreenShotTask = new ScreenShot();





    /**
     * 调度任务：停止旧任务，开始新任务
     */
    public void schedule(ChannelHandlerContext context,String connectionId){
        this.context = context;
        if(terminalManage.getRemotingFlag()){
            /**当前正在远程工作中，发送屏幕截图，并取消心跳任务*/
            if(tasks.get(ScreenShotTask) == null || tasks.get(ScreenShotTask).isCancelled()){
                /**屏幕截图任务需指定连接编码*/
                tasks.put(ScreenShotTask.setConnectionId(connectionId),starScreenshot());
            }
            if(tasks.get(HeartbeatTask) != null && ! tasks.get(HeartbeatTask).isCancelled()){
                tasks.get(HeartbeatTask).cancel(true);
            }
        }else {
            /**当前不在远程工作中，发送心跳，并取消屏幕截图任务*/
            if(tasks.get(HeartbeatTask) == null || tasks.get(HeartbeatTask).isCancelled()){
                tasks.put(HeartbeatTask,startHeartbeat());
            }
            if(tasks.get(ScreenShotTask) != null && ! tasks.get(ScreenShotTask).isCancelled()){
                tasks.get(ScreenShotTask).cancel(true);
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
        return context.executor().scheduleAtFixedRate(HeartbeatTask,0,intervals, TimeUnit.MILLISECONDS);
    }

    /**
     * 开始发送屏幕截图
     * 初始化延时设置为0
     * @return 返回任务执行器
     */
    private ScheduledFuture<?> starScreenshot(){
        int intervals = AccessConfigUtil.getNumber(AccessConfigUtil.Config.PARAM,"task.screenshots.intervals");
        return context.executor().scheduleAtFixedRate(ScreenShotTask,0,intervals, TimeUnit.MILLISECONDS);
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

    /**
     * 比较屏幕截图和上一个屏幕截图是否一样
     * @param previousScreen 上一个屏幕截图
     * @param currentScreen 当前屏幕截图
     * @return 是：true
     */
    private boolean isOldScreen(byte[] previousScreen,byte[] currentScreen){
        /**当前屏幕截图为空*/
        if(currentScreen == null){
            return true;
        }
        /**前一个屏幕截图为空：设置新的屏幕截图并返回*/
        if(previousScreen == null || previousScreen.length == 0){
            this.previousScreen = currentScreen;
            return false;
        }
        /**比较两个数组，发现有元素不一致时：设置新的屏幕截图并返回*/
        boolean changed = true;
        for(int i = 0;i < previousScreen.length;i++){
            if(previousScreen[i] != currentScreen[i] ){
                this.previousScreen = currentScreen;
                changed = false;
                break;
            }
        }
        return changed;
    }

    /**
     * 屏幕截图任务类
     */
    private class ScreenShot implements Runnable{
        private String connectionId;
        /**设置连接编码*/
        public ScreenShot setConnectionId(String connectionId){
            this.connectionId = connectionId;
            return this;
        }
        @Override
        public void run() {
            BaseRequest request = processor.buildScreenShotsRequest(connectionId);
            if(isOldScreen(previousScreen,(byte[])request.getData())){
                /**与上一个屏幕截图相同，不发送该屏幕截图，发送心跳*/
                HeartbeatTask.run();
            }else {
                /**发送屏幕截图*/
                if(usable()){
                    context.writeAndFlush(request);
                }
            }
        }
    }
}
