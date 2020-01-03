package com.qf.shop_goods;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean
    public FanoutExchange getExchange(){
        return new FanoutExchange("goods_exchange");
    }

    @Bean
    public Queue getQueue(){
        return new Queue("kill_queue");
    }

    @Bean
    public Binding getBingd(FanoutExchange getExchange, Queue getQueue){
        return BindingBuilder.bind(getQueue).to(getExchange);
    }

}
