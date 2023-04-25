package xyz.blushyes.demo;

import xyz.blushyes.atomic.StudentUseFieldUpdater;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.LongAdder;

/**
 * 测试字段更新器，可与demo3对比
 */
public class AtomicDemo5 {
    public static void main(String[] args) throws InterruptedException {
        AtomicReferenceFieldUpdater<StudentUseFieldUpdater, Float> scoreUpdater = AtomicReferenceFieldUpdater.newUpdater(StudentUseFieldUpdater.class, Float.class, "score");

        StudentUseFieldUpdater student = new StudentUseFieldUpdater(0);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; ++i) {
                scoreUpdater.updateAndGet(student, score -> score + 1);
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10000; ++i) {
                scoreUpdater.updateAndGet(student, score -> score - 1);
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(student.score);   // 0.0
    }
}