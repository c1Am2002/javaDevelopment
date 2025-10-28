package com.example.springboottestr.controller.test5;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private chaxun chaxun1;

    public int test51(String name){
        this.addUser(new userDAO(name));
        return chaxun1.findByName(name).size();
    }
    @Transactional
    public void addUser(userDAO userDAO){
        chaxun1.save(userDAO);
        if(userDAO.getName().contains("test")){
            throw new RuntimeException("名称错误 事务回滚");
        }
    }//估计因为版本问题 我这里使用transactional注解必须指定方法为public 但是这样就没法触发事务失效的情况
    //具体而言当时事务失效是spring默认通过AOP动态代理来增强对象 而private没法被代理 也就没法被增强 所谓增强也就是事务的功能 所以事务失效

    //不对 这里还是没有生效 因为必须通过代理过的类从外部调用目标方法才能生效。也就是其他的类调用这个类的被@Transactional标注的方法 其次这里的被代理过的类就是chaxun类

    public int getCount(String name){
        return chaxun1.findByName(name).size();
    }
}
