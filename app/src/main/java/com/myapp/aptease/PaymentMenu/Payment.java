package com.myapp.aptease.PaymentMenu;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class Payment extends Fragment {

    private RecyclerView recyclerView;
    private PaymentAdapter paymentAdapter;
    private List<PaymentLists> paymentList;
    private DatabaseReference paymentRef;

    private EditText searchPayment;
    private ImageView searchPaymentButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerViewPayment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchPayment = view.findViewById(R.id.searchHistory);
        searchPaymentButton = view.findViewById(R.id.searchHistoryButton);

        searchPaymentButton.setOnClickListener(v -> {
            String tenantName = searchPayment.getText().toString().trim();
            if (tenantName.isEmpty()) {
                searchPayment.setError("Enter tenant name");
                return;
            }
            searchPaymentsByTenant(tenantName);
        });


        paymentList = new ArrayList<>();
        paymentAdapter = new PaymentAdapter(paymentList, new PaymentAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Delete Payment")
                        .setMessage("Are you sure you want to delete this payment?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            PaymentLists payment = paymentList.get(position);
                            String key = payment.getKey();
                            if (key != null) {
                                paymentRef.child(key).removeValue();
                                paymentList.remove(position);
                                paymentAdapter.notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }

            @Override
            public void onViewHistoryClick(int position) {
                PaymentLists selected = paymentList.get(position);
                String tenantName = selected.getTenantPayment();

                DatabaseReference historyRef = FirebaseDatabase.getInstance()
                        .getReference("tenantPaymentHistory").child(tenantName);

                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.history_dialog, null);
                LinearLayout container = dialogView.findViewById(R.id.historyContainer);

                AlertDialog dialog = new AlertDialog.Builder(requireContext())
                        .setTitle("History for " + tenantName)
                        .setView(dialogView)
                        .setPositiveButton("Close", null)
                        .create();

                historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            TextView none = new TextView(getContext());
                            none.setText("No history available.");
                            container.addView(none);
                            return;
                        }

                        for (DataSnapshot monthSnapshot : snapshot.getChildren()) {
                            String month = monthSnapshot.getKey();
                            String fee = monthSnapshot.child("monthlyFee").getValue(String.class);
                            String status = monthSnapshot.child("status").getValue(String.class);
                            String datePaid = monthSnapshot.child("datePaid").getValue(String.class);

                            TextView item = new TextView(getContext());
                            item.setText(month + " - " + fee + " - " + status + " - " + datePaid);
                            item.setPadding(0, 10, 0, 10);
                            container.addView(item);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // handle error
                    }
                });

                dialog.show();
            }

        });

        recyclerView.setAdapter(paymentAdapter);

        // Firebase reference
        paymentRef = FirebaseDatabase.getInstance().getReference("tenantPayments");

        loadPaymentsFromFirebase();
    }

    private void searchPaymentsByTenant(String tenantName) {
        paymentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                paymentList.clear();
                for (DataSnapshot paymentSnapshot : snapshot.getChildren()) {
                    PaymentLists payment = paymentSnapshot.getValue(PaymentLists.class);
                    if (payment != null && payment.getTenantPayment() != null) {
                        if (payment.getTenantPayment().toLowerCase().contains(tenantName.toLowerCase())) {
                            payment.setKey(paymentSnapshot.getKey());
                            paymentList.add(payment);
                        }
                    }
                }
                if (paymentList.isEmpty()) {
                    searchPayment.setError("No matching tenants found");
                }
                paymentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void loadPaymentsFromFirebase() {
        paymentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                paymentList.clear();
                for (DataSnapshot paymentSnapshot : snapshot.getChildren()) {
                    PaymentLists payment = paymentSnapshot.getValue(PaymentLists.class);
                    if (payment != null) {
                        payment.setKey(paymentSnapshot.getKey());
                        paymentList.add(payment);
                    }
                }
                paymentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
