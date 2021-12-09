package com.qyn.project.entity;

import java.io.Serializable;

public class Production implements Serializable {
    private static final long serialVersionUID = 2223540124348297618L;
    private String company;
    private String name;
    private String serial;

    private boolean mapFlag = false;

    private String companyCode;
    private String nameCode;

    private String EPCBin;
    private String UIDBin;

    public boolean isMapFlag() {
        return mapFlag;
    }

    public void setMapFlag(boolean mapFlag) {
        this.mapFlag = mapFlag;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getNameCode() {
        return nameCode;
    }

    public void setNameCode(String nameCode) {
        this.nameCode = nameCode;
    }

    public String getEPCBin() {
        return EPCBin;
    }

    public void setEPCBin(String EPCBin) {
        this.EPCBin = EPCBin;
    }

    public String getUIDBin() {
        return UIDBin;
    }

    public void setUIDBin(String UIDBin) {
        this.UIDBin = UIDBin;
    }

    @Override
    public String toString() {
        return "Production{" +
                "company='" + company + '\'' +
                ", name='" + name + '\'' +
                ", serial='" + serial + '\'' +
                ", mapFlag=" + mapFlag +
                ", companyCode='" + companyCode + '\'' +
                ", nameCode='" + nameCode + '\'' +
                ", EPCBin='" + EPCBin + '\'' +
                ", UIDBin='" + UIDBin + '\'' +
                '}';
    }
}
