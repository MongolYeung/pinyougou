package cn.itcast.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 值类型 String类型
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:*.xml"})
public class SpringDataRedisDemo {

    @Autowired
    private RedisTemplate redisTemplate;

    //添加   //修改
    @Test
    public void testAdd(){
        //张三 22
        redisTemplate.boundValueOps("wangwu").set(22);
    }



    //删除
    @Test
    public void testDelete(){
        redisTemplate.delete("wangwu");

        //1.Redis缓存 有事务吗?
        //有事务 : 有 弱事务 一个特性 原子性    强事务  ACID
        //redis: 只能处理一个请求 此请求不处理完 是不会处理下一个的
        //zhangsan 22 incr zhangsan 23


    }

    //查询
    @Test
    public void testFind(){
        Object age = redisTemplate.boundValueOps("wangwu").get();
        System.out.println(age);
    }


}
