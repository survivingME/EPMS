package com.qyn.project.dao;

import com.qyn.project.entity.User;

public interface UserMapper {
    User select(User user);

    void alterpwd(User user);

    int checkUser(String name);

    void addUser(User user);
}
