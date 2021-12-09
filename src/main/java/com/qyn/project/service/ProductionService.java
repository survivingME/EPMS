package com.qyn.project.service;

import com.qyn.project.pojo.CreateProductionResult;

public interface ProductionService {

    CreateProductionResult createProduction(String company, String name, String companyCode, String nameCode);

}
