package com.example.springboottestr.controller.test5;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class kongzhiqiTest5 {
    @Resource
    private UserService userService;
    //这里因为不是没有 通过代理过的类从外部调用 导致事务失效
    @GetMapping("/test5")
    public void test5(@RequestParam("name") String name){
        System.out.println("请求的参数:" + name);
        System.out.println(userService.test51(name));
    }

    //这里因为是 通过代理过的类从外部调用 事务正常
    @GetMapping("/test52")
    public void test52(@RequestParam("name") String name){
        System.out.println("请求的参数:" + name);
        userService.addUser(new userDAO(name));
        System.out.println(userService.getCount(name));
    }
}
