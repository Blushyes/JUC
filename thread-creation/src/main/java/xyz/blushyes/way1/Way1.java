package xyz.blushyes.way1;

public class Way1 {
    public static void main(String[] args) {
        Thread thread1 = new MyThread("thread1");
        Thread thread2 = new MyThread("thread2");

        thread1.start();
        thread2.start();

        // 注意，main线程已经结束，但是thread1和thread2仍然会继续运行
        // 也就是说此时程序还没有停止运行
    }
}
