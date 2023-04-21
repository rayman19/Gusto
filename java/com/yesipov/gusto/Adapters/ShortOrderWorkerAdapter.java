package com.yesipov.gusto.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yesipov.gusto.Models.Order;
import com.yesipov.gusto.Models.OrderItem;
import com.yesipov.gusto.Models.User;
import com.yesipov.gusto.R;

import java.util.List;

public class ShortOrderWorkerAdapter extends RecyclerView.Adapter<ShortOrderWorkerAdapter.ViewHolder> {
    public interface OnOrderClickListener{
        void onOrderClick(Order order, int position);
    }

    private final OnOrderClickListener onClickListener;
    private final LayoutInflater inflater;
    private final List<Order> orders;
    DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("users");

    public ShortOrderWorkerAdapter(Context context, List<Order> orders, OnOrderClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ShortOrderWorkerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fullorder_worker_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShortOrderWorkerAdapter.ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.titleView.setText(order.getUsername() + "\n" + order.getNumber());
        holder.statusView.setText(order.getStatus());

        if (order.getStatus().toString().equals("Готовится")) {
            holder.layout.setBackgroundResource(R.drawable.status_shape_red);
        }
        else if (order.getStatus().toString().equals("Готов")) {
            holder.layout.setBackgroundResource(R.drawable.status_shape_orange);
        }
        else if (order.getStatus().toString().equals("Выдан")) {
            holder.layout.setBackgroundResource(R.drawable.status_shape_green);
        }

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", order.getStatus());
                if (order.getStatus().toString().equals("Готовится")) {
                    order.setStatus("Готов");
                    holder.layout.setBackgroundResource(R.drawable.status_shape_orange);
                }
                else if (order.getStatus().toString().equals("Готов")) {
                    order.setStatus("Выдан");
                    holder.layout.setBackgroundResource(R.drawable.status_shape_green);
                }
                else if (order.getStatus().toString().equals("Выдан")) {
                    orders.remove(position);
                    notifyDataSetChanged();
                }

                refUser.child(order.getUsername()).child("orders").child("commit").child(order.getNumber()).child("status")
                        .setValue(order.getStatus()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onOrderClick(order, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView titleView, statusView;
        final Button acceptButton;
        final LinearLayout layout;
        ViewHolder(View view){
            super(view);
            titleView = (TextView) view.findViewById(R.id.textViewNumWorker);
            statusView = (TextView) view.findViewById(R.id.textViewStatusWorker);
            layout = (LinearLayout) view.findViewById(R.id.fullorder_item_color_worker);
            acceptButton = (Button) view.findViewById(R.id.buttonAcceptWorker);
        }
    }
}