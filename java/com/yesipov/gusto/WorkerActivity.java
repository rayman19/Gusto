package com.yesipov.gusto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yesipov.gusto.Adapters.ShortOrderAdapter;
import com.yesipov.gusto.Adapters.ShortOrderWorkerAdapter;
import com.yesipov.gusto.Models.Order;
import com.yesipov.gusto.Orders.GetDescriberOrderActivity;
import com.yesipov.gusto.Orders.GetOrdersActivity;

import java.util.ArrayList;

public class WorkerActivity extends AppCompatActivity implements View.OnClickListener {


    DatabaseReference refUsers = FirebaseDatabase.getInstance().getReference("users");
    Button btnExit, btnStat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_activity);
        btnExit = (Button) findViewById(R.id.buttonExitWorker);
        btnStat = (Button) findViewById(R.id.buttonStatWorker);
        btnExit.setOnClickListener(this);
        btnStat.setOnClickListener(this);
        setListOrders();
    }

    public void setListOrders() {
        refUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList orders = new ArrayList<Order>();
                RecyclerView ordersList = findViewById(R.id.recyclerViewOrders);

                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    for (DataSnapshot child : user.child("orders").child("commit").getChildren()) {
                        orders.add(child.getValue(Order.class));
                    }
                }

                ShortOrderWorkerAdapter.OnOrderClickListener orderClickListener = new ShortOrderWorkerAdapter.OnOrderClickListener() {
                    @Override
                    public void onOrderClick(Order order, int position) {
                        Intent intent = new Intent(WorkerActivity.this, GetDescriberWorkerOrderActivity.class);
                        intent.putExtra("position", order.getNumber());
                        intent.putExtra("username", order.getUsername());
                        startActivity(intent);
                    }
                };

                ShortOrderWorkerAdapter adapter = new ShortOrderWorkerAdapter(WorkerActivity.this, orders, orderClickListener);
                ordersList.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonExitWorker: {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            }
            case R.id.buttonStatWorker: {
                startActivity(new Intent(this, WorkerStatisticActivity.class));
                break;
            }
        }
    }
}
