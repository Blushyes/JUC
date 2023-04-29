package xyz.blushyes.pool;

import lombok.extern.slf4j.Slf4j;
import xyz.blushyes.util.BlockedQueue;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadPool {
    private final Set<Worker> workers = new HashSet<>();

    private final BlockedQueue<Runnable> blockedQueue;

    private final int coreQuantity;

    private final long timeout;

    private final TimeUnit timeUnit;

    public ThreadPool(int coreQuantity, long timeout, TimeUnit timeUnit, int blockedQueueCapacity) {
        this.coreQuantity = coreQuantity;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.blockedQueue = new BlockedQueue<>(blockedQueueCapacity);
        log.debug("创建一个新的线程池，核心线程数为{}", coreQuantity);
    }

    public ThreadPool(int coreQuantity, int blockedQueueCapacity) {
        this(coreQuantity, 3, TimeUnit.SECONDS, blockedQueueCapacity);
    }

    public void execute(Runnable task) {
        synchronized (workers) {
            if (workers.size() < coreQuantity) {    // 如果线程池中的线程数小于核心线程数，那么就新建一个线程
                Worker worker = new Worker(task);
                workers.add(worker);
                log.debug("新增一个worker，目前workers的大小为{}", workers.size());
                worker.start();
            } else {    // 否则新任务加入阻塞队列
                blockedQueue.offer(task);
            }
        }
    }

    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable target) {
            this.task = target;
        }

        @Override
        public void run() {
            // 这里可以加一个超时机制
            // 从阻塞队列中获取新任务
            while (task != null || (task = blockedQueue.poll(timeout, timeUnit)) != null) {
                task.run();
                this.task = null;
            }

            // 由于worker线程不安全，所以需要上锁
            synchronized (workers) {
                workers.remove(this);
                log.debug("移除一个worker，目前workers的大小为{}", workers.size());
            }
        }
    }
}