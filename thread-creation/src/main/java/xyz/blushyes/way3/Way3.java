package xyz.blushyes.way3;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class Way3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Thread(FutureTask target);
        // 接收一个FutureTask对象
        // 而FutureTask类需要由一个实现了Callable的类的对象来构造

        FutureTask<String> task1 = new FutureTask<>(() -> {
            System.out.println("Thread1 is running...");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return "this is thread1, and I have returned.";
        });

        FutureTask<String> task2 = new FutureTask<>(() -> {
            System.out.println("Thread2 is running...");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return "this is thread2, and I have returned.";
        });

        Thread thread1 = new Thread(task1, "thread1");
        Thread thread2 = new Thread(task2, "thread2");

        thread1.start();
        thread2.start();

        // 通过task的get方法获取线程的返回值
        System.out.println("thread1 return: " + task1.get());
        System.out.println("thread2 return: " + task2.get());
    }
}
