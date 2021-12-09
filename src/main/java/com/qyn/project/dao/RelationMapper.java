package com.qyn.project.dao;

import com.qyn.project.entity.Relation;

import java.util.List;
import java.util.Map;

public interface RelationMapper {
    List<Relation> selectRelationsByType(Map<String ,Object> paramMap);

    void addRelation(Relation relation);

    Relation selectRelationByCode(String code);

    Relation selectRelationByName(String name);

    Integer checkByName(String name);

    Integer checkByCode(String code);

    Integer queryCount(Map<String, Object> paramMap);
}
