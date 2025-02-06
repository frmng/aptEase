package com.myapp.aptease;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Apartment extends Fragment {

    private static final int APARTMENT_FORM_REQUEST = 1;
    private static final int TENANT_FORM_REQUEST = 2;  // Request code for Tenant Form

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apartment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView addApartment = view.findViewById(R.id.addapartment);
        TextView addTenant1 = view.findViewById(R.id.addtenant1);
        TextView addTenant2 = view.findViewById(R.id.addtenant2);
        TextView addTenant3 = view.findViewById(R.id.addtenant3);
        TextView addTenant4 = view.findViewById(R.id.addtenant4);
        TextView addTenant5 = view.findViewById(R.id.addtenant5);

        // Existing addApartment functionality
        addApartment.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ApartmentForm.class);
            startActivityForResult(intent, APARTMENT_FORM_REQUEST);
        });

        // Add click listeners for tenants
        addTenant1.setOnClickListener(v -> openTenantForm());
        addTenant2.setOnClickListener(v -> openTenantForm());
        addTenant3.setOnClickListener(v -> openTenantForm());
        addTenant4.setOnClickListener(v -> openTenantForm());
        addTenant5.setOnClickListener(v -> openTenantForm());
    }

    private void openTenantForm() {
        Intent intent = new Intent(getActivity(), TenantForm.class);
        startActivityForResult(intent, TENANT_FORM_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == APARTMENT_FORM_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            String message = data.getStringExtra("message");
            if (message != null) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }

        // Handle result for Tenant Form (if needed)
        if (requestCode == TENANT_FORM_REQUEST) {
            if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                String message = data.getStringExtra("message");
                if (message != null) {
                    // Show success message
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                // Handle cancel result if needed
                Toast.makeText(getActivity(), "Tenant addition canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
