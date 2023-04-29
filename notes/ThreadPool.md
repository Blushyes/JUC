# 自制一个简单的线程池

## 问题

- 用什么存储线程？
- 需要暴露哪些方法？
- 怎么实现已有的线程运行阻塞队列中的任务？
- 如果任务队列为空怎么办？
- 如果任务队列满了怎么办？

## 用什么存储线程？

对于线程池中的线程，有两个操作：

1. 创建并存入
2. 获取并使用

很容易想到用一个Set存储最优。

~~~java
private final Set<Worker> workers = new HashSet<>();
~~~

## 需要暴露哪些方法？

~~~java
public void execute(Runnable task);
~~~

需要暴露一个执行任务的方法，内部是怎么运行的外部不需要知道。

等于是外部交给线程池一个任务来执行，不需要管线程池是怎么执行的。

## 怎么实现已有的线程运行阻塞队列中的任务？

首先来看一下每一个线程需要做的事情：

1. 执行一个任务
2. 如果自己手里的任务执行完毕，去阻塞队列获取新的任务

创建一个内部类Worker继承Thread类它具有一个Runnable的task属性，用来存储任务。

具体实现如下：

~~~java
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
~~~

## 代码

~~~java
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
~~~