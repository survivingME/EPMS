package com.qyn.project.dao;

import com.qyn.project.entity.Category;

import java.util.ArrayList;

public interface TypeMapper {
    ArrayList<Category> listCategory();

    void updateBookType(Category category);

    void delBookType(Integer cid);

    void addBookType(String cname);
}
