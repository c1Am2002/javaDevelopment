package com.example.springboottestr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Indexed;
import org.springframework.web.bind.annotation.*;

@RestController
public class test {
    //这里是创建一个静态的threadlocal对象 并指定初始值为null
    private final static ThreadLocal<Integer> test1 = ThreadLocal.withInitial(() -> null);
    private final static ThreadLocal<Integer> test2 = new ThreadLocal<>();
    //上述两种方法一样 第一种相当于创建一个SuppliedThreadLocal类对象（threadlocal子类） 赋值给父类 然后获取值的时候就会从子类获取 这样写的好处是子类可以针对父类进行扩展 那么第一种创建
    //就可以使用子类新增的方法

    //这个是测试一个坑：使用threadlocal并不完全代表线程安全 threadlocal是确保线程间数据隔离 但是我们的服务是启动在tomcat上的 tomcat有个连接池 里面的链接会复用 那么就可能出现a用户使用线程1
    //配置了值1 然后b用户过来 没有更新值直接使用 如果正好是线程1运行的 那么就获取到a用户设置的值  所以每次接口使用threadlocal结束后 需要手动删除threadloacl的值
    @GetMapping("/test")
    public void test1(@RequestParam Integer test0){
        try{
            String before = Thread.currentThread().getName() + ":" + test1.get();
            test1.set(test0);
            String after = Thread.currentThread().getName() + ":" + test1.get();
            System.out.println("1" + "|" + before);
            System.out.println("2" + "|" + after);
        }
        finally {
            test1.remove();
        }
    }
}
