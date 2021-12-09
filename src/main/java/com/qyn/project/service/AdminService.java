package com.qyn.project.service;

import com.qyn.project.entity.Admin;

public interface AdminService {
    Admin select(Admin ad);

    void alterpwd(Admin ad);
}
