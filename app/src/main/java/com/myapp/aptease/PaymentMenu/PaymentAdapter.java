package com.myapp.aptease.PaymentMenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.aptease.R;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private List<PaymentLists> paymentLists;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onViewHistoryClick(int position);
    }

    public PaymentAdapter(List<PaymentLists> paymentLists, OnItemClickListener listener) {
        this.paymentLists = paymentLists;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_card, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        PaymentLists payment = paymentLists.get(position);
        holder.tenantName.setText("Name: " + payment.getTenantPayment());
        holder.registeredDate.setText("Registered Date: " + payment.getRegisteredDate());
        holder.monthlyFee.setText("Monthly: " + payment.getMonthlyFee());
        holder.status.setText("Status: " + payment.getStatus());

        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(position));
        holder.viewHistoryButton.setOnClickListener(v -> listener.onViewHistoryClick(position));
    }

    @Override
    public int getItemCount() {
        return paymentLists.size();
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tenantName, registeredDate, monthlyFee, status, viewHistoryButton;
        ImageView deleteButton;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            tenantName = itemView.findViewById(R.id.tenantName);
            registeredDate = itemView.findViewById(R.id.registeredDate);
            monthlyFee = itemView.findViewById(R.id.monthlyPrice);
            status = itemView.findViewById(R.id.paymentStatus);
            viewHistoryButton = itemView.findViewById(R.id.viewHistoryButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
