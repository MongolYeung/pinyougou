package com.itheima.core.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * 测试
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private Environment env;

    @RequestMapping("/getName")
    public String getName(){
        return "springBoot:" + env.getProperty("url") + ":" + env.getProperty("server.port");
    }

    @Autowired
    private JmsTemplate jmsTemplate;
    //发消息
    @RequestMapping("/sendMessage")
    public String sendMessage(final String name){
        jmsTemplate.send("haha", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(name);
            }
        });
        return "OK";
    }
}
