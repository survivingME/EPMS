package com.qyn.project.pojo;

import com.qyn.project.entity.Production;

public class CreateProductionResult {
    private int statusCode;
    private Production production;

    public CreateProductionResult(){}

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Production getProduction() {
        return production;
    }

    public void setProduction(Production production) {
        this.production = production;
    }
}
