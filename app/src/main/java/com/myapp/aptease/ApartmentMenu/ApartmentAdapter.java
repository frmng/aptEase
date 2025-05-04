package com.myapp.aptease.ApartmentMenu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myapp.aptease.R;

import java.util.List;

public class ApartmentAdapter extends RecyclerView.Adapter<ApartmentAdapter.ApartmentViewHolder> {

    private List<AparmentCardLists> apartmentList;

    public ApartmentAdapter(List<AparmentCardLists> apartmentList) {
        this.apartmentList = apartmentList;
    }

    @Override
    public ApartmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apartment_card, parent, false);
        return new ApartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ApartmentViewHolder holder, int position) {
        AparmentCardLists apartment = apartmentList.get(position);

        holder.apartmentName.setText(apartment.getApartmentType());
        holder.bedroom.setText(apartment.getBedroomNumber());
        holder.restroom.setText(apartment.getRestroomNumber());
        holder.kitchen.setText(apartment.getKitchenNumber());
        holder.livingRoom.setText(apartment.getLivingRoomNumber());
        holder.monthlyRent.setText("Monthly: " + apartment.getMonthlyPrice());

//        String base64Image = apartment.getImageBase64();
//        if (base64Image != null && !base64Image.isEmpty()) {
//            try {
//                byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
//                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//                if (decodedBitmap != null) {
//                    holder.apartmentImage.setImageBitmap(decodedBitmap);
//                } else {
//                    Log.e("Adapter", "Decoded bitmap is null");
//                    holder.apartmentImage.setImageResource(R.drawable.apartment1);
//                }
//            } catch (Exception e) {
//                Log.e("Adapter", "Image decode error", e);
//                holder.apartmentImage.setImageResource(R.drawable.apartment1);
//            }
//        } else {
//            holder.apartmentImage.setImageResource(R.drawable.apartment1);
//        }
    }

    @Override
    public int getItemCount() {
        return apartmentList.size();
    }

    public static class ApartmentViewHolder extends RecyclerView.ViewHolder {
        TextView apartmentName, bedroom, restroom, kitchen, livingRoom, monthlyRent;
        ImageView apartmentImage;

        public ApartmentViewHolder(View itemView) {
            super(itemView);
            apartmentName = itemView.findViewById(R.id.apartment_name);
            bedroom = itemView.findViewById(R.id.bedroom_number);
            restroom = itemView.findViewById(R.id.restroom_number);
            kitchen = itemView.findViewById(R.id.kitchen);
            livingRoom = itemView.findViewById(R.id.livingroom);
            monthlyRent = itemView.findViewById(R.id.monthly_rent);
//            apartmentImage = itemView.findViewById(R.id.apartmentImage);
        }
    }
}
