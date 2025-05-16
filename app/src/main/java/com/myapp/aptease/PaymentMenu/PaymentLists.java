package com.myapp.aptease.PaymentMenu;

public class PaymentLists {

    private String key;

    private String TenantPayment, RegisteredDate, MonthlyFee, Status;

    public PaymentLists(String tenantPayment, String registeredDate, String monthlyFee, String status) {
        TenantPayment = tenantPayment;
        RegisteredDate = registeredDate;
        MonthlyFee = monthlyFee;
        Status = status;
    }

    public PaymentLists() {}

    public String getTenantPayment() {
        return TenantPayment;
    }

    public void setTenantPayment(String tenantPayment) {
        TenantPayment = tenantPayment;
    }

    public String getRegisteredDate() {
        return RegisteredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        RegisteredDate = registeredDate;
    }

    public String getMonthlyFee() {
        return MonthlyFee;
    }

    public void setMonthlyFee(String monthlyFee) {
        MonthlyFee = monthlyFee;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
