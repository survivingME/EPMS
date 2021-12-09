package com.qyn.project.dao;

import com.qyn.project.entity.LendInfo;

import java.util.List;
import java.util.Map;

public interface LendInfoMapper {
    List<LendInfo> queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);

    void backbook(Map<String, Object> ret);

    Integer isLended(LendInfo lendInfo);

    Integer cardState(Integer reader_id);

    void addLead(LendInfo lendInfo);
}
