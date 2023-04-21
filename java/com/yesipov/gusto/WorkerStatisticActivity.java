package com.yesipov.gusto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.yesipov.gusto.Adapters.ShortOrderWorkerAdapter;
import com.yesipov.gusto.Models.Food;
import com.yesipov.gusto.Models.Order;
import com.yesipov.gusto.Models.OrderItem;
import com.yesipov.gusto.Models.User;

import java.util.ArrayList;
import java.util.List;

public class WorkerStatisticActivity extends AppCompatActivity{

    TextView countOrder, countUser, countUserOrder, countPizza, countDrink, countRefrigo, countDesert;
    DatabaseReference refUsers = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_stat_activity);
        countOrder = findViewById(R.id.workCountOrderValue);
        countUser = findViewById(R.id.workCountUsersValue);
        countUserOrder = findViewById(R.id.workCountUsersOrdersValue);
        countPizza = findViewById(R.id.workCountPizzaValue);
        countDrink = findViewById(R.id.workCountDrinkValue);
        countRefrigo = findViewById(R.id.workCountRefregsValue);
        countDesert = findViewById(R.id.workCountDesertValue);

        List foods = new ArrayList<Order>();


        refUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList orders = new ArrayList<Order>();
                ArrayList users = new ArrayList<User>();
                int countOrderI = 0, countUserI = 0, countUserOrderI = 0, countPizzaI = 0, countDrinkI = 0, countRefrigoI = 0, countDesertI = 0;

                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    boolean check = false;
                    countUserI += 1;

                    users.add(user.getValue(User.class));

                    for (DataSnapshot child : user.child("orders").child("commit").getChildren()) {
                        check = true;
                        countOrderI += 1;
                        orders.add(child.getValue(Order.class));

                        for (DataSnapshot food : child.child("listOrderItem").getChildren()) {
                            int count = food.getValue(OrderItem.class).getCount();
                            Food foodItem = food.child("food").getValue(Food.class);
                            if (foodItem.getCategory().equals("Пицца")) {
                                countPizzaI += 1 * count;
                            }

                            else if (foodItem.getCategory().equals("Закуска")) {
                                countRefrigoI += 1 * count;
                            }

                            else if (foodItem.getCategory().equals("Десерт")) {
                                countDesertI += 1 * count;
                            }

                            else if (foodItem.getCategory().equals("Напитки")) {
                                countDrinkI += 1 * count;
                            }
                        }
                    }

                    if (check) {
                        countUserOrderI +=1 ;
                    }
                }

                countDesert.setText(String.valueOf(countDesertI));
                countDrink.setText(String.valueOf(countDrinkI));
                countOrder.setText(String.valueOf(countOrderI));
                countPizza.setText(String.valueOf(countPizzaI));
                countRefrigo.setText(String.valueOf(countRefrigoI));
                countUserOrder.setText(String.valueOf(countUserOrderI));
                countUser.setText(String.valueOf(countUserI));
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
}
