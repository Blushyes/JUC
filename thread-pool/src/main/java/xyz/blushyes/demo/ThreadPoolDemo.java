package xyz.blushyes.demo;

import lombok.extern.slf4j.Slf4j;
import xyz.blushyes.pool.ThreadPool;
import xyz.blushyes.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ThreadPoolDemo {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(6, 10);
        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            int finalI = i;
            tasks.add(()->{
                log.debug("第{}个任务执行中......", finalI + 1);
                ThreadUtil.stop(3);
            });
        }
        tasks.forEach(threadPool::execute);
    }
}
