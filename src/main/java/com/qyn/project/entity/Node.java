package com.qyn.project.entity;

import java.util.Map;

public class Node implements Map.Entry<String ,String> {
    private String key;
    private String value;

    public Node(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String setValue(String value) {
        String oldValue = this.value;
        this.value = value;
        return oldValue;
    }
}
