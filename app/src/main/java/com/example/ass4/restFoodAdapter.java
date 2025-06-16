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

public class restFoodAdapter extends RecyclerView.Adapter<restFoodAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> fName,fTime,fRate,fPrice;
    ArrayList<Integer> fid;
    int cartId;
    int userId;
    String username;

    public restFoodAdapter(Context context, ArrayList<String> fName, ArrayList<String> fTime, ArrayList<String> fRate, ArrayList<String> fPrice, ArrayList<Integer> fid, int userId, String username, int cartId) {
        this.context = context;
        this.fName = fName;
        this.fTime = fTime;
        this.fRate = fRate;
        this.fPrice = fPrice;
        this.fid = fid;
        this.cartId = cartId;
        this.userId = userId;
        this.username = username;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_rest_food_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // holder.Fimg.setText(items.get(position).getfImg());
        holder.fName.setText(String.valueOf(fName.get(position)));
        holder.fTime.setText(String.valueOf(fTime.get(position)));
        holder.fRate.setText(String.valueOf(fRate.get(position)));
        holder.fPrice.setText(String.valueOf(fPrice.get(position)));
        //holder.fid.setText(String.valueOf(fid.get(position)));
        int foodId = fid.get(position);
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
        return fName.size()  ;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fName,fTime,fRate,fPrice,fid;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Fimg = itemView.findViewById(R.id.img_food);
            fName = itemView.findViewById(R.id.lbl_foNameRest);
            fTime = itemView.findViewById(R.id.lbl_fTimeRest);
            fPrice = itemView.findViewById(R.id.lbl_foPrice2Rest);
            fRate = itemView.findViewById(R.id.lbl_foRateRest);
            fid = itemView.findViewById(R.id.lbl_fRestFId2);
        }
    }
}
