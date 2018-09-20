package com.hstypay.sandbox.support;

import java.util.concurrent.*;

/**
 * @author Vincent
 * @version 1.0 2017-07-01 18:45
 */
public class GloabalCacheThreadPool {

    //默认线程池的基本大小为20
    private static final int DEFAULT_POOL_SIZE = 20;
    private static final int DEFAULT_POOL_MAX_SIZE = 100;
    private static final int DEFAULT_QUEUE_MAX_SIZE = 10000;

    private static ExecutorService executor;
    private static BlockingQueue<Runnable> workQueue;

    private static int corePoolSize = DEFAULT_POOL_SIZE;
    private static int maxPoolSize = DEFAULT_POOL_MAX_SIZE;
    private static int queueMaxSize = DEFAULT_QUEUE_MAX_SIZE;

    private GloabalCacheThreadPool() {

    }

    public static void start() {
        init();
    }

    public static void start(int corePoolSize, int maxPoolSize, int queueMaxSize) {
        if (executor != null) throw new IllegalArgumentException("GloabalCacheThreadPool rlready started!");
        GloabalCacheThreadPool.corePoolSize = corePoolSize <= 0 ? DEFAULT_POOL_SIZE : corePoolSize;
        GloabalCacheThreadPool.maxPoolSize = maxPoolSize <= 0 ? DEFAULT_POOL_SIZE : maxPoolSize;
        GloabalCacheThreadPool.queueMaxSize = queueMaxSize <= 0 ? DEFAULT_POOL_SIZE : queueMaxSize;
        init();
    }

    private static void init() {
        if (executor != null) throw new IllegalArgumentException("GloabalCacheThreadPool rlready started!");
        synchronized (GloabalCacheThreadPool.class) {
            workQueue = new LinkedBlockingDeque<Runnable>(queueMaxSize);
            executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 60L, TimeUnit.SECONDS, workQueue);
        }
    }

    /**
     * 使用newCachedThreadPool创建线程池，修改线程池基本大小为默认数量
     *
     * @return ExecutorService对象
     */
    public static ExecutorService getInstance() {
        return executor;
    }

    public static BlockingQueue<Runnable> getWorkQueue() {
        return workQueue;
    }

    public static void setWorkQueue(BlockingQueue<Runnable> workQueue) {
        GloabalCacheThreadPool.workQueue = workQueue;
    }

    public static void shutdown() {
        if (executor != null) executor.shutdown();
    }

    public static int getCorePoolSize() {
        return corePoolSize;
    }

    public static int getMaxPoolSize() {
        return maxPoolSize;
    }

    public static int getQueueMaxSize() {
        return queueMaxSize;
    }
}
