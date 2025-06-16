package com.example.ass4;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> fName2, rName2, fPrice2, fTot2, fSize, fTop;
    ArrayList<Integer> fQua, fId2, fCid2;
    foodSQLHelper db;

    public cartAdapter(Context context, ArrayList<String> fName2, ArrayList<String> rName2, ArrayList<String> fPrice2, ArrayList<String> fTot2, ArrayList<String> fSize, ArrayList<String> fTop, ArrayList<Integer> fQua, ArrayList<Integer> fId2, ArrayList<Integer> fCid2, foodSQLHelper db) {
        this.context = context;
        this.fName2 = fName2;
        this.rName2 = rName2;
        this.fPrice2 = fPrice2;
        this.fTot2 = fTot2;
        this.fSize = fSize;
        this.fTop = fTop;
        this.fQua = fQua;
        this.fId2 = fId2;
        this.fCid2 = fCid2;
        this.db = db;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_cart_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String priceString = fPrice2.get(position);
        int quantity = fQua.get(position);
        int foodId2 = fId2.get(position);
        int cartId2 = fCid2.get(position);

        // Set the text for name, price, and quantity
        holder.fName2.setText(fName2.get(position));
        holder.rName2.setText(rName2.get(position));
        holder.fPrice2.setText(priceString);
        holder.fSize.setText(fSize.get(position));
        holder.fTop.setText(fTop.get(position));
        holder.fQua.setText(String.valueOf(quantity));
        holder.fId2.setText(String.valueOf(foodId2));
        holder.fCid2.setText(String.valueOf(cartId2));

        // Calculate and set total price
        double price = Double.parseDouble(priceString);
        double totalPrice = price * quantity;
        holder.fTot2.setText(String.format("%.2f", totalPrice));

        // Decrease quantity
        holder.btnDec.setOnClickListener(v -> {
            int currentQuantity = fQua.get(position); // Get the current quantity
            if (currentQuantity > 1) { // Ensure quantity doesn't go below 1
                int newQuantity = currentQuantity - 1; // Calculate the new quantity

                fQua.set(position, newQuantity); // Update the quantity in the ArrayList
                notifyItemChanged(position); // Notify the adapter to refresh the item view
                db.updateCartItemQuantity(cartId2, foodId2, newQuantity); // Update the quantity in the database

                // Call refreshCart() method in the activity
                //((Activity) context).recreate();
                ((cus_my_cart) context).refreshCart();
            }
        });

        // Increase quantity
        holder.btnInc.setOnClickListener(v -> {
            int currentQuantity = fQua.get(position);
            if (currentQuantity > 0) { // Ensure quantity doesn't go below 1
                int newQuantity = currentQuantity + 1; // Calculate the new quantity

                fQua.set(position, newQuantity); // Update the quantity in the ArrayList
                notifyItemChanged(position); // Notify the adapter to refresh the item view
                db.updateCartItemQuantity(cartId2, foodId2, newQuantity); // Update the quantity in the database

                // Call refreshCart() method in the activity
                //((Activity) context).recreate();
                ((cus_my_cart) context).refreshCart();
            }
        });

        // Delete Cart Item
        holder.btnDel.setOnClickListener(v -> {
            //int currentProId = fId2.get(position);
            notifyItemChanged(position); // Notify the adapter to refresh the item view
            db.deleteCartItem(cartId2, foodId2); // Delete food from the cart in the database

            // Call refreshCart() method in the activity
            //((Activity) context).recreate();
            ((cus_my_cart) context).refreshCart();

        });
    }

    @Override
    public int getItemCount() {
        return fName2.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fName2, rName2, fPrice2, fQua, fTot2, fCid2, fId2, fSize, fTop;
        TextView btnDec, btnInc;
        ImageView btnDel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fName2 = itemView.findViewById(R.id.lbl_foName2);
            rName2 = itemView.findViewById(R.id.lbl_reName2);
            fPrice2 = itemView.findViewById(R.id.lbl_foPrice2);
            fTot2 = itemView.findViewById(R.id.lbl_foToPrice2);
            fSize = itemView.findViewById(R.id.lbl_foSize2);
            fTop = itemView.findViewById(R.id.lbl_foTop2);
            fQua = itemView.findViewById(R.id.lbl_qua);
            btnDec = itemView.findViewById(R.id.btn_dec2);
            btnInc = itemView.findViewById(R.id.btn_inc2);
            btnDel = itemView.findViewById(R.id.btn_delCartItem);

            fId2 = itemView.findViewById(R.id.lbl_foodId2);
            fCid2 = itemView.findViewById(R.id.lbl_cartId2);
        }
    }
}
