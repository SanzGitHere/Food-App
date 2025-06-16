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

public class restAdapter extends RecyclerView.Adapter<restAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> rName,rRate,rBra;
    ArrayList<Integer> rId;

    int cartId,userId;
    String username;
    public restAdapter(Context context, ArrayList<String> rName, ArrayList<String> rRate, ArrayList<String> rBra, ArrayList<Integer> rId, int userId, String username, int cartId) {
        this.context = context;
        this.rName = rName;
        this.rRate = rRate;
        this.rBra = rBra;
        this.rId = rId;
        this.cartId = cartId;
        this.userId = userId;
        this.username = username;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_rest_list,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       // holder.Fimg.setText(items.get(position).getfImg());
        holder.rName.setText(String.valueOf(rName.get(position)));
        holder.rRate.setText(String.valueOf(rRate.get(position)));
        holder.rBra.setText(String.valueOf(rBra.get(position)));
        //holder.rId.setText(String.valueOf(rId.get(position)));


        int quantity =  rId.get(position);
        holder.rId.setText(String.valueOf(quantity));
        int restId =  rId.get(position); // Get the current quantity

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RestDetails.class);
            intent.putExtra("cartId", cartId);
            intent.putExtra("username", username);
            intent.putExtra("userId", userId);
            intent.putExtra("restId", restId); // Pass the foodId of the clicked item
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return rName.size()  ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rName,rRate,rBra,rId;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Fimg = itemView.findViewById(R.id.img_food);
            rName = itemView.findViewById(R.id.lbl_reName);
            rRate = itemView.findViewById(R.id.lbl_reStar);
            rBra = itemView.findViewById(R.id.lbl_restbra);
            rId = itemView.findViewById(R.id.lbl_resId3);
        }
    }
}
