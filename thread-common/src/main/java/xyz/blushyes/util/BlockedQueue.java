package xyz.blushyes.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class BlockedQueue<T> {
    private final Deque<T> storage = new ArrayDeque<>();

    private final int capacity;

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition publisherBlocked = lock.newCondition();

    private final Condition consumerBlocked = lock.newCondition();

    public BlockedQueue(int capacity) {
        this.capacity = capacity;
    }

    // 存入
    public void offer(T e){
        lock.lock();

        while (storage.size() == capacity) {   // 如果队列已满，那么进入阻塞
            try {
                log.debug("队列满了存不进，阻塞中......");
                publisherBlocked.await();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

        storage.offer(e);
        log.debug("存入：{}，队列：{}", e, storage);

        // 通知消费者队列中有元素了，可以起来干活了
        consumerBlocked.signal();

        lock.unlock();
    }

    // 带超时存入
    public void offer(T e, long timeout, TimeUnit timeUnit){
        lock.lock();

        long nanos = timeUnit.toNanos(timeout);

        while (storage.size() == capacity) {   // 如果队列已满，那么进入阻塞
            try {
                log.debug("队列满了存不进，阻塞中......");
                nanos = publisherBlocked.awaitNanos(nanos);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

        storage.offer(e);
        log.debug("存入：{}，队列：{}", e, storage);

        // 通知消费者队列中有元素了，可以起来干活了
        consumerBlocked.signal();

        lock.unlock();
    }


    // 取出
    public T poll(){
        lock.lock();

        while (storage.isEmpty()) {    // 如果队列为空，则进入阻塞
            try {
                log.debug("队列为空取不到，阻塞中......");
                consumerBlocked.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        T res = storage.poll();
        log.debug("获取到：{}，队列：{}", res, storage);

        // 通知生产者可以继续生产了，队列中有空闲了
        publisherBlocked.signal();

        lock.unlock();

        return res;
    }

    // 有超时时间取出
    public T poll(long timeout, TimeUnit timeUnit){
        lock.lock();

        long nanos = timeUnit.toNanos(timeout);

        while (storage.isEmpty()) {    // 如果队列为空，则进入阻塞
            try {
                if (nanos <= 0) {
                    log.debug("超时，退出");
                    break;
                }

                log.debug("队列为空取不到，阻塞中......");

                // 注意consumerBlocked.awaitNanos()返回的是剩余需要等待的时间
                // 如果没有等够时间，就被唤醒了，那么就会返回剩余的时间
                nanos = consumerBlocked.awaitNanos(nanos);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        T res = storage.poll();
        log.debug("获取到：{}，队列：{}", res, storage);

        // 通知生产者可以继续生产了，队列中有空闲了
        publisherBlocked.signal();

        lock.unlock();
        return res;
    }

    // 获取元素个数
    public int size(){
        return storage.size();
    }
}
