package com.qf.task;

import com.qf.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyTask {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Scheduled(cron = "0 0 0/1 * * *")
    public void mytask(){
        //更新秒杀场次
        //获得原来场次并删除
        String oldTime = redisTemplate.opsForValue().get("killgoods_now");
        redisTemplate.delete("killgoods_" + oldTime);

        //更新redis中的场次时间
        String time = DateUtil.get2Date(new Date(), "yyMMddHH");
        redisTemplate.opsForValue().set("killgoods_now", time);
    }
}
