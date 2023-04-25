package xyz.blushyes.demo;

import lombok.extern.slf4j.Slf4j;
import xyz.blushyes.atomic.CASOperate;

/**
 * 针对原子变量使用的测试
 */
@Slf4j
public class AtomicDemo1 {
    public static void main(String[] args) throws InterruptedException {
        CASOperate casOperate = new CASOperate();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100000; ++i) {
                casOperate.increase();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100000; ++i) {
                casOperate.decrease();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(casOperate.getValue());  // 0
    }
}
