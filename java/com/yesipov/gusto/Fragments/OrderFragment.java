package com.yesipov.gusto.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yesipov.gusto.Adapters.OrderAdapter;
import com.yesipov.gusto.Adapters.SpecialsAdapter;
import com.yesipov.gusto.Models.Food;
import com.yesipov.gusto.Models.Order;
import com.yesipov.gusto.Models.OrderItem;
import com.yesipov.gusto.Models.Specials;
import com.yesipov.gusto.R;
import com.yesipov.gusto.SendOrderActivity;
import com.yesipov.gusto.SpecialList;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("users");

    OrderAdapter adapter = null;

    Button buttonOrder;
    TextView fullPrice;
    ArrayList<OrderItem> order = new ArrayList<OrderItem>();
    int full = 0;

    public OrderFragment() {}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        buttonOrder = (Button) getView().findViewById(R.id.buttonOrder);
        fullPrice = (TextView) getView().findViewById(R.id.full_price);

        setListOrders();

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFullPrice(order);
                Intent intent = new Intent(getActivity(), SendOrderActivity.class);
                intent.putExtra("price", full);
                startActivity(intent);
            }
        });
    }

    private void isListEmpty(List<OrderItem> orders) {
        if (orders.isEmpty()) {
            buttonOrder.setEnabled(false);
        }

        else {
            buttonOrder.setEnabled(true);
        }
    }

    public void setFullPrice(List<OrderItem> orders) {
        full = 0;
        for (int i = 0; i < orders.size(); i++) {
            full += orders.get(i).getFullPrice();
        }
        fullPrice.setText("Итоговая цена: " + full);
    }

    public void setListOrders() {
        ListView productList = (ListView) getView().findViewById(R.id.order_list);
        refUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("orders").child("current").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order = new ArrayList<OrderItem>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    order.add(child.getValue(OrderItem.class));
                }

                isListEmpty(order);
                setFullPrice(order);

                adapter = new OrderAdapter(getContext(), R.layout.order_item, order);
                productList.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }
}