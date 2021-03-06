package cn.itcast.core.listener;

import cn.itcast.core.service.StaticPageService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 自定义实现类
 */
public class PageListener implements MessageListener {

    @Autowired
    private StaticPageService staticPageService;
    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage atm = (ActiveMQTextMessage) message;
        try {
            String id = atm.getText();
            System.out.println("静态化的项目接收到的ID:" + id);


            //3:将商品信息静态化处理
            staticPageService.index(Long.parseLong(id));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
