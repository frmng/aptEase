package com.myapp.aptease.TenantMenu;

import java.io.Serializable;

public class TenantLists implements Serializable  {

    String tenantName, apartmentType;
    int apartmentNumber, contactNum;

    public TenantLists(String tenantName, String apartmentType, int apartmentNumber, int contactNum) {
        this.tenantName = tenantName;
        this.apartmentType = apartmentType;
        this.apartmentNumber = apartmentNumber;
        this.contactNum = contactNum;
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

    public int getContactNum() {
        return contactNum;
    }

    public void setContactNum(int contactNum) {
        this.contactNum = contactNum;
    }


}
