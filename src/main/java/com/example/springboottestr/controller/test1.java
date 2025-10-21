package com.example.springboottestr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@RestController
public class test1 {

    private int threadMax = 10;
    private int numMax = 1000;
    @GetMapping("/test1")
    public  void  test2() throws InterruptedException {
        ConcurrentHashMap<String, Long> testHashMap = addData(numMax - 100);
        System.out.println("哈希表数据大小：" + testHashMap.size());
        ForkJoinPool forkJoinPool = new ForkJoinPool(threadMax);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
            int gap = numMax - testHashMap.size();
            System.out.println("当前差多少元素:" + gap);
            //补全
            testHashMap.putAll(addData(gap));
        }));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("当前哈希表大小:" + testHashMap.size());
        //这里会出现一个i问题 由于第一个线程观察到差100 然后往里面添加一百 在添加时 后续线程观察到里面差0-100的值（此时线程1未添加完） 这就导致多添加了数据 所以concurrentHashMap对线程间的操作是线程安全的
        //但是如果线程间操作时 一个线程会根据另一个线程操作中的数据 决定接下来的操作 这样就导致了线程不安全
    }
    public ConcurrentHashMap<String ,Long>addData(int max){
        return LongStream.rangeClosed(1, max)//设置map元素个数
                .boxed()//进行类型装箱 将long类型改为Long类型
                //生成一个concurrentHashMap 其中key通过UUID生成 第二个参数就是max本地 比如3 那么就是分别设置三个对象 三个对象的value分别是（1，2，3） 第三个参数表示如果出现相同的key 那么取前者
                //也就是重新生成后者 第四个就是指定生成一个concurrentHashMap
                .collect(Collectors.toConcurrentMap(i -> UUID.randomUUID().toString(), Function.identity(), (o1, o2) -> o1, ConcurrentHashMap::new));
    }
}
