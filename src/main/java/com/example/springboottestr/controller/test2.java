package com.example.springboottestr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RestController
public class test2 {
    private List<Integer> data = new ArrayList<>();
    volatile  int a= 1;
    volatile  int b = 1;
    @GetMapping("/test2")
    public void test2()
    {
        new Thread(this::add).start();
        new Thread(this::compare).start();
    }
    @GetMapping("/test21")
    public void test21(){
        //给整段方法加锁: 运行时间:10.51s
        IntStream.rangeClosed(1, 1000).parallel().forEach(i ->{
            synchronized (this){
                System.out.println("开始操作");
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                data.add(i);
            }
        });

        //单独给添加list的操作加锁：0.784s
//        IntStream.rangeClosed(1, 1000).parallel().forEach(i ->{
//            try {
//                TimeUnit.MILLISECONDS.sleep(10);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            synchronized (data){
//                data.add(i);
//            }
//        });
        //在开发中 需要加锁的时候一定要将锁的粒度降到最低 这样才能更好的提高性能
    }

    //如果两个线程对两个变量进行操作 一个是让变量自增 另一个是比较两个变量的值是否相当 按理说应该是一直相等 但是有个问题 就是两个线程是交替进行的
    // 比如线程1正在给变量进行自增 这个时候线程2来判断两个变量是否相当 变量1已经自增 但是变量2没有 这就会导致两个变量在同一个线程自增 但是比较时出现值不一样的情况
    // 所以需要对两个操作进行加锁 也就是通过synchronized修饰方法 确保线程安全
    public void add(){
        for(int i = 0;i<1000;i++){
            System.out.println("a ,b开始自增");
            a++;
            b++;
        }
    }

    public void compare(){
        System.out.println("开始比较");
        for(int i=0;i<1000;i++){
            if(a > b){
                System.out.println("a大于b");
            }
        }
        System.out.println("比较结束");
    }
}
