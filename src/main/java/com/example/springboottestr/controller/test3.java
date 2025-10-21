package com.example.springboottestr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class test3 {
    @GetMapping("/test3")
    public void test3() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        print(threadPoolExecutor);
        IntStream.rangeClosed(1, 20).forEach(i ->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Integer s = atomicInteger.incrementAndGet();
            threadPoolExecutor.submit(() ->{
                System.out.println( s + "开始");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(s + "结束");
            });
        });
    }

    public void print(ThreadPoolExecutor threadPoolExecutor) throws InterruptedException {
//        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() ->{
//            System.out.println("-----------------------");
//            System.out.println("线程池大小:" + threadPoolExecutor.getPoolSize());
//            System.out.println("活跃的数量:" + threadPoolExecutor.getActiveCount());
//            System.out.println("完成的任务数:" + threadPoolExecutor.getCompletedTaskCount());
//            System.out.println("任务队列的任务数:" + threadPoolExecutor.getQueue().size());
//        }, 0, 1, TimeUnit.SECONDS);
        //这样不行 因为这样是主线程在打印 会阻塞 后面的代码就没法执行了
        while(true){
            System.out.println("-----------------------");
            System.out.println("线程池大小:" + threadPoolExecutor.getPoolSize());
            System.out.println("活跃的数量:" + threadPoolExecutor.getActiveCount());
            System.out.println("完成的任务数:" + threadPoolExecutor.getCompletedTaskCount());
            System.out.println("任务队列的任务数:" + threadPoolExecutor.getQueue().size());
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
