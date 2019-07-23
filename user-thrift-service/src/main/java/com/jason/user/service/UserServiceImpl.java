package com.jason.user.service;

import com.jason.thrift.user.UserInfo;
import com.jason.thrift.user.UserService;
import com.jason.user.mapper.UserMapper;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//只需要实现Iface这些接口
//mybatis连接数据
@Service
public class UserServiceImpl implements UserService.Iface {


    @Autowired
    private UserMapper userMapper;

    @Override
    public UserInfo getUserById(int id) throws TException {
        return userMapper.getUserById(id);
    }

    @Override
    public UserInfo getUserByName(String username) throws TException {
        return userMapper.getUserByName(username);
    }

    @Override
    public void registerUser(UserInfo userInfo) throws TException {
        userMapper.registerUser(userInfo);
    }
}
