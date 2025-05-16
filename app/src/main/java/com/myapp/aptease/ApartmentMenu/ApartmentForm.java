package com.myapp.aptease.ApartmentMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.myapp.aptease.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ApartmentForm extends AppCompatActivity {

    private TextView saveBtn, cancelBtn;
    private EditText apartmentType, monthlyPrice;
    private Spinner bedroomNumber, restroomNumber, kitchenNumber, livingRoomNumber;
    private CardView addImage;

    private DatabaseReference database;

    private static final int PICK_IMAGE_REQUEST = 1;
    private String base64Image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_form);

        // Initialize views
        saveBtn = findViewById(R.id.savebtn);
        cancelBtn = findViewById(R.id.cancelbtn);
        apartmentType = findViewById(R.id.apartmenttype);
        bedroomNumber = findViewById(R.id.bedroom_number);
        restroomNumber = findViewById(R.id.restroom_number);
        kitchenNumber = findViewById(R.id.kitchen_number);
        livingRoomNumber = findViewById(R.id.livingroom_nnumber);
        monthlyPrice = findViewById(R.id.monthly_price);
        addImage = findViewById(R.id.addpicture);

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("apartments");

        // Set click listeners
        saveBtn.setOnClickListener(v -> {
            if (areFieldsValid()) {
                checkIfApartmentExists();  // Check if apartment exists before saving
            }
        });
        cancelBtn.setOnClickListener(v -> finishWithResult());

        addImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                byte[] imageBytes = new byte[inputStream.available()];
                inputStream.read(imageBytes);
                base64Image = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
                Toast.makeText(this, "Image selected!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to encode image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkIfApartmentExists() {
        String apartmentTypeText = apartmentType.getText().toString();
        String bedroomNumberText = bedroomNumber.getSelectedItem().toString();
        String restroomNumberText = restroomNumber.getSelectedItem().toString();
        String kitchenNumberText = kitchenNumber.getSelectedItem().toString();
        String livingRoomNumberText = livingRoomNumber.getSelectedItem().toString();
        String monthlyPriceText = monthlyPrice.getText().toString();

        // Query the database to check if an apartment with the same properties already exists
        Query query = database.orderByChild("apartmentType").equalTo(apartmentTypeText);
        query.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                boolean isDuplicate = false;

                // Check if any apartments with the same details already exist
                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String existingBedroomNumber = snapshot.child("bedroomNumber").getValue(String.class);
                    String existingRestroomNumber = snapshot.child("restroomNumber").getValue(String.class);
                    String existingKitchenNumber = snapshot.child("kitchenNumber").getValue(String.class);
                    String existingLivingRoomNumber = snapshot.child("livingRoomNumber").getValue(String.class);
                    String existingMonthlyPrice = snapshot.child("monthlyPrice").getValue(String.class);

                    // If an apartment with the same details is found, mark it as a duplicate
                    if (existingBedroomNumber.equals(bedroomNumberText)
                            && existingRestroomNumber.equals(restroomNumberText)
                            && existingKitchenNumber.equals(kitchenNumberText)
                            && existingLivingRoomNumber.equals(livingRoomNumberText)
                            && existingMonthlyPrice.equals(monthlyPriceText)) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (isDuplicate) {
                    Toast.makeText(ApartmentForm.this, "This apartment already exists.", Toast.LENGTH_SHORT).show();
                } else {
                    // If no duplicate, proceed with saving the apartment
                    saveApartmentToDatabase();
                }
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                Toast.makeText(ApartmentForm.this, "Error checking for duplicate", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveApartmentToDatabase() {
        String apartmentTypeText = apartmentType.getText().toString();
        String bedroomNumberText = bedroomNumber.getSelectedItem().toString();
        String restroomNumberText = restroomNumber.getSelectedItem().toString();
        String kitchenNumberText = kitchenNumber.getSelectedItem().toString();
        String livingRoomNumberText = livingRoomNumber.getSelectedItem().toString();
        String monthlyPriceText = monthlyPrice.getText().toString();

        String apartmentId = database.push().getKey();

        if (apartmentId != null) {
            Map<String, Object> apartmentValues = new HashMap<>();
            apartmentValues.put("apartmentType", apartmentTypeText);
            apartmentValues.put("bedroomNumber", bedroomNumberText);
            apartmentValues.put("restroomNumber", restroomNumberText);
            apartmentValues.put("kitchenNumber", kitchenNumberText);
            apartmentValues.put("livingRoomNumber", livingRoomNumberText);
            apartmentValues.put("monthlyPrice", monthlyPriceText);
            apartmentValues.put("imageBase64", base64Image);

            database.child(apartmentId).setValue(apartmentValues)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("apartmentType", apartmentTypeText);
                            resultIntent.putExtra("bedroomNumber", bedroomNumberText);
                            resultIntent.putExtra("restroomNumber", restroomNumberText);
                            resultIntent.putExtra("kitchenNumber", kitchenNumberText);
                            resultIntent.putExtra("livingRoomNumber", livingRoomNumberText);
                            resultIntent.putExtra("monthlyPrice", monthlyPriceText);
                            resultIntent.putExtra("base64Image", base64Image);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        } else {
                            Toast.makeText(ApartmentForm.this, "Failed to save apartment", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(ApartmentForm.this, "Failed to generate apartment ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void finishWithResult() {
        // Finish activity and send data back to the fragment
        setResult(RESULT_OK, new Intent());
        finish();
    }

    private boolean areFieldsValid() {
        if (apartmentType.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Apartment category is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate spinner selections
        if (bedroomNumber.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select the number of bedrooms", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (restroomNumber.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select the number of restrooms", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (kitchenNumber.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select the number of kitchens", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (livingRoomNumber.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select the number of living rooms", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate monthly price
        String price = monthlyPrice.getText().toString().replaceAll("[^0-9.]", "").trim();

        if (price.isEmpty()) {
            Toast.makeText(this, "Monthly price is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            Double.parseDouble(price);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
