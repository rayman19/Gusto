package com.yesipov.gusto.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.yesipov.gusto.Models.Food;
import com.yesipov.gusto.Models.Order;
import com.yesipov.gusto.Models.OrderItem;
import com.yesipov.gusto.Models.Specials;
import com.yesipov.gusto.R;

import java.util.ArrayList;
import java.util.List;

public class ShortOrderAdapter extends RecyclerView.Adapter<ShortOrderAdapter.ViewHolder> {
    public interface OnOrderClickListener{
        void onOrderClick(Order order, int position);
    }

    private final OnOrderClickListener onClickListener;
    private final LayoutInflater inflater;
    private final List<Order> orders;

    public ShortOrderAdapter(Context context, List<Order> orders, OnOrderClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ShortOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fullorder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShortOrderAdapter.ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.titleView.setText(order.getNumber());
        holder.statusView.setText(order.getStatus());
        holder.costView.setText(String.valueOf(order.getFullprice()) + " Р");
        if (order.getStatus().toString().equals("Готовится")) {
            holder.layout.setBackgroundResource(R.drawable.status_shape_red);
        }
        else if (order.getStatus().toString().equals("Готов")) {
            holder.layout.setBackgroundResource(R.drawable.status_shape_orange);
        }
        else if (order.getStatus().toString().equals("Выдан")) {
            holder.layout.setBackgroundResource(R.drawable.status_shape_green);
        }
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
        final TextView titleView, statusView, costView;
        final LinearLayout layout;
        ViewHolder(View view){
            super(view);
            titleView = (TextView) view.findViewById(R.id.textViewNum);
            statusView = (TextView) view.findViewById(R.id.textViewStatus);
            costView = (TextView) view.findViewById(R.id.textViewPrice);
            layout = (LinearLayout) view.findViewById(R.id.fullorder_item_color);
        }
    }
}