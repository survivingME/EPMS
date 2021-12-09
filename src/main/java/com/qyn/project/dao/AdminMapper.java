package com.qyn.project.dao;

import com.qyn.project.entity.Admin;

public interface AdminMapper {
    Admin select(Admin ad);

    void alterpwd(Admin ad);
}
