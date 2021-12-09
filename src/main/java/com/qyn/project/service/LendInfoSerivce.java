package com.qyn.project.service;

import com.qyn.project.util.PageBean;
import com.qyn.project.entity.LendInfo;

import java.util.Map;

public interface LendInfoSerivce {
    PageBean<LendInfo> queryLeadInfoPage(Map<String, Object> paramMap);

    void backBook(Map<String, Object> ret);

    boolean isLended(LendInfo lendInfo);

    Integer cardState(Integer reader_id);

    void lendBook(LendInfo lendInfo);
}
