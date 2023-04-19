package xyz.blushyes.way2;

import java.util.concurrent.TimeUnit;

public class Way2 {
    public static void main(String[] args) {
        // Thread(Runnable target);
        // 接收一个Runnable对象
        // 也就是接收一个实现了Runnable接口的类的对象

        Thread thread1 = new Thread(() -> {
            for (;;){
                System.out.println("Thread1 is running...");

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread thread2 = new Thread(() -> {
           for (;;){
               System.out.println("Thread2 is running...");

               try {
                   TimeUnit.SECONDS.sleep(1);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        });

        thread1.start();
        thread2.start();
    }
}
