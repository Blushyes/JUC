package xyz.blushyes;

import xyz.blushyes.util.ThreadUtil;

public class NormalDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = ThreadUtil.getThread();

        thread.start();

        System.out.println("主线程运行中......");

        System.out.println("主线程结束");
    }
}
