package com.myapp.aptease.ApartmentMenu;

import android.net.Uri;

public class AparmentCardLists {
    private String apartmentType;
    private String monthlyPrice;
    private String bedroomNumber;
    private String restroomNumber;
    private String kitchenNumber;
    private String livingRoomNumber;
    private String imageBase64;

    public AparmentCardLists() {}

    // ✅ Constructor with 6 parameters (used when image is not available)
    public AparmentCardLists(String apartmentType, String monthlyPrice, String bedroomNumber,
                             String restroomNumber, String kitchenNumber, String livingRoomNumber) {
        this.apartmentType = apartmentType;
        this.monthlyPrice = monthlyPrice;
        this.bedroomNumber = bedroomNumber;
        this.restroomNumber = restroomNumber;
        this.kitchenNumber = kitchenNumber;
        this.livingRoomNumber = livingRoomNumber;
    }

    // ✅ Constructor with image (used when saving or viewing with image)
    public AparmentCardLists(String apartmentType, String monthlyPrice, String bedroomNumber,
                             String restroomNumber, String kitchenNumber, String livingRoomNumber,
                             String imageBase64) {
        this.apartmentType = apartmentType;
        this.monthlyPrice = monthlyPrice;
        this.bedroomNumber = bedroomNumber;
        this.restroomNumber = restroomNumber;
        this.kitchenNumber = kitchenNumber;
        this.livingRoomNumber = livingRoomNumber;
        this.imageBase64 = imageBase64;
    }

    // Getters and setters
    public String getApartmentType() { return apartmentType; }
    public String getMonthlyPrice() { return monthlyPrice; }
    public String getBedroomNumber() { return bedroomNumber; }
    public String getRestroomNumber() { return restroomNumber; }
    public String getKitchenNumber() { return kitchenNumber; }
    public String getLivingRoomNumber() { return livingRoomNumber; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
}
