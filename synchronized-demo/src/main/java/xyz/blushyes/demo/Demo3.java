package xyz.blushyes.demo;

/**
 * 如果两个临界区采用的锁不同会怎样？
 */
public class Demo3 {
    private static int count = 0;

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100000; ++i) {
                synchronized (lock1) {
                    count++;
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100000; ++i) {
                synchronized (lock2) {
                    count--;
                }
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(count);  // random
    }
}
