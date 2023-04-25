package xyz.blushyes.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class CASOperate {
    private final AtomicInteger value = new AtomicInteger(0);

    public int getValue() {
        return value.get();
    }

    public void increase(){
        for (;;) {
            // 首先通过get方法获取值
            int expectedValue = value.get();

            // cas函数会重新获取value的值并expectValue作对比，若相同，则替换成newValue，否则返回false
            if (value.compareAndSet(expectedValue, expectedValue + 1)) {
                break;
            }
        }
    }

    public void decrease() {
        for (;;) {
            int expectedValue = value.get();
            if (value.compareAndSet(expectedValue, expectedValue - 1)) {
                break;
            }
        }
    }

    public void otherOps() {
        value.updateAndGet(val -> val * 2);
    }
}
