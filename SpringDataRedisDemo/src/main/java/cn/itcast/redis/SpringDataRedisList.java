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
 * List类型
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:*.xml"})
public class SpringDataRedisList {

    @Autowired
    private RedisTemplate redisTemplate;

    //添加
    @Test
    public void testAdd(){
        redisTemplate.boundListOps("list").leftPush("赵云");
        redisTemplate.boundListOps("list").rightPush("刘备");
        redisTemplate.boundListOps("list").rightPush("诸葛亮");
        redisTemplate.boundListOps("list").leftPush("黄忠");
        redisTemplate.boundListOps("list").rightPush("周瑜");
    }

    //修改
    @Test
    public void testUpdate(){
        redisTemplate.boundListOps("list").set(3,"马超");
    }


    //删除
    @Test
    public void testDelete(){
        //redisTemplate.boundListOps("list").remove(4, "刘备");
        //参数1: 删除的个数  >0 从左往右删  <0  从右往左删 =0 全部删除
        redisTemplate.boundListOps("list").remove(0, "诸葛亮");

        //redisTemplate.delete("list");

    }

    //查询
    @Test
    public void testFind(){
        List list = redisTemplate.boundListOps("list").range(0, -1);
        for (Object o : list) {
            System.out.println(o);
        }


    }
    
}
