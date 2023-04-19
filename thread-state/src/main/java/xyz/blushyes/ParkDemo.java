package xyz.blushyes;

import xyz.blushyes.util.ThreadUtil;

import java.util.concurrent.locks.LockSupport;

public class ParkDemo {
    public static void main(String[] args) {
        Thread thread = new Thread(()->{
            for (int i = 0; i < 3; ++i){
                System.out.println("thread线程运行中......");
                ThreadUtil.stop(1);
            }

            System.out.println("thread线程暂停");
            LockSupport.park();

            for (int i = 0; i < 3; ++i){
                System.out.println("thread线程运行中......");
                ThreadUtil.stop(1);
            }

            System.out.println("thread运行结束......");
        }, "thread");
        thread.start();

        for (int i = 0; i < 6; ++i){
            System.out.println("主线程运行中......");
            ThreadUtil.stop(1);
        }

        // 主线程运行一段时间后将thread解除暂停
        System.out.println("thread解除暂停");
        LockSupport.unpark(thread);
    }
}
