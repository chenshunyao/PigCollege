package com.xnf.henghenghui.io;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;

/**
 * create threadpool for fileLoader and ImageLoader cached image;<BR />
 * You can set the threadpool name and ProcessType (FIFO or LIFO)
 * @author laotian
 *
 */
public class DefaultThreadFactory implements ThreadFactory {

    private  final AtomicInteger poolNumber = new AtomicInteger(1);

    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private final int threadPriority;
    

    DefaultThreadFactory(String threadPoolPrefixName, int threadPriority) {
        this.threadPriority = threadPriority;
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = threadPoolPrefixName+"-pool-" + poolNumber.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()) t.setDaemon(false);
        t.setPriority(threadPriority);
        return t;
    }
    
    public static ThreadPoolExecutor createExecutor(int threadPoolSize, int threadPriority, QueueProcessingType tasksProcessingType, String threadPoolPrefixName)
    {
        boolean lifo = tasksProcessingType == QueueProcessingType.LIFO;
        BlockingQueue<Runnable> taskQueue = lifo ? new LIFOLinkedBlockingDeque<Runnable>() : new LinkedBlockingQueue<Runnable>();
        return new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0L, TimeUnit.MILLISECONDS, taskQueue, new DefaultThreadFactory(threadPoolPrefixName, threadPriority));
    }

}
