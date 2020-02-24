package com.md.luck.lottery;

import com.md.luck.lottery.common.entity.JMenu;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LotteryApplicationTests {

//    /**
//     * 模拟mvc测试对象
//     */
//    private MockMvc mockMvc;
//
//    /**
//     * web项目上下文
//     */
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    /**
//     * 所有测试方法执行之前执行该方法
//     */
//    @Before
//    public void before() {
//        //获取mockmvc对象实例
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }
//
//    /**
//     * 测试添加商品
//     * @throws Exception
//     */
//    @Test
//    public void addGood() throws Exception
//    {
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/quartz/add"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().is(200))
//                .andReturn();
//        result.getResponse().setCharacterEncoding("UTF-8");
//        System.out.println(result.getResponse().getContentAsString());
//    }
//    /**
//     * 测试添加商品
//     * @throws Exception
//     */
//    @Test
//    public void delGood() throws Exception
//    {
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/quartz/del"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().is(200))
//                .andReturn();
//        result.getResponse().setCharacterEncoding("UTF-8");
//        System.out.println(result.getResponse().getContentAsString());
//    }
//
//    public void test(){
//        System.out.println("sdfdsf");
//    }
@Autowired
private RedisTemplate redisTemplate;

    @Test
    public void set999() {
//        redisTemplate.opsForValue().set("test:set1", "testValue1");
//        redisTemplate.opsForSet().add("test:set2", "asdf");
//        redisTemplate.opsForHash().put("hash1", "name1", "lms1");
//        redisTemplate.opsForHash().put("hash1", "name2", "lms2");
//        redisTemplate.opsForHash().put("hash1", "name3", "lms3");
//        System.out.println(redisTemplate.opsForValue().get("test:set"));
//        System.out.println(redisTemplate.opsForHash().get("hash1", "name1"));
//        redisTemplate.opsForZSet().incrementScore("zset", "s1", 9);
//        redisTemplate.opsForZSet().incrementScore("zset", "s2", 9);
//        redisTemplate.opsForZSet().incrementScore("zset", "s3", 10);
//        redisTemplate.opsForZSet().incrementScore("zset", "s4", 11);
//        redisTemplate.opsForZSet().incrementScore("zset", "s4", 1);
//        redisTemplate.opsForZSet().incrementScore("zset", "s5", 2);
        Set<Object> strings = redisTemplate.opsForZSet().range("zset", 0, 2);
        Set<Object> strings2 = redisTemplate.opsForZSet().reverseRange("zset", 0, 2);

        for (Object str : strings) {
            System.out.println(str);
        }
        System.out.println("----------");
        for (Object str : strings2) {
            System.out.println(str);
        }
        System.out.println(redisTemplate.opsForZSet().score("zset", "s4"));
        System.out.println(redisTemplate.opsForZSet().score("zset", "s1"));
        System.out.println(redisTemplate.opsForZSet().score("zset", "s5"));
        System.out.println(redisTemplate.opsForZSet().score("zset", "s2"));
        System.out.println(redisTemplate.opsForZSet().score("zset", "s1"));
        System.out.println(redisTemplate.opsForZSet().rank("zset", "s4"));
        System.out.println(redisTemplate.opsForZSet().rank("zset", "s5"));

        User user1 = (User) redisTemplate.opsForHash().get("user", "user");

        System.out.println(user1.getUsername());
//        JMenu jMenu =
//        System.out.println(redisTemplate.opsForHash().get("user", "user"));
    }
    @Test
    public void demo1() {
        //put方法
        redisTemplate.opsForHash().put("k1", "name", "baipengfei");
        redisTemplate.opsForHash().put("k1", "age", "22");
        redisTemplate.opsForHash().put("k1", "height", "176");

        //hashKey不存在时，才设值
        //redisTemplate.opsForHash().putIfAbsent(key, hashKey, value)
    }

    @Test
    public void demo2() {
        //putAll方法
        Map<String, String> data = new HashMap<>();
        data.put("name", "jack ma");
        data.put("company", "alibaba");
        data.put("age", "500");
        redisTemplate.opsForHash().putAll("k2", data);
    }

    @Test
    public void demo3() {
        //delete方法，删除key对应的hash的hashkey及其value
        redisTemplate.opsForHash().delete("k2", "name");
    }

    @Test
    public void demo4() {
        //hasKey方法，确定hashkey是否存在
        System.out.println(redisTemplate.opsForHash().hasKey("k2", "name"));
    }

    @Test
    public void demo5() {
        //get方法，根据key和hashkey找出对应的值
        System.out.println(redisTemplate.opsForHash().get("k1", "name"));
    }

    @Test
    public void demo6() {
        //multiGet方法，根据key和多个hashkey找出对应的多个值
        Collection<Object> keys = new ArrayList<>();
        keys.add("ma");
        keys.add("md");
        System.out.println(redisTemplate.opsForHash().multiGet("m", keys));
    }

    @Test
    public void demo7() {
        //increment方法，对key和hashkey对应的值进行增加操作
        //增加长整形（无法对浮点数据使用本方法）
        System.out.println(redisTemplate.opsForHash().increment("k1", "age", 1));
        //增加浮点型（可以对整形数据使用本方法）
        System.out.println(redisTemplate.opsForHash().increment("k1", "age", 1.0));
    }

    @Test
    public void demo8() {
        //keys方法，获取key对应的hash表的所有key
        Set<Object> keys = redisTemplate.opsForHash().keys("k1");
        System.out.println(keys);

        //values方法，获取key对应的hash表的所有value
        List<Object> values = redisTemplate.opsForHash().values("k1");
        System.out.println(values);
    }

    @Test
    public void demo9() {
        //keys方法，获取key对应的hash表的大小
        long size = redisTemplate.opsForHash().size("k1");
        System.out.println(size);
    }

    @Test
    public void demo10() {
        //keys方法，获取key对应的hash表的所有键值对
        Map<Object, Object> entries = redisTemplate.opsForHash().entries("k1");
        System.out.println(entries);
    }


}
