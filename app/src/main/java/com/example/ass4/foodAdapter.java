package com.example.ass4;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class foodAdapter extends RecyclerView.Adapter<foodAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> fName, rName, fTime, fRate ,fPrice;
    ArrayList<Integer> fId;

    int cartId, userId;
    String username;

    public foodAdapter(Context context, ArrayList<String> fName, ArrayList<String> rName, ArrayList<String> fTime, ArrayList<String> fRate, ArrayList<Integer> fId, ArrayList<String> fPrice, int userId, String username, int cartId) {
        this.context = context;
        this.fName = fName;
        this.rName = rName;
        this.fTime = fTime;
        this.fRate = fRate;
        this.fId = fId;
        this.fPrice = fPrice;
        this.userId = userId;
        this.username = username;
        this.cartId = cartId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_food_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.fName.setText(fName.get(position));
        holder.rName.setText(rName.get(position));
        holder.fTime.setText(fTime.get(position));
        holder.fRate.setText(fRate.get(position));
        holder.fPrice.setText(fPrice.get(position));
       // holder.fId.setText(fId.get(position));

        int quantity = fId.get(position);
        holder.fId.setText(String.valueOf(quantity));
        int foodId = fId.get(position); // Get the current quantity

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, foodDetails.class);
            intent.putExtra("cartId", cartId);
            intent.putExtra("username", username);
            intent.putExtra("userId", userId);
            intent.putExtra("foodId", foodId); // Pass the foodId of the clicked item
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return fName.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fName, rName, fTime, fRate, fId, fPrice;
        TextView btnDec, btnInc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fName = itemView.findViewById(R.id.lbl_foName);
            rName = itemView.findViewById(R.id.lbl_reName);
            fTime = itemView.findViewById(R.id.lbl_foTime);
            fRate = itemView.findViewById(R.id.lbl_foStar);
            fId = itemView.findViewById(R.id.lbl_foId4);
            fPrice = itemView.findViewById(R.id.txt_fd_price);

            btnDec = itemView.findViewById(R.id.btn_dec2);
            btnInc = itemView.findViewById(R.id.btn_inc2);
        }
    }
}
