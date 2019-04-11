package cn.itcast.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Hash类型
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:*.xml"})
public class SpringDataRedisHash {

    @Autowired
    private RedisTemplate redisTemplate;

    //添加 //修改
    @Test
    public void testAdd(){
        //User 对象
        //ID 名称 年龄 性别 ......50个属性
        //redisTemplate.boundValueOps("user").set(user);
        //缺点:
        redisTemplate.boundHashOps("user").put("l", "刘备");
        redisTemplate.boundHashOps("user").put("z", "张飞");
        redisTemplate.boundHashOps("user").put("g", "关羽");
    }



    //删除
    @Test
    public void testDelete(){

        redisTemplate.boundHashOps("user").delete("l", "z");

    }

    //查询
    @Test
    public void testFind(){

        Object o = redisTemplate.boundHashOps("user").get("1");
        System.out.println(o);

        List userList = redisTemplate.boundHashOps("user").values();
        for (Object o1 : userList) {
            System.out.println(o1);
        }

        Set keysList = redisTemplate.boundHashOps("user").keys();
        for (Object o1 : keysList) {
            System.out.println(o1);
        }

        Map<String,String> user = redisTemplate.boundHashOps("user").entries();
        Set<Map.Entry<String, String>> entries = user.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }
    
}
