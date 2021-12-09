package com.qyn.project.pojo;

import com.qyn.project.entity.Node;
import java.util.List;

public class MappingQueryResult {
    private List<Node> resultSet;
    private int map;

    public MappingQueryResult(){}

    public List<Node> getResultSet() {
        return resultSet;
    }

    public void setResultSet(List<Node> resultSet) {
        this.resultSet = resultSet;
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }
}
