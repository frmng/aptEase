package com.myapp.aptease;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ApartmentForm extends AppCompatActivity {

    private ImageView imageView;
    private TextView addPicture, saveBtn, cancelBtn;
    private EditText apartmentType, monthlyPrice;
    private Spinner bedroomNumber, restroomNumber, kitchenNumber, livingRoomNumber;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE);
                        addPicture.setVisibility(View.GONE);
                    } catch (IOException e) {
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_form);

        imageView = findViewById(R.id.imageView);
        addPicture = findViewById(R.id.addpicture);
        saveBtn = findViewById(R.id.savebtn);
        cancelBtn = findViewById(R.id.cancelbtn);

        apartmentType = findViewById(R.id.apartmenttype);
        bedroomNumber = findViewById(R.id.bedroomnumber);
        restroomNumber = findViewById(R.id.restroomnumber);
        kitchenNumber = findViewById(R.id.kitchennumber);
        livingRoomNumber = findViewById(R.id.livingroomnnumber);
        monthlyPrice = findViewById(R.id.monthlyprice);

        addPicture.setOnClickListener(v -> openImagePicker());

        saveBtn.setOnClickListener(v -> {
            if (areFieldsValid()) {
                finishWithResult("Apartment added successfully!");
            }
        });

        cancelBtn.setOnClickListener(v -> finishWithResult(null));
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private boolean areFieldsValid() {
        if (apartmentType.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Apartment category is required", Toast.LENGTH_SHORT).show();
            return false;
        }
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
        if (monthlyPrice.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Monthly price is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void finishWithResult(String message) {
        Intent resultIntent = new Intent();
        if (message != null) {
            resultIntent.putExtra("message", message);
        }
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
