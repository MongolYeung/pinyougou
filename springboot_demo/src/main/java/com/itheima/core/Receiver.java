package com.itheima.core;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 接收
 */
@Component
public class Receiver {

    //接收  SpringBoot 整合AcctiveMQ 发  接收 发到哪里去了  从哪里接收到消息
    //内嵌AcctiveMQ服务器中了
    @JmsListener(destination = "haha")
    public void receiver(String name){
        System.out.println(name);
    }
}
