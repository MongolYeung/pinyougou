package cn.itcast.dubboxdemo.controller;

import cn.itcast.dubboxdemo.service.UserService;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
    //远程调用
    @Reference
    private UserService userService;
    //获取用户名

    /**
     * 入参:  string类型 String ss = "{k:v,k:v,...}"
     * 问题: Json格式字符串转对象时 使用的是什么工具或是.jar进行Json转换
     *   jackson.jar
     *   上面的jar听说转换性能差,想换个好的,能换吗?能,怎么换?
     *
     * @return
     */

    @RequestMapping("/getName")
    @ResponseBody
    public String getName(){
        return userService.getName();
    }
}
