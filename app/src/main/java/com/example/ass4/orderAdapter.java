package com.example.ass4;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class orderAdapter extends RecyclerView.Adapter<orderAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> oStatus, oDate, oPrice;
    ArrayList<String> oId;
    foodSQLHelper dbb;

    int cartId, userId;
    String username;

    public orderAdapter(Context context, ArrayList<String> oStatus, ArrayList<String> oDate, ArrayList<String> oPrice, ArrayList<String> oId, int cartId, int userId, String username) {
        this.context = context;
        this.oStatus = oStatus;
        this.oDate = oDate;
        this.oPrice = oPrice;
        this.oId = oId;
        this.cartId = cartId;
        this.userId = userId;
        this.username = username;
        this.dbb = new foodSQLHelper(context); // Initialize dbb here
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_order_item, parent, false);
        return new MyViewHolder(v);
    }

    public int updateOrderStatus(int orderId, String newStatus) {
        // Prepare the new values
        ContentValues values = new ContentValues();
        values.put("status", newStatus); // Column name and new status value

        // Define the WHERE clause and arguments
        String selection = "order_id = ?";
        String[] selectionArgs = { String.valueOf(orderId) }; // Convert int to String for the selectionArgs

        // Get a writable instance of the database
        SQLiteDatabase db = dbb.getWritableDatabase();

        // Perform the update operation
        int count = db.update(
                "Orders",    // Table name
                values,      // New values
                selection,   // WHERE clause
                selectionArgs // WHERE clause arguments
        );

        return count; // Return the number of rows affected
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.oId.setText(oId.get(position));
        holder.oStatus.setText(oStatus.get(position));
        holder.oDate.setText(oDate.get(position));
        holder.oPrice.setText(oPrice.get(position));

        int orId = Integer.parseInt(oId.get(position));

        // Check if the status is "Delivered" and hide the button if true
        if (oStatus.get(position).equals("Delivered")) {
            holder.btnRec.setVisibility(View.GONE); // Hide the button
        } else {
            holder.btnRec.setVisibility(View.VISIBLE); // Ensure the button is visible for other statuses
        }

        holder.btnRec.setOnClickListener(v -> {
            int rowsUpdated = updateOrderStatus(orId, "Delivered");
            if (rowsUpdated > 0) {
                // Update was successful
                Toast.makeText(context, "Order updated successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, cusMenuAct.class);
                intent.putExtra("cartId", cartId);
                intent.putExtra("username", username);
                intent.putExtra("userId", userId);
                context.startActivity(intent);
            } else {
                // No rows were updated, handle the case where the order ID was not found
                Toast.makeText(context, "Order update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return oStatus.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView oId, oStatus, oDate, oPrice;
        TextView btnRec;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            oId = itemView.findViewById(R.id.lbl_odId);
            oStatus = itemView.findViewById(R.id.lbl_odStatus);
            oDate = itemView.findViewById(R.id.lbl_odDate);
            oPrice = itemView.findViewById(R.id.lbl_odTot);

            btnRec = itemView.findViewById(R.id.btn_rec);
        }
    }
}
