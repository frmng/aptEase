package com.myapp.aptease.TenantMenu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.aptease.R;

public class TenantForm extends AppCompatActivity {

    private EditText fullName, contactNumber, registerDate;
    private Spinner apartmentType, apartmentNumber;
    private TextView saveBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_form);

        // Initialize views
        fullName = findViewById(R.id.fullname);
        contactNumber = findViewById(R.id.contactnumber);
        registerDate = findViewById(R.id.registerdate);
        apartmentType = findViewById(R.id.apartmenttype);
        apartmentNumber = findViewById(R.id.apartmentnumber);
        saveBtn = findViewById(R.id.savebtn1);
        cancelBtn = findViewById(R.id.cancelbtn1);

        // Open calendar fragment on date selection
        registerDate.setOnClickListener(v -> openCalendarFragment());

        // Save button click listener
        saveBtn.setOnClickListener(v -> {
            if (areFieldsValid()) {
                // Create a TenantLists object to hold tenant data
                String tenantName = fullName.getText().toString().trim();
                String contactNum = contactNumber.getText().toString().trim();
                String apartmentTypeValue = apartmentType.getSelectedItem().toString();
                int apartmentNumberValue = Integer.parseInt(apartmentNumber.getSelectedItem().toString());
                String registerDateValue = registerDate.getText().toString().trim();

                // Create a TenantLists object with the tenant data
                TenantLists tenant = new TenantLists(tenantName, apartmentTypeValue, apartmentNumberValue, Integer.parseInt(contactNum));

                // Create intent to pass the tenant data back
                Intent resultIntent = new Intent();
                resultIntent.putExtra("tenant", tenant); //error
                setResult(RESULT_OK, resultIntent); // Set result to OK and pass the tenant object
                finish(); // Close the TenantForm activity
            }
        });

        // Cancel button click listener
        cancelBtn.setOnClickListener(v -> {
            setResult(RESULT_CANCELED); // Set result to CANCELED
            finish(); // Close the TenantForm activity
        });
    }

    // Method to open the calendar fragment
    private void openCalendarFragment() {
        // Calendar fragment can be opened as needed (not modified for now)
    }

    // Method to check if all fields are filled
    private boolean areFieldsValid() {
        if (fullName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Full name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (contactNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Contact number is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (apartmentType.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select an apartment type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (apartmentNumber.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select an apartment number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (registerDate.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Register date is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true; // All fields are filled
    }

    // Method to update the registerDate with the selected date
    public void setRegisterDate(String date) {
        registerDate.setText(date); // Set the selected date in the EditText
    }
}
