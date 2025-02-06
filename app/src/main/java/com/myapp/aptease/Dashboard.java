package com.myapp.aptease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Load the default fragment (BookingFragment) when the activity starts
        if (savedInstanceState == null) {
            Log.d("FRAGMENT_TRANSACTION", "Initial fragment loading: BookingFragment");
            loadFragment(new MainDashboard());
        }

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set an item selected listener for the navigation items
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                String email = getIntent().getStringExtra("email");
                String role = getIntent().getStringExtra("role");

                switch (item.getItemId()) {
                    case R.id.dashboard:
                        Log.d("NAVIGATION", "Booking item selected");
                        fragment = new MainDashboard();
                        break;
                    case R.id.apartment:
                        Log.d("NAVIGATION", "Profile item selected");
                        fragment = new Apartment();
                        break;
                    case R.id.tenant:
                        Log.d("NAVIGATION", "Notification item selected");
                        fragment = new Tenant();
                        break;
                    case R.id.payment:
                        Log.d("NAVIGATION", "Booking item selected");
                        fragment = new Payment();
                        break;
                    default:
                        Log.d("NAVIGATION", "Unknown item selected");
                        return false;
                }

                // Ensure the fragment is not null before loading it
                if (fragment != null) {
                    Log.d("FRAGMENT_TRANSACTION", "Loading fragment: " + fragment.getClass().getSimpleName());
                    loadFragment(fragment);
                    return true;
                } else {
                    Log.d("FRAGMENT_TRANSACTION", "Fragment is null for some reason");
                }

                return false;
            }
        });
    }

    // Method to load fragments dynamically
    private void loadFragment(Fragment fragment) {
        Log.d("FRAGMENT_TRANSACTION", "Replacing fragment with: " + fragment.getClass().getSimpleName());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }
}