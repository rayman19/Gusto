package com.yesipov.gusto.Orders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yesipov.gusto.Adapters.OrderAdapter;
import com.yesipov.gusto.Adapters.ShortOrderAdapter;
import com.yesipov.gusto.Adapters.SpecialsAdapter;
import com.yesipov.gusto.Models.Food;
import com.yesipov.gusto.Models.Order;
import com.yesipov.gusto.Models.OrderItem;
import com.yesipov.gusto.Models.Specials;
import com.yesipov.gusto.R;
import com.yesipov.gusto.SpecialList;

import java.util.ArrayList;

public class GetOrdersActivity extends AppCompatActivity {
    ArrayList<Order> orders;
    RecyclerView ordersList;
    DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_activity);
        ordersList = findViewById(R.id.orders_list);
        setListOrders();
    }

    public void setListOrders() {

        refUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("orders").child("commit").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orders = new ArrayList<Order>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    orders.add(child.getValue(Order.class));
                }

                ShortOrderAdapter.OnOrderClickListener orderClickListener = new ShortOrderAdapter.OnOrderClickListener() {
                    @Override
                    public void onOrderClick(Order order, int position) {
                        Intent intent = new Intent(GetOrdersActivity.this, GetDescriberOrderActivity.class);
                        intent.putExtra("position", order.getNumber());
                        startActivity(intent);
                    }
                };

                ShortOrderAdapter adapter = new ShortOrderAdapter(GetOrdersActivity.this, orders, orderClickListener);
                ordersList.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
}
