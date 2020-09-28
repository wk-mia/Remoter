package com.aoligei.remoter.manage;

import com.aoligei.remoter.exception.SponsorException;

import java.util.concurrent.*;

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
     * maxPoolSize：5+5*0.4 = 7
     * 使用默认的拒绝策略。
     */
    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(
        5,
            7,
            1,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(4),
            new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     * 关闭线程池
     */
    public static void shutdown(){pool.shutdown();}

    /**
     * 向线程池中提交任务
     * @param task 不带返回值的任务
     */
    public static void submit(Runnable task){
        pool.execute(task);
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
            Future<T> submit = pool.submit(task);
            return submit.get();
        }catch (InterruptedException | ExecutionException e){
            throw new SponsorException(e.getMessage(),e);
        }
    }
}
