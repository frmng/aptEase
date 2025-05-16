package com.myapp.aptease.TenantMenu;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.myapp.aptease.PaymentMenu.PaymentLists;
import com.myapp.aptease.PaymentMenu.TenantPaymentHistory;
import com.myapp.aptease.R;

import java.util.List;

public class TenantAdapter extends RecyclerView.Adapter<TenantAdapter.TenantViewHolder> {

    private Context context;
    private List<TenantLists> tenantLists;

    public TenantAdapter(Context context, List<TenantLists> tenantLists) {
        this.context = context;
        this.tenantLists = tenantLists;
    }

    @NonNull
    @Override
    public TenantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tenant_card, parent, false);
        return new TenantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TenantViewHolder holder, int position) {
        TenantLists tenant = tenantLists.get(position);

        holder.tenantName.setText(tenant.getTenantName());
        holder.contactNumber.setText(tenant.getContactNum());
        holder.apartmentNumber.setText("Apartment Number: " + tenant.getApartmentNumber());
        holder.apartmentType.setText("Apartment Type: " + tenant.getApartmentType());

        holder.editButton.setOnClickListener(v -> {
            TenantLists tenants = tenantLists.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Edit Tenant");

            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_tenant, null);
            builder.setView(dialogView);

            EditText nameEdit = dialogView.findViewById(R.id.editTenantName);
            EditText contactEdit = dialogView.findViewById(R.id.editContactNum);
            EditText apartmentNumEdit = dialogView.findViewById(R.id.editApartmentNumber);
            EditText apartmentTypeEdit = dialogView.findViewById(R.id.editApartmentType);

            if (nameEdit == null || contactEdit == null || apartmentNumEdit == null || apartmentTypeEdit == null) {
                Toast.makeText(context, "Error: Invalid layout or missing fields", Toast.LENGTH_SHORT).show();
                return;
            }

            nameEdit.setText(tenants.getTenantName());
            contactEdit.setText(tenants.getContactNum());
            apartmentNumEdit.setText(String.valueOf(tenants.getApartmentNumber()));
            apartmentTypeEdit.setText(tenants.getApartmentType());

            builder.setPositiveButton("Update", (dialog, which) -> {
                String updatedName = nameEdit.getText().toString().trim();
                String updatedContact = contactEdit.getText().toString().trim();
                String updatedApartmentNum = apartmentNumEdit.getText().toString().trim();
                String updatedApartmentType = apartmentTypeEdit.getText().toString().trim();

                int apartmentNumInt;
                try {
                    apartmentNumInt = Integer.parseInt(updatedApartmentNum);
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Invalid apartment number", Toast.LENGTH_SHORT).show();
                    return;
                }

                tenants.setTenantName(updatedName);
                tenants.setContactNum(updatedContact);
                tenants.setApartmentNumber(apartmentNumInt);
                tenants.setApartmentType(updatedApartmentType);

                if (tenants.getKey() != null) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tenants").child(tenants.getKey());
                    ref.setValue(tenants).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            tenantLists.set(position, tenants);
                            notifyItemChanged(position);
                            Toast.makeText(context, "Tenant updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Update failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.create().show();
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (tenant.getKey() != null) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete this tenant?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tenants").child(tenant.getKey());
                            ref.removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    tenantLists.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, tenantLists.size());
                                    Toast.makeText(context, "Tenant deleted successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Failed to delete tenant: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
                Toast.makeText(context, "Error: Tenant key is null", Toast.LENGTH_SHORT).show();
            }
        });

        holder.takeFeeButton.setOnClickListener(v -> {
            TenantLists tenantFee = tenantLists.get(position);
            String apartmentName = tenantFee.getApartmentType().trim(); // trim here once

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Enter Payment Amount");

            final EditText input = new EditText(context);
            input.setHint("Enter monthly fee for " + apartmentName);
            input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            builder.setPositiveButton("Submit", null);
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.setOnShowListener(d -> {
                Button submitButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                submitButton.setOnClickListener(view -> {
                    String enteredAmount = input.getText().toString().trim();
                    if (enteredAmount.isEmpty()) {
                        Toast.makeText(context, "Please enter an amount", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Use the correct reference path here
                    DatabaseReference paymentRef = FirebaseDatabase.getInstance().getReference("apartments");
                    paymentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("PAYMENT_DEBUG", "Number of children: " + snapshot.getChildrenCount());
                            boolean foundMatch = false;

                            for (DataSnapshot snap : snapshot.getChildren()) {
                                String dbApartmentType = snap.child("apartmentType").getValue(String.class);
                                String monthlyPriceStr = snap.child("monthlyPrice").getValue(String.class);

                                Log.d("PAYMENT_DEBUG", "Checking apartmentType: '" + dbApartmentType + "' against: '" + apartmentName + "'");

                                if (dbApartmentType != null && monthlyPriceStr != null &&
                                        dbApartmentType.trim().equalsIgnoreCase(apartmentName)) {

                                    foundMatch = true;

                                    try {
                                        int monthlyPrice = Integer.parseInt(monthlyPriceStr);
                                        int paidAmount = Integer.parseInt(enteredAmount);

                                        if (paidAmount != monthlyPrice) {
                                            Toast.makeText(context, "Amount must be exactly ₱" + monthlyPrice, Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        // Valid payment
                                        String paymentStatus = "Paid";
                                        String currentDate = java.text.DateFormat.getDateInstance().format(new java.util.Date());

                                        PaymentLists payment = new PaymentLists(
                                                tenantFee.getTenantName(),
                                                currentDate,
                                                String.valueOf(monthlyPrice),
                                                paymentStatus
                                        );

                                        DatabaseReference recordRef = FirebaseDatabase.getInstance().getReference("tenantPayments");
                                        recordRef.push().setValue(payment).addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                // ✅ Save to tenantPaymentHistory
                                                String tenantName = payment.getTenantPayment();
                                                String datePaid = payment.getRegisteredDate();
                                                String monthKey = getMonthYear(datePaid);

                                                TenantPaymentHistory history = new TenantPaymentHistory(
                                                        payment.getMonthlyFee(),
                                                        payment.getStatus(),
                                                        datePaid
                                                );

                                                DatabaseReference historyRef = FirebaseDatabase.getInstance()
                                                        .getReference("tenantPaymentHistory")
                                                        .child(tenantName)
                                                        .child(monthKey);

                                                historyRef.setValue(history).addOnCompleteListener(historyTask -> {
                                                    if (historyTask.isSuccessful()) {
                                                        Toast.makeText(context, "Payment recorded successfully", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                    } else {
                                                        Toast.makeText(context, "Payment saved but failed to record history", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(context, "Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    } catch (NumberFormatException e) {
                                        Toast.makeText(context, "Invalid number entered", Toast.LENGTH_SHORT).show();
                                    }

                                    break; // stop looping after match
                                }
                            }

                            if (!foundMatch) {
                                Toast.makeText(context, "No payment data found for this apartment", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "DB Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            });

            dialog.show();
        });


    }

    private String getMonthYear(String dateStr) {
        String[] parts = dateStr.split(" ");
        return parts.length >= 3 ? parts[1] + " " + parts[2] : "Unknown";
    }


    @Override
    public int getItemCount() {
        return tenantLists.size();
    }

    public static class TenantViewHolder extends RecyclerView.ViewHolder {

        TextView tenantName, contactNumber, apartmentNumber, apartmentType, takeFeeButton;
        ImageView editButton, deleteButton;

        public TenantViewHolder(@NonNull View itemView) {
            super(itemView);
            tenantName = itemView.findViewById(R.id.tenantName);
            contactNumber = itemView.findViewById(R.id.contactNumber);
            apartmentNumber = itemView.findViewById(R.id.apartmentNumber);
            apartmentType = itemView.findViewById(R.id.apartmentType);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            takeFeeButton = itemView.findViewById(R.id.takeFeeButton);
        }
    }
}
