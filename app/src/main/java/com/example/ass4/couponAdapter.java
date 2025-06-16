package com.example.ass4;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class couponAdapter extends RecyclerView.Adapter<couponAdapter.MyViewHolder> {

    Context context;
    foodSQLHelper db;

    ArrayList<String> cCode,cDisAmo;
    ArrayList<Double> disAmo;
    ArrayList<String> disType;

    int cartId, userId;
    String username,course_id;
    Double bill,disAmount;

    public couponAdapter(Context context, foodSQLHelper db, ArrayList<String> cCode, ArrayList<String> cDisAmo, ArrayList<Double> disAmo, ArrayList<String> disType, Double bill, int userId, String username, int cartId) {
        this.context = context;
        this.db = db;
        this.cCode = cCode;
        this.cDisAmo = cDisAmo;
        this.disAmo = disAmo;
        this.disType = disType;
        this.bill = bill;
        this.userId = userId;
        this.username = username;
        this.cartId = cartId;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

       // db.execSQL(" CREATE TABLE Coupons ( coupon_id INTEGER PRIMARY KEY AUTOINCREMENT,code TEXT UNIQUE NOT NULL, discount_amount REAL NOT NULL, discount_type TEXT CHECK(discount_type IN ('Percentage', 'Fixed')) NOT NULL,min_purchase_amount REAL DEFAULT 0, valid_from TEXT, valid_to TEXT, usage_limit INTEGER DEFAULT 1, usage_count INTEGER DEFAULT 0); ")
        TextView cCode,cDisAmo;
        Double disAmo;
        String disType;
        Button btnApl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cCode = itemView.findViewById(R.id.txt_coCode);
            cDisAmo = itemView.findViewById(R.id.txt_coPrice);
            btnApl = itemView.findViewById(R.id.btn_apply);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_coupon_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        double dis = disAmo.get(position);
        String type = disType.get(position);
        course_id = cCode.get(position);
        if (Objects.equals(type, "Fixed")) {
            disAmount = dis;
        }
        else {
            disAmount = bill * (dis/100);
        }

        holder.cCode.setText(cCode.get(position));
       // holder.cDisAmo.setText(disAmount.get(position));
        holder.cDisAmo.setText(String.format("%.2f", disAmount));

        // Apply Coupon
        holder.btnApl.setOnClickListener(v -> {
            Intent resultIntent = new Intent(context, cus_my_cart.class);
            resultIntent.putExtra("couponCode", course_id);
            resultIntent.putExtra("couponAmount", disAmount);
            resultIntent.putExtra("username", username);
            resultIntent.putExtra("userId", userId);
            resultIntent.putExtra("cartId", cartId);
            context.startActivity(resultIntent);
        });
    }

    @Override
    public int getItemCount() {
        return cCode.size();
    }
}

