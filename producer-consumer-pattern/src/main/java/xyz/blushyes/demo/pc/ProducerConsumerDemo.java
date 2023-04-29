package xyz.blushyes.demo.pc;

import lombok.extern.slf4j.Slf4j;
import xyz.blushyes.util.BlockedQueue;
import xyz.blushyes.util.ThreadUtil;

@Slf4j
public class ProducerConsumerDemo {
    public static void main(String[] args) {
        BlockedQueue<Integer> blockedQueue = new BlockedQueue<>(5);
        
        for (int i = 0; i < 2; ++i){
            new Thread(()->{
                for (int j = 0; j < 100; ++j) {
                    ThreadUtil.stop(3);
                    blockedQueue.offer(j);
                }
            }, "生产者" + i).start();
        }

        for (int i = 0; i < 3; ++i){
            new Thread(()->{
                for (;;) {
                    ThreadUtil.stop(1);
                    log.debug("获取到：{}", blockedQueue.poll());
                }
            }, "消费者" + i).start();
        }
    }
}
