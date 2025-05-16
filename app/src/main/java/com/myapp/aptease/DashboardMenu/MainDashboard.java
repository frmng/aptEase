package com.myapp.aptease.DashboardMenu;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.aptease.R;

public class MainDashboard extends Fragment {

    TextView registeredUser, apartmentCountTextView, tenantCount, paymentCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_dashboard, container, false);

        // Initialize TextViews
        registeredUser = root.findViewById(R.id.registeredUsers);
        apartmentCountTextView = root.findViewById(R.id.apartments);
        tenantCount = root.findViewById(R.id.tenants);
        paymentCount = root.findViewById(R.id.paymentCount);


        // Firebase instance to fetch data
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Reference for user count
        DatabaseReference userCountRef = database.getReference("userCount");
        // Reference for apartment count
        DatabaseReference apartmentRef = database.getReference("apartments");

        // Get registered user count
        userCountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer count = dataSnapshot.getValue(Integer.class);
                if (count != null) {
                    registeredUser.setText("Registered Users: " + count);
                } else {
                    // Show a Toast error message when data is null
                    Toast.makeText(getContext(), "Failed to retrieve user data.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error here with a Toast
                Toast.makeText(getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Get apartment count
        apartmentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int validApartmentCount = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String type = snapshot.child("apartmentType").getValue(String.class);
                    if (type != null && !type.trim().isEmpty()) {
                        validApartmentCount++;
                    }
                }
                apartmentCountTextView.setText("Apartments: " + validApartmentCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Get tenant count
        DatabaseReference tenantRef = database.getReference("tenants");
        tenantRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int tenantTotal = (int) dataSnapshot.getChildrenCount();
                tenantCount.setText("Tenants: " + tenantTotal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error loading tenants: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        loadPaymentCount(database);

        return root;
    }

    private void loadPaymentCount(FirebaseDatabase database) {
        DatabaseReference paymentRef = database.getReference("tenantPayments");
        paymentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Count valid payments (non-null keys)
                int count = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.exists()) {
                        count++;
                    }
                }
                paymentCount.setText("Payments: " + count);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(), "Error loading payments: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
