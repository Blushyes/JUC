package xyz.blushyes.demo;

import xyz.blushyes.atomic.Student;

/**
 * 测试普通修改
 */
public class AtomicDemo2 {
    public static void main(String[] args) throws InterruptedException {
        Student student = new Student(0);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100000; ++i) {
                student.setScore(student.getScore() + 1);
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100000; ++i) {
                student.setScore(student.getScore() - 1);
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(student.getScore()); // random
    }
}