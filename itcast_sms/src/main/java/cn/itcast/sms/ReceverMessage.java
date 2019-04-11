package cn.itcast.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听器接收消息
 *
 */
@Component
public class ReceverMessage {

    @Autowired
    private CommonRpc commonRpc; //jdk  cglib

    //监听器
    @JmsListener(destination = "sms")
    public void receverMessage(Map<String,String> map){
        //发消息
        //1.手机号
        //2.模板id
        //3.签名
        //4.验证码

        commonRpc.sendSms(map);

    }
}
