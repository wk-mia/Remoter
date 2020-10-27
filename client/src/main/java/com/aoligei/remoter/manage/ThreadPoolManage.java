package com.aoligei.remoter.manage;

import com.aoligei.remoter.exception.SponsorException;

import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

/**
 * @author wk-mia
 * 2020-9-16
 * 线程池管理器
 */
public class ThreadPoolManage {

    /**
     * 线程池
     * 假设客户端每秒平均有50个请求（tasks）发生，每个任务平均耗时0.1s（taskCost），40%情况下，任务量略大于50。
     * corePoolSize：50/1*0.1 = 5
     * 使用默认的拒绝策略。
     */
    private static final ScheduledThreadPoolExecutor POOL = new ScheduledThreadPoolExecutor(
            5,
            new AbortPolicy()
    );

    /**
     * 关闭线程池
     */
    public static void shutdown(){
        POOL.shutdown();}

    /**
     * 向线程池中提交任务
     * @param task 不带返回值的任务
     */
    public static void submit(Runnable task){
        POOL.execute(task);
    }

    /**
     * 向线程池中提交延时任务
     * @param task 不带返回值的任务
     * @param delay 延时时间
     */
    public static void submit(Runnable task,long delay){
        POOL.schedule(task,delay,TimeUnit.MILLISECONDS);
    }

    /**
     * 向线程池中提交任务
     * @param task 带返回值的任务
     * @param <T> 返回类型
     * @return 返回值
     * @throws SponsorException 执行异常
     */
    public static <T> T submit(Callable task) throws SponsorException{
        try{
            Future<T> submit = POOL.submit(task);
            return submit.get();
        }catch (InterruptedException | ExecutionException e){
            throw new SponsorException(e.getMessage(),e);
        }
    }
}
