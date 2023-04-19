package xyz.blushyes;

import xyz.blushyes.util.ThreadUtil;

import java.util.concurrent.TimeUnit;

public class InterruptDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(()->{
            for (;;){
                System.out.println("thread运行中......");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("thread的sleep被打断");
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        System.out.println(thread.isInterrupted());
        ThreadUtil.stop(3);
        thread.interrupt();
        System.out.println(thread.isInterrupted());

        for (;;){
            System.out.println(thread.isInterrupted());
            ThreadUtil.stop(1);
        }
    }
}
