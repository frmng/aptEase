package com.myapp.aptease.ApartmentMenu;

import static android.app.ProgressDialog.show;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.aptease.R;

import java.util.ArrayList;
import java.util.List;

public class Apartment extends Fragment {

    private List<AparmentCardLists> apartmentList = new ArrayList<>();
    private ApartmentAdapter apartmentAdapter;
    private RecyclerView apartmentRecyclerView;
    private DatabaseReference database;

    private ImageView apartmentImg, searchButton;
    private EditText searchBar;


    // Declare the ActivityResultLauncher for Apartment Form
    private final ActivityResultLauncher<Intent> apartmentFormLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                    String apartmentType = result.getData().getStringExtra("apartmentType");
                    String bedroomNumber = result.getData().getStringExtra("bedroomNumber");
                    String restroomNumber = result.getData().getStringExtra("restroomNumber");
                    String kitchenNumber = result.getData().getStringExtra("kitchenNumber");
                    String livingRoomNumber = result.getData().getStringExtra("livingRoomNumber");
                    String monthlyPrice = result.getData().getStringExtra("monthlyPrice");
//                    String base64Image = result.getData().getStringExtra("base64Image");

                    if (monthlyPrice == null || monthlyPrice.isEmpty()) {
                        return;
                    }

                    try {
                        int monthlyPayment = Integer.parseInt(monthlyPrice);

                        AparmentCardLists apartment = new AparmentCardLists(
                                apartmentType, String.valueOf(monthlyPayment), bedroomNumber, restroomNumber,
                                kitchenNumber, livingRoomNumber
                        );

//                        saveApartmentToDatabase(apartment, base64Image);

                    } catch (NumberFormatException e) {
                        Toast.makeText(getActivity(), "Invalid monthly price format", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_apartment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apartmentImg = view.findViewById(R.id.apartmentImage);
        searchButton = view.findViewById(R.id.searchbtn);
        searchBar = view.findViewById(R.id.searchBar);

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("apartments");

        apartmentRecyclerView = view.findViewById(R.id.recyclerView);
        apartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        apartmentAdapter = new ApartmentAdapter(apartmentList);
        apartmentRecyclerView.setAdapter(apartmentAdapter);

        searchButton.setOnClickListener(v -> {
            String query = searchBar.getText().toString().trim();
            if (!query.isEmpty()) {
                searchApartmentByType(query);
            } else {
                // Reload all if search is empty
                loadApartmentsFromDatabase();
            }
        });


        // Load apartments from Firebase when the fragment is created
        loadApartmentsFromDatabase();

        TextView addApartment = view.findViewById(R.id.addapartment);

        // Handle add apartment click
        if (addApartment != null) {
            addApartment.setOnClickListener(v -> openApartmentForm());
        }
    }

    //search apartment method
    private void searchApartmentByType(String query) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                apartmentList.clear();

                for(DataSnapshot apartmentSnapshot : snapshot.getChildren()) {
                    String apartmentType = apartmentSnapshot.child("apartmentType").getValue(String.class);

                    if(apartmentType != null && apartmentType.toLowerCase().contains(query.toLowerCase())) {
                        String bedroomNumber = apartmentSnapshot.child("bedroomNumber").getValue(String.class);
                        String restroomNumber = apartmentSnapshot.child("restroomNumber").getValue(String.class);
                        String kitchenNumber = apartmentSnapshot.child("kitchenNumber").getValue(String.class);
                        String livingRoomNumber = apartmentSnapshot.child("livingRoomNumber").getValue(String.class);

                        Object monthlyPriceObj = apartmentSnapshot.child("monthlyPrice").getValue();
                        int monthlyPayment = 0;

                        if(monthlyPriceObj instanceof Long) {
                            monthlyPayment = ((Long) monthlyPriceObj).intValue();
                        } else if(monthlyPriceObj instanceof String) {
                            try {
                                monthlyPayment = Integer.parseInt((String) monthlyPriceObj);
                            } catch(NumberFormatException e) {
                                continue;
                            }
                        }

                        AparmentCardLists aparment = new AparmentCardLists(
                                apartmentType, String.valueOf(monthlyPayment), bedroomNumber, restroomNumber, kitchenNumber, livingRoomNumber
                        );

                        apartmentList.add(aparment);

                    }
                }

                apartmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Search failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openApartmentForm() {
        Intent intent = new Intent(getActivity(), ApartmentForm.class);
        apartmentFormLauncher.launch(intent);
    }

    private void loadApartmentsFromDatabase() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                apartmentList.clear();

                for (DataSnapshot apartmentSnapshot : dataSnapshot.getChildren()) {
                    String apartmentType = apartmentSnapshot.child("apartmentType").getValue(String.class);
                    String bedroomNumber = apartmentSnapshot.child("bedroomNumber").getValue(String.class);
                    String restroomNumber = apartmentSnapshot.child("restroomNumber").getValue(String.class);
                    String kitchenNumber = apartmentSnapshot.child("kitchenNumber").getValue(String.class);
                    String livingRoomNumber = apartmentSnapshot.child("livingRoomNumber").getValue(String.class);

                    Object monthlyPriceObj = apartmentSnapshot.child("monthlyPrice").getValue();
                    int monthlyPayment = 0;

                    if (monthlyPriceObj instanceof Long) {
                        monthlyPayment = ((Long) monthlyPriceObj).intValue();
                    } else if (monthlyPriceObj instanceof Integer) {
                        monthlyPayment = (Integer) monthlyPriceObj;
                    } else if (monthlyPriceObj instanceof String) {
                        try {
                            monthlyPayment = Integer.parseInt((String) monthlyPriceObj);
                        } catch (NumberFormatException e) {
                            Log.e("Apartment", "Invalid monthlyPrice format (String)", e);
                            continue;
                        }
                    } else if (monthlyPriceObj instanceof Double) {
                        monthlyPayment = ((Double) monthlyPriceObj).intValue();
                    } else {
                        Log.e("Apartment", "Unknown type for monthlyPrice: " + monthlyPriceObj);
                        continue;
                    }

                    AparmentCardLists apartment = new AparmentCardLists(
                            apartmentType, String.valueOf(monthlyPayment), bedroomNumber, restroomNumber,
                            kitchenNumber, livingRoomNumber
                    );

                    apartmentList.add(apartment);
                }

                apartmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to load apartments.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveApartmentToDatabase(AparmentCardLists apartment, String base64Image) {
        DatabaseReference apartmentsRef = database.child("apartments");

        apartmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isDuplicate = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AparmentCardLists existingApartment = snapshot.getValue(AparmentCardLists.class);

                    if (existingApartment != null &&
                            existingApartment.getApartmentType().equals(apartment.getApartmentType()) &&
                            existingApartment.getBedroomNumber().equals(apartment.getBedroomNumber()) &&
                            existingApartment.getRestroomNumber().equals(apartment.getRestroomNumber()) &&
                            existingApartment.getKitchenNumber().equals(apartment.getKitchenNumber()) &&
                            existingApartment.getLivingRoomNumber().equals(apartment.getLivingRoomNumber()) &&
                            existingApartment.getMonthlyPrice().equals(apartment.getMonthlyPrice())) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (isDuplicate) {
                    Toast.makeText(requireContext(), "This apartment already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    // ✅ Attach base64 image if available
//                    if (base64Image != null) {
//                        apartment.setImageBase64(base64Image);
//                    }

                    apartmentsRef.push().setValue(apartment)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(requireContext(), "Apartment added successfully!", Toast.LENGTH_SHORT).show();
                                    requireActivity().finish(); // Optionally close the activity
                                } else {
                                    Toast.makeText(requireContext(), "Failed to save apartment.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Error checking for duplicate apartment.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper to safely compare possibly-null strings
    private boolean safeEquals(String a, String b) {
        return a != null && b != null && a.trim().equalsIgnoreCase(b.trim());
    }

}
