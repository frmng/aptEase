package com.myapp.aptease.Account;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.myapp.aptease.R;

public class RegisterPage extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userCountRef = database.getReference("userCount");
    DatabaseReference usersRef = database.getReference("users"); // New node to store user details

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

        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Set OnClickListener to navigate to LoginPage when the TextView is clicked
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginPage
                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        // Toggle password visibility
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
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Validate the inputs
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterPage.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new user with email and password
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Get the newly registered user
                                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    String userId = firebaseUser.getUid();  // Get the user ID
                                    String userName = firebaseUser.getEmail();  // Use email as username (you can change this)

                                    // Create a new RegisteredUser object
                                    RegisteredUser newUser = new RegisteredUser(userId, userName);

                                    // Store the user details in Firebase under "users" node
                                    usersRef.child(userId).setValue(newUser)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // Increment the user count in Firebase
                                                        incrementUserCount();

                                                        // Show success message and navigate to LoginPage
                                                        Toast.makeText(RegisterPage.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(), LoginPage.class));
                                                        finish();
                                                    } else {
                                                        Toast.makeText(RegisterPage.this, "Failed to save user details", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(RegisterPage.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    // Method to increment the user count in Firebase
    private void incrementUserCount() {
        userCountRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                Integer count = currentData.getValue(Integer.class);
                if (count == null) {
                    count = 0;  // Initialize to 0 if no value exists
                }
                currentData.setValue(count + 1);  // Increment the count by 1
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                if (committed) {
                    // Optionally handle any post-increment logic here
                    Toast.makeText(RegisterPage.this, "User count updated", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle failure to update user count
                    Toast.makeText(RegisterPage.this, "Failed to update user count", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = emailEditText.getText().toString().trim();
//                String password = passwordEditText.getText().toString().trim();
//
//                // Validate the inputs
//                if (email.isEmpty() || password.isEmpty()) {
//                    Toast.makeText(RegisterPage.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Create user account and store it
//                    createUserAccount(email, password);
//                }
//            }
//        });

//    private void createUserAccount(String email, String password) {
//        // Store the email and password in SharedPreferences
//        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("email", email);
//        editor.putString("password", password);
//        editor.apply();
//
//        Toast.makeText(RegisterPage.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
//
//        // Navigate to LoginPage
//        Intent intent = new Intent(RegisterPage.this, LoginPage.class);
//        startActivity(intent);
//        finish();  // Close the current activity
//    }
