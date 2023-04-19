package xyz.blushyes.util;

import java.util.concurrent.TimeUnit;

public class ThreadUtil {
    public static Thread getThread(){
        return getThread("thread");
    }

    /**
     * 获取一个线程
     * @return 一个循环五次的线程
     */
    public static Thread getThread(String name){
        return getThread(name, 5);
    }

    public static Thread getThread(String name, int times){
        return new Thread(() -> {
            for (int i = 0; i < times; ++i){
                System.out.println(name + "线程运行中......");
                stop(1);
            }

            System.out.println(name + "线程运行结束");
        }, name);
    }

    /**
     * 暂停
     */
    public static void stop(long time){
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
