package com.myapp.aptease.PaymentMenu;

public class TenantPaymentHistory {
    private String monthlyFee;
    private String status;
    private String datePaid;

    public TenantPaymentHistory() {
    }

    public TenantPaymentHistory(String monthlyFee, String status, String datePaid) {
        this.monthlyFee = monthlyFee;
        this.status = status;
        this.datePaid = datePaid;
    }

    public String getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(String monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(String datePaid) {
        this.datePaid = datePaid;
    }
}