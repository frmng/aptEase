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

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Find the views in the layout
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);
        CheckBox showPasswordCheckBox = findViewById(R.id.showpassword);
        Button loginButton = findViewById(R.id.loginbtn);
        TextView signupBtn = findViewById(R.id.signupbtn);

        // Set OnClickListener to navigate to RegisterPage when the TextView is clicked
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegisterPage
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
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

        // Login button click logic
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Validate the inputs
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginPage.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if the user credentials are correct
                    loginUser(email, password);
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        // Retrieve the stored email and password from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String registeredEmail = sharedPreferences.getString("email", null);
        String registeredPassword = sharedPreferences.getString("password", null);

        // Check if the entered credentials match the registered credentials
        if (email.equals(registeredEmail) && password.equals(registeredPassword)) {
            Toast.makeText(LoginPage.this, "Login successful!", Toast.LENGTH_SHORT).show();

            // Navigate to Dashboard after successful login
            Intent intent = new Intent(LoginPage.this, Dashboard.class);
            startActivity(intent);
            finish();  // Close the current activity
        } else {
            Toast.makeText(LoginPage.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}
