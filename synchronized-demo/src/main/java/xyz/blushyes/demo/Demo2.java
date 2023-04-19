package xyz.blushyes.demo;

/**
 * 使用synchronize解决
 */
public class Demo2 {
    private static int count = 0;

    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100000; ++i) {
                synchronized (lock) {
                    count++;
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100000; ++i) {
                synchronized (lock) {
                    count--;
                }
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(count);  // 0
    }
}
