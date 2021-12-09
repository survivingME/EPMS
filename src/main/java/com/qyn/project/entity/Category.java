package com.qyn.project.entity;

public class Category {
    public Category(Integer cid, String cname) {
        this.cid = cid;
        this.cname = cname;
    }

    private Integer cid;//类别id
    private String cname;//类别名字
    public Integer getCid() {
        return cid;
    }
    public void setCid(Integer cid) {
        this.cid = cid;
    }
    public String getCname() {
        return cname;
    }
    public void setCname(String cname) {
        this.cname = cname;
    }
}
