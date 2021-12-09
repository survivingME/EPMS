package com.qyn.project.service;

import com.qyn.project.entity.Relation;
import com.qyn.project.util.PageBean;

import java.util.Map;

public interface RelationService {

    PageBean<Relation> queryRelations(Map<String, Object> paramMap);

    void addRelation(Relation relation);

    Relation selectRelationByCode(String code);

    Relation selectRelationByName(String name);

    Integer checkByName(String name);

    Integer checkByCode(String code);

}
