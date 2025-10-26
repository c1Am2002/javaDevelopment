package com.example.springboottestr.controller;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;

@RestController
public class test4 {
    @GetMapping("/test4")
    public void test4() throws InterruptedException {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        //密码认证
        jedis.auth("123456");
        //(如果第一个参数不是ture 那么抛出异常 异常信息是第二个参数的值
        Assert.isTrue("OK".equals(jedis.set("a", "1")), "set a = 1 return OK");
        Assert.isTrue("OK".equals(jedis.set("b", "2")), "set b = 2 return OK");
        jedis.close();
        TimeUnit.SECONDS.sleep(10);
        Jedis jedis1 = new Jedis("127.0.0.1", 6379);
        jedis1.auth("123456");
        new Thread(() ->{
            for(int i = 0;i<1000 ;i++){
                String a = jedis1.get("a");
                if(!a.equals("1")){
                    System.out.println("a数据读取异常");
                    return;
                }
            }
        }).start();
        new Thread(() -> {
            for(int i = 0;i<1000 ;i++) {
                String b = jedis1.get("b");
                if (!b.equals("2")) {
                    System.out.println("b数据读取异常");
                    return;
                }
            }
        }).start();
    }
    //经过测试 出现了 1.打印了a或者b数据读取异常 2.redis链接报错（包括流读取异常和关闭链接异常）

    //原因：首先jedis继承BinaryClient 而BinaryClient有一个Clien对象 Client对象继承于Connection 而Connection有一个socket以及一个写入一个读取流 也就是一个jedis对应一个写入或者读取流
    //我们使用jedis命令的本质是调用Connection写好的代码 而Connection会调用Protocol类的sendCommand 来操作数据 具体代码如下
//    private static void sendCommand(final RedisOutputStream os, final byte[] command,
//                                    final byte[]... args) {
//        try {
//            os.write(ASTERISK_BYTE);
//            os.writeIntCrLf(args.length + 1);
//            os.write(DOLLAR_BYTE);
//            os.writeIntCrLf(command.length);
//            os.write(command);
//            os.writeCrLf();
//            for (final byte[] arg : args) {
//                os.write(DOLLAR_BYTE);
//                os.writeIntCrLf(arg.length);
//                os.write(arg);
//                os.writeCrLf();
//            }
//        } catch (IOException e) {
//            throw new JedisConnectionException(e);
//        }
//    }
    //可以看出它是直接操作RedisOutputStream对象 那么我们复用jedis本质就是复用RedisOutputStream对象 对象只有一个 而这段方法又没有任何锁 也就会出现线程不安全的问题 也就会导致连接异常和数据读取异常
    //具体的解决方法就是使用jedisPool 这是它对应的线程安全的类 通过static修饰 申明jedisPool可以在多个线程间共享
}
