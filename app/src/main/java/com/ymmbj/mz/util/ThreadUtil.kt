package com.ymmbj.mz.util

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

object ThreadUtil {
    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
    private val CORE_POOL_SIZE = CPU_COUNT + 1
    private val MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1
    private const val KEEP_ALIVE = 1

    private val MyThreadFactory: ThreadFactory = object : ThreadFactory {
        private val mCount: AtomicInteger = AtomicInteger(1)
        override fun newThread(r: Runnable?): Thread {
            return Thread(r, "MyThreadFactory #" + mCount.getAndIncrement())
        }
    }

    private val wkPoolWorkQueue: BlockingQueue<Runnable> = LinkedBlockingQueue<Runnable>(128)

    /**
     * ThreadPoolExecutor默认有四个拒绝策略：
     *
     *
     * 1、ThreadPoolExecutor.AbortPolicy()   直接抛出异常RejectedExecutionException
     *
     *
     * 2、ThreadPoolExecutor.CallerRunsPolicy()    直接调用run方法并且阻塞执行
     *
     *
     * 3、ThreadPoolExecutor.DiscardPolicy()   直接丢弃后来的任务
     *
     *
     * 4、ThreadPoolExecutor.DiscardOldestPolicy()  丢弃在队列中队首的任务
     */
    val MyThreadPoolExecutor: Executor = ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE.toLong(), TimeUnit.SECONDS, wkPoolWorkQueue, MyThreadFactory, ThreadPoolExecutor.DiscardPolicy())

    val ControlImplPoolExecutor: Executor = ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE.toLong(), TimeUnit.SECONDS, wkPoolWorkQueue, object : ThreadFactory {
        private val mCount: AtomicInteger = AtomicInteger(1)
        override fun newThread(r: Runnable?): Thread {
            return Thread(r, "ControlImplThreadFactory #" + mCount.getAndIncrement())
        }
    }, ThreadPoolExecutor.DiscardPolicy())

}