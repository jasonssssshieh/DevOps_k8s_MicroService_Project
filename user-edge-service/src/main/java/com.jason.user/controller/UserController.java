package com.jason.user.controller;

import com.jason.thrift.user.UserInfo;
import com.jason.user.response.Response;
import com.jason.user.thrift.ServiceProvider;
import org.apache.thrift.TException;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Controller
public class UserController {

    @Autowired
    private ServiceProvider serviceProvider;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response login(@RequestParam("username") String username, @RequestParam("password") String password){

        //1. 验证用户名和密码
        UserInfo userInfo = null;
        try {
            userInfo = serviceProvider.getUserService().getUserByName(username);
        } catch (TException e) {
            e.printStackTrace();
            return Response.USERNAME_PASSWORD_INVALIDATE;
        }

        if(userInfo == null) {
            return Response.USERNAME_PASSWORD_INVALIDATE;
        }

        //数据库里的password一般会使用md5的方式,
        if(!userInfo.getPassword().equals(md5(password))){
            return Response.USERNAME_PASSWORD_INVALIDATE;
        }
        //2. 生成token 随机的 唯一标示, 单点登录
        String token = genToken();

        //3. 缓存用户=>redis 需要启动redis



    }

    private String genToken(){
        return randomCode("012345679abcdefghijklmnopqrstuvwxyz", 32);
    }

    private String randomCode(String s, int size){
        StringBuilder result = new StringBuilder(size);

        Random random = new Random();
        for(int i = 0; i < size; ++i){
            int index = random.nextInt(s.length);
            result.append(s.charAt(index));
        }
        return result.toString();
    }

    private String md5(String password){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(password.getBytes(Charset.forName("UTF-8")));
            return HexUtils.toHexString(md5Bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
