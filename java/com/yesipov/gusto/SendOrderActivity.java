package com.yesipov.gusto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yesipov.gusto.Adapters.OrderAdapter;
import com.yesipov.gusto.Models.Order;
import com.yesipov.gusto.Models.OrderItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendOrderActivity extends AppCompatActivity {
    String typePay, typeSend, adress = "";
    DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("users");
    RadioButton selectCour, selectCard, selectGand, selectSelf;
    boolean isCour = false, isCard = false;
    int price;
    final int minSend = 599;
    final int costSend = 99;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_order);
        price = getIntent().getIntExtra("price", 0);

        EditText editAddress = (EditText) findViewById(R.id.editФdress);
        TextView priceView = (TextView) findViewById(R.id.textCost);
        selectCour = (RadioButton) findViewById(R.id.radioButtonKur);
        selectSelf = (RadioButton) findViewById(R.id.radioButtonSamo);
        selectCard = (RadioButton) findViewById(R.id.radioButtonKart);
        selectGand = (RadioButton) findViewById(R.id.radioButtonNal);
        Button sendOrder = (Button) findViewById(R.id.buttonSendOrder);
        priceView.setText("К оплате: " + String.valueOf(price) + " Руб.");

        RadioGroup radioGroupSend = findViewById(R.id.radioGroupSend);
        radioGroupSend.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonKur: {
                        editAddress.setVisibility(View.VISIBLE);

                        if (price < minSend && !isCour) {
                            price += costSend;
                            priceView.setText("К оплате: " + String.valueOf(price) + " Руб. (+ Курьер)");
                        }
                        else {
                            price = getIntent().getIntExtra("price", 0);
                            priceView.setText("К оплате: " + String.valueOf(price) + " Руб.");
                        }

                        isCour = true;
                        typeSend = selectCour.getText().toString();
                        break;
                    }
                    case R.id.radioButtonSamo: {
                        editAddress.setVisibility(View.GONE);
                        isCour = false;
                        price = getIntent().getIntExtra("price", 0);
                        priceView.setText("К оплате: " + String.valueOf(price) + " Руб.");
                        adress = "";
                        typeSend = selectSelf.getText().toString();
                        break;
                    }
                }
            }
        });

        RadioGroup radioGroupPay = findViewById(R.id.radioGroupPay);
        radioGroupPay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonNal: {
                        typePay = selectGand.getText().toString();
                        isCard = false;
                        break;
                    }
                    case R.id.radioButtonKart: {
                        typePay = selectCard.getText().toString();
                        isCard = true;
                        break;
                    }
                }
            }
        });

        sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("orders").child("current").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<OrderItem> order = new ArrayList<OrderItem>();

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            order.add(child.getValue(OrderItem.class));
                        }

                        Log.d("TIME", Timestamp.now().toDate().toString());

                        refUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("orders").child("commit")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        adress = editAddress.getText().toString();
                                        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date number = Timestamp.now().toDate();

                                       /* refUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("points").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                    int newPoints = dataSnapshot.getValue(int.class);
                                                    refUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("points")
                                                        .setValue(newPoints + Math.round(price * 0.05))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                            }
                                                        });
                                                }
                                            @Override
                                            public void onCancelled(DatabaseError error) {
                                                Log.w("TAG", "Failed to read value.", error.toException());
                                            }
                                        });
*/
                                        refUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("orders").child("commit").child(formater.format(number))
                                                .setValue(new Order(formater.format(number), "Готовится", price, order, adress, isCour, isCard, FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("TAG", "Failed to read value.", error.toException());
                    }
                });
                Intent intent = new Intent(SendOrderActivity.this, Congrate.class);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });
    }
}
