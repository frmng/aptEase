package com.myapp.aptease.ApartmentMenu;

import android.net.Uri;

public class AparmentCardLists {
    private String apartmentType;
    private String  monthlyPrice;
    private String bedroomNumber;
    private String restroomNumber;
    private String kitchenNumber;
    private String livingRoomNumber;
//    private String imageBase64;

    // Default constructor for Firebase
    public AparmentCardLists() {}

    public AparmentCardLists(String apartmentType, String monthlyPrice, String bedroomNumber,
                             String restroomNumber, String kitchenNumber, String livingRoomNumber) {
        this.apartmentType = apartmentType;
        this.monthlyPrice = monthlyPrice;
        this.bedroomNumber = bedroomNumber;
        this.restroomNumber = restroomNumber;
        this.kitchenNumber = kitchenNumber;
        this.livingRoomNumber = livingRoomNumber;
    }


    // Getter methods
    public String getApartmentType() { return apartmentType; }
    public String getMonthlyPrice() { return monthlyPrice; }
    public String getBedroomNumber() { return bedroomNumber; }
    public String getRestroomNumber() { return restroomNumber; }
    public String getKitchenNumber() { return kitchenNumber; }
    public String getLivingRoomNumber() { return livingRoomNumber; }
//    public String getImageBase64() {
//        return imageBase64;
//    }
//
//    public void setImageBase64(String imageBase64) {
//        this.imageBase64 = imageBase64;
//    }
}
