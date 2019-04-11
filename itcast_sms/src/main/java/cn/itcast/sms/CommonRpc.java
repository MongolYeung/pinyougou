package cn.itcast.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

/*
pom.xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>4.0.3</version>
</dependency>
*/
@SuppressWarnings("all")
@Component
public class CommonRpc {

    @Autowired
    private Environment env;

    //发短信 入参Map
    public void sendSms(Map<String,String> map) {
        DefaultProfile profile = DefaultProfile.getProfile("default",
                env.getProperty("AccessKeyID"), env.getProperty("AccessKeySecret"));
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        //手机号
        request.putQueryParameter("PhoneNumbers", map.get("PhoneNumbers"));//"15072174810"
        //签名
        request.putQueryParameter("SignName", map.get("SignName"));//"碧潇"
        //模板ID
        request.putQueryParameter("TemplateCode", map.get("TemplateCode"));//"SMS_162631020"
        //您好,欢迎注册品优购商城,您的验证码为${number}
        request.putQueryParameter("TemplateParam", map.get("TemplateParam"));//"{\"number\":\"111111\"}"
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}