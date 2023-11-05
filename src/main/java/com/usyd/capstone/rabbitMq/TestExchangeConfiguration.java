package com.usyd.capstone.rabbitMq;

import com.usyd.capstone.common.config.Constants;
import com.usyd.capstone.common.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestExchangeConfiguration {

    private static Logger logger = LoggerFactory.getLogger(TestExchangeConfiguration.class);

    @Autowired
    private ServerConfig serverConfig;

    @Bean
    public FanoutExchange fanoutExchange() {
        logger.info("【【【交换机实例创建成功】】】");
        return new FanoutExchange(Constants.FANOUT_EXCHANGE_NAME);
    }


    @Bean
    public Queue notificationQueue() {
        logger.info("【【【消息推送队列实例创建成功】】】");
        //动态名称
        return new Queue(Constants.TEST_QUEUE1_NAME+"——"+serverConfig.getUrl());
    }



    @Bean
    public Binding bingQueue1ToExchange() {
        logger.info("【【【绑定队列到交换机成功】】】");
        return BindingBuilder.bind(notificationQueue()).to(fanoutExchange());
    }


    // 创建聊天室的交换机
    @Bean
    public FanoutExchange chatExchange() {
        logger.info("【【【聊天交换机实例创建成功】】】");
        return new FanoutExchange(Constants.CHAT_EXCHANGE_NAME);
    }

    // 创建聊天室的队列
    @Bean
    public Queue chatQueue() {
        logger.info("【【【聊天队列创建成功】】】");
        return new Queue(Constants.CHAT_QUEUE_NAME+"——"+serverConfig.getUrl());
    }

    // 将聊天室队列绑定到聊天室交换机
    @Bean
    public Binding bindingChatQueueToExchange() {
        logger.info("【【【绑定队列到聊天交换机成功】】】");
        return BindingBuilder.bind(chatQueue()).to(chatExchange());
    }


}

