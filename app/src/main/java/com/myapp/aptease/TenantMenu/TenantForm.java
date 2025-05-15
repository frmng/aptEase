package com.myapp.aptease.TenantMenu;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myapp.aptease.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TenantForm extends AppCompatActivity {

    private EditText fullName, contactNumber, registerDate;
    private EditText apartmentType, apartmentNumber;
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

        String type = getIntent().getStringExtra("apartmentType");
        if (type != null && !type.isEmpty()) {
            apartmentType.setText(type);
        }

        // Open calendar fragment on date selection
        registerDate.setOnClickListener(v -> openCalendar());

        // Save button click listener
        saveBtn.setOnClickListener(v -> {
            if (areFieldsValid()) {
                saveTenantData();
            }
        });

        // Cancel button click listener
        cancelBtn.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void saveTenantData() {
        String name = fullName.getText().toString().trim();
        String aptType = apartmentType.getText().toString().trim();
        String contactRaw = contactNumber.getText().toString();
        String aptNumberRaw = apartmentNumber.getText().toString();

        // Remove all non-digit characters
        String contact = contactRaw.replaceAll("[^\\d]", "");
        String aptNumber = aptNumberRaw.replaceAll("[^\\d]", "");

        String regDate = registerDate.getText().toString().trim();

        String contactNum;
        int apartmentNum;
        try {
            contactNum = contact;
            apartmentNum = Integer.parseInt(aptNumber);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Contact or apartment number must be numeric", Toast.LENGTH_SHORT).show();
            return;
        }

        TenantLists tenant = new TenantLists(name, aptType, apartmentNum, contactNum, regDate);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tenants");
        String key = ref.push().getKey();

        if (key != null) {
            ref.child(key).setValue(tenant)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Tenant saved successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }


    private void openCalendar() {
        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                TenantForm.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the selected date
                    String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%d",
                            selectedMonth + 1, selectedDay, selectedYear);
                    registerDate.setText(formattedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private boolean areFieldsValid() {
        if (fullName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Full name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (contactNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Contact number is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (apartmentType.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Apartment Type is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (apartmentNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Apartment Number is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (registerDate.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Register date is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void setRegisterDate(String date) {
        registerDate.setText(date);
    }
}
