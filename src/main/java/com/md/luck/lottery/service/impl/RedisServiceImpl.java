package com.md.luck.lottery.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * redis 相关操作
 */
@Service
public class RedisServiceImpl {
    @Autowired
    private RedisTemplate redisTemplate;
}
