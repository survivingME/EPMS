package com.qyn.project.service.impl;

import com.qyn.project.dao.UserMapper;
import com.qyn.project.entity.User;
import com.qyn.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer checkUser(String name) {
        return userMapper.checkUser(name);
    }

    @Override
    public void addUser(User user) {
        userMapper.addUser(user);
    }

    @Override
    public User select(User user) {
        return userMapper.select(user);
    }

    @Override
    public void alterpwd(User user) {
        userMapper.alterpwd(user);
    }
}
