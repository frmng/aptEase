package com.myapp.aptease.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.myapp.aptease.Account.LoginPage;
import com.myapp.aptease.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // This points to your main XML

        // Use a handler to introduce a delay, e.g., 3 seconds, before transitioning to the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After the delay, start the LoginCategoryActivity
                Intent intent = new Intent(MainActivity.this, LoginPage.class);
                startActivity(intent);
                finish(); // Close the MainActivity
            }
        }, 3000); // 3000 milliseconds = 3 seconds
    }
}
