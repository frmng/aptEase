package com.myapp.aptease.TenantMenu;

import java.io.Serializable;
import java.util.Date;

public class TenantLists implements Serializable  {

    String tenantName, apartmentType;
    int apartmentNumber, contactNum;
    Date registerDate;

    public TenantLists(String tenantName, String apartmentType, int apartmentNumber, int contactNum, Date registerDate) {
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

    public int getContactNum() {
        return contactNum;
    }

    public void setContactNum(int contactNum) {
        this.contactNum = contactNum;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }


}
