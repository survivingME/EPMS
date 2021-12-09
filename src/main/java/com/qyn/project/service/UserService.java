package com.qyn.project.service;

import com.qyn.project.entity.User;

public interface UserService {
    Integer checkUser(String name);

    void addUser(User user);

    User select(User user);

    void alterpwd(User user);
}
