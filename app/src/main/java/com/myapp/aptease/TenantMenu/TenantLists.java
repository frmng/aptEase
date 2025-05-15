package com.myapp.aptease.TenantMenu;

import java.io.Serializable;
import java.util.Date;

public class TenantLists implements Serializable  {

    private String key;
    String tenantName, apartmentType;
    int apartmentNumber;
    String contactNum;
    String registerDate;

    public TenantLists(String tenantName, String apartmentType, int apartmentNumber, String contactNum, String registerDate) {
        this.tenantName = tenantName;
        this.apartmentType = apartmentType;
        this.apartmentNumber = apartmentNumber;
        this.contactNum = contactNum;
        this.registerDate = registerDate;
    }

    public TenantLists(){}

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(String apartmentType) {
        this.apartmentType = apartmentType;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(int apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

}
