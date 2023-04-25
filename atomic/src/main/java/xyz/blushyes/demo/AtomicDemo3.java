package xyz.blushyes.demo;

import xyz.blushyes.atomic.Student;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 测试原子引用是否会让对成员变量的操作变为原子操作
 */
public class AtomicDemo3 {
    public static void main(String[] args) throws InterruptedException {
        AtomicReference<Student> student = new AtomicReference<>(new Student(0));

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; ++i) {
                student.updateAndGet(stu -> {
                    stu.setScore(stu.getScore() + 1);
                    return stu;
                });
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10000; ++i) {
                student.updateAndGet(stu -> {
                    stu.setScore(stu.getScore() - 1);
                    return stu;
                });
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(student.get().getScore());   // random
    }
}