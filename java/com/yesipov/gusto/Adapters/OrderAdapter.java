package com.yesipov.gusto.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yesipov.gusto.Fragments.OrderFragment;
import com.yesipov.gusto.Models.OrderItem;
import com.yesipov.gusto.R;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<OrderItem> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<OrderItem> productList;
    DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("users");

    public OrderAdapter(Context context, int resource, ArrayList<OrderItem> products) {
        super(context, resource, products);
        this.productList = products;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        OrderItem product = productList.get(position);

        viewHolder.nameView.setText(product.getFood().getName());
        viewHolder.countView.setText(String.valueOf(product.getCount()));
        product.setFullPrice(product.getFood().getPrice() * product.getCount());
        viewHolder.flagView.setImageResource(product.getFood().getFlagResource());
        viewHolder.priceView.setText(String.valueOf(product.getFullPrice()) + " Р");

        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = product.getCount() - 1;
                if (count < 1) {
                    productList.remove(position);
                    refUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("orders").child("current").child(product.getFood().getName()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().removeValue();
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.w("TAG", "Failed to read value.", error.toException());
                        }
                    });
                    return;
                }
                product.setCount(count);
                product.setFullPrice(count * product.getFood().getPrice());
                viewHolder.countView.setText(String.valueOf(product.getCount()));
                viewHolder.priceView.setText(String.valueOf(product.getFullPrice()) + " Р");
                refUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("orders").child("current").child(product.getFood().getName()).child("count")
                        .setValue(product.getCount()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
                refUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("orders").child("current").child(product.getFood().getName()).child("fullPrice")
                        .setValue(product.getFullPrice()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });
        viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = product.getCount() + 1;
                product.setCount(count);
                product.setFullPrice(count * product.getFood().getPrice());
                viewHolder.countView.setText(String.valueOf(product.getCount()));
                viewHolder.priceView.setText(String.valueOf(product.getFullPrice()) + " Р");
                refUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("orders").child("current").child(product.getFood().getName()).child("count")
                        .setValue(product.getCount()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
                refUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("orders").child("current").child(product.getFood().getName()).child("fullPrice")
                        .setValue(product.getFullPrice()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });
        viewHolder.removeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productList.remove(position);
                refUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("orders").child("current").child(product.getFood().getName()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue();
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("TAG", "Failed to read value.", error.toException());
                    }
                });
            }
        });

        return convertView;
    }
    private class ViewHolder {
        final ImageButton addButton, removeButton, removeItemButton;
        final TextView nameView, countView, priceView;
        final ImageView flagView;

        ViewHolder(View view){
            addButton = (ImageButton) view.findViewById(R.id.button_plus);
            removeButton = (ImageButton) view.findViewById(R.id.button_minus);
            nameView = (TextView) view.findViewById(R.id.title_food_order);
            countView = (TextView) view.findViewById(R.id.count_food);
            priceView = (TextView) view.findViewById(R.id.price_food_order);
            removeItemButton = (ImageButton) view.findViewById(R.id.remove_food);
            flagView = (ImageView) view.findViewById(R.id.image_food_order);
        }
    }
}
