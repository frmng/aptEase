package com.myapp.aptease;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        // Find the views in the layout
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);
        CheckBox showPasswordCheckBox = findViewById(R.id.showpassword);
        Button registerButton = findViewById(R.id.loginbtn);
        TextView loginBtn = findViewById(R.id.loginbutton);

        // Set OnClickListener to navigate to LoginPage when the TextView is clicked
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginPage
                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show password
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // Hide password
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        // Register button click logic
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Validate the inputs
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterPage.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Create user account and store it
                    createUserAccount(email, password);
                }
            }
        });
    }

    private void createUserAccount(String email, String password) {
        // Store the email and password in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();

        Toast.makeText(RegisterPage.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

        // Navigate to LoginPage
        Intent intent = new Intent(RegisterPage.this, LoginPage.class);
        startActivity(intent);
        finish();  // Close the current activity
    }
}
