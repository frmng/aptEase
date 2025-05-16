package com.myapp.aptease.TenantMenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.myapp.aptease.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class Tenant extends Fragment {

    RecyclerView recyclerView;
    TenantAdapter tenantAdapter;
    List<TenantLists> tenantList = new ArrayList<>();

    EditText searchTenant;
    ImageView searchTenantButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tenant, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTenant);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tenantAdapter = new TenantAdapter(getContext(), tenantList);
        recyclerView.setAdapter(tenantAdapter);

        searchTenant = view.findViewById(R.id.searchTenant);
        searchTenantButton = view.findViewById(R.id.searchTenantButton);

        loadTenantData();

        searchTenantButton.setOnClickListener(v -> {
            String query = searchTenant.getText().toString().trim();
            if (!query.isEmpty()) {
                searchTenantByName(query);
            } else {
                loadTenantData();
            }
        });

        return view;
    }

    private void searchTenantByName(String nameQuery) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tenants");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenantList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TenantLists tenant = dataSnapshot.getValue(TenantLists.class);
                    if (tenant != null && tenant.getTenantName() != null) {
                        if (tenant.getTenantName().toLowerCase().contains(nameQuery.toLowerCase())) {
                            tenant.setKey(dataSnapshot.getKey());
                            tenantList.add(tenant);
                        }
                    }
                }

                if (tenantList.isEmpty()) {
                    Toast.makeText(getContext(), "No tenants found with that name.", Toast.LENGTH_SHORT).show();
                }

                tenantAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Search failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadTenantData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tenants");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenantList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TenantLists tenant = dataSnapshot.getValue(TenantLists.class);
                    if (tenant != null) {
                        tenant.setKey(dataSnapshot.getKey());
                        tenantList.add(tenant);
                    }
                }
                tenantAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
