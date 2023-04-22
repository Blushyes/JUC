package xyz.blushyes.demo.pc;

import xyz.blushyes.util.BlockedQueue;
import xyz.blushyes.util.ThreadUtil;

import java.util.concurrent.TimeUnit;

public class BlockedQueueDemo {
    public static void main(String[] args) {
        BlockedQueue<Integer> blockedQueue = new BlockedQueue<>(5);

        new Thread(()->{
            for (int i = 0; i < 100; ++i) {
                ThreadUtil.stop(3);
                blockedQueue.offer(i);
            }
        }).start();

        new Thread(()->{
            for (int i = 0; i < 100; ++i) {
                ThreadUtil.stop(1);
                blockedQueue.poll(1, TimeUnit.SECONDS);
            }
        }).start();
    }
}
