package com.example.crypto.util;

import com.example.crypto.service.BeanUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisClient {
    
    private RedisTemplate template = BeanUtilService.getBean(RedisTemplate.class);

    Logger logger = LoggerFactory.getLogger(RedisClient.class);

    public Boolean addEntry(String currencyName, String subscribers) {
        logger.info(String.format("Redis:: Adding key -> %s with value --> %s \n", currencyName, subscribers));
        template.opsForValue().set(currencyName, subscribers);
        return true;
    }

    public String getAllEntriesForCurrency(String currencyName) {
        Object object = template.opsForValue().get(currencyName);
        logger.info(String.format("Redis:: Getting value for key -> %s --> %s \n", currencyName, object));
        return (String) object;
    }

}
