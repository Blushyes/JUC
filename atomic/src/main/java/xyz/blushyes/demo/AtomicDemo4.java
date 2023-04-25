package xyz.blushyes.demo;

import lombok.extern.slf4j.Slf4j;
import xyz.blushyes.util.ThreadUtil;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

/**
 * ABA问题
 */
@Slf4j
public class AtomicDemo4 {
    public static void main(String[] args) throws InterruptedException {
        AtomicReference<BigDecimal> val = new AtomicReference<>(new BigDecimal(0));

        new Thread(() -> {
            log.debug("first val: {}", val.get());
            ThreadUtil.stop(3);
            for (;;) {
                BigDecimal expectedValue = val.get();

                if (val.compareAndSet(expectedValue, new BigDecimal(2))) {
                    log.debug("succeed, and val: {}", val.get());
                    break;
                }
            }
        }).start();

        new Thread(() -> {
            ThreadUtil.stop(1);
            for (;;) {
                BigDecimal expectedValue = val.get();

                if (val.compareAndSet(expectedValue, new BigDecimal(1))) {
                    log.debug("succeed, and val: {}", val.get());
                    break;
                }
            }
        }).start();

        new Thread(() -> {
            ThreadUtil.stop(2);
            for (;;) {
                BigDecimal expectedValue = val.get();

                if (val.compareAndSet(expectedValue, new BigDecimal(0))) {
                    log.debug("succeed, and val: {}", val.get());
                    break;
                }
            }
        }).start();

        //  [Thread-0] - first val: 0
        //  [Thread-1] - succeed, and val: 1
        //  [Thread-2] - succeed, and val: 0
        //  [Thread-0] - succeed, and val: 2
        // 在Thread1执行的过程中，val被进行更改，只不过最后又改回去了
    }
}