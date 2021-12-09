package com.qyn.project.service.impl;

import com.qyn.project.dao.RelationMapper;
import com.qyn.project.entity.Relation;
import com.qyn.project.service.RelationService;
import com.qyn.project.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RelationServiceImpl implements RelationService {

    @Autowired
    private RelationMapper relationMapper;

    @Override
    public PageBean<Relation> queryRelations(Map<String, Object> paramMap) {
        PageBean<Relation> pageBean = new PageBean<>((Integer) paramMap.get("pageno"), (Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex", startIndex);
        List<Relation> relations = relationMapper.selectRelationsByType(paramMap);
        pageBean.setDatas(relations);

        Integer totalSize = relationMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalSize);

        return pageBean;
    }

    @Override
    public void addRelation(Relation relation) {
        relationMapper.addRelation(relation);
    }

    @Override
    public Relation selectRelationByCode(String code) {
        return relationMapper.selectRelationByCode(code);
    }

    @Override
    public Relation selectRelationByName(String name) {
        return relationMapper.selectRelationByName(name);
    }

    @Override
    public Integer checkByName(String name) {
        return relationMapper.checkByName(name);
    }

    @Override
    public Integer checkByCode(String code) {
        return relationMapper.checkByCode(code);
    }

}
