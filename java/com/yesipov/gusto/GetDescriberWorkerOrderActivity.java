package com.yesipov.gusto;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yesipov.gusto.Adapters.FullOrderAdapter;
import com.yesipov.gusto.Models.Order;
import com.yesipov.gusto.Models.OrderItem;

import java.util.ArrayList;

public class
GetDescriberWorkerOrderActivity extends AppCompatActivity {
    Order order;
    RecyclerView orderList;
    String number, username;
    DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullorder_activity);
        number = getIntent().getStringExtra("position");
        username = getIntent().getStringExtra("username");
        setListOrders();
    }

    public void setListOrders() {
        refUser.child(username).child("orders").child("commit").child(String.valueOf(number)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView textViewName = findViewById(R.id.textViewName);
                TextView textViewPaySel = findViewById(R.id.textViewPaySel);
                TextView textViewSendSel = findViewById(R.id.textViewSendSel);
                TextView textViewStatusSel = findViewById(R.id.textViewStatusSel);
                TextView textViewFull = findViewById(R.id.textViewFull);

                Order current = dataSnapshot.getValue(Order.class);
                textViewFull.setText("Итог: " + current.getFullprice() + " Руб.");
                textViewName.setText(current.getNumber());

                if (current.isCour())
                    textViewSendSel.setText(current.getAdress());
                else
                    textViewSendSel.setText("ул. Ленина, 5, Липецк");

                if (current.isCard())
                    textViewPaySel.setText("Банковская карта");
                else
                    textViewPaySel.setText("Наличные");

                textViewStatusSel.setText(current.getStatus());

                refUser.child(username).child("orders")
                        .child("commit").child(String.valueOf(number)).child("listOrderItem").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ListView orderList = findViewById(R.id.fullorder_list);
                        ArrayList<OrderItem> order = new ArrayList<OrderItem>();

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            order.add(child.getValue(OrderItem.class));
                        }

                        FullOrderAdapter adapter = new FullOrderAdapter(GetDescriberWorkerOrderActivity.this, R.layout.desc_order_item, order);
                        orderList.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("TAG", "Failed to read value.", error.toException());
                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
}
