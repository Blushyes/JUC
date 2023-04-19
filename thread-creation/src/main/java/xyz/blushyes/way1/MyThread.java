package xyz.blushyes.way1;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 创建一个类继承Thread类
 * 并重写run方法
 */
@Slf4j
class MyThread extends Thread{

    public MyThread() {
        super();
    }

    /**
     * 如果有命名需求，就重写父类的对应构造方法
     */
    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (;;){
            log.debug("running...");

            // 休眠
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}