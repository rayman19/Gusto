package com.yesipov.gusto.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yesipov.gusto.Models.Food;
import com.yesipov.gusto.Models.OrderItem;
import com.yesipov.gusto.Models.Specials;
import com.yesipov.gusto.R;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    public interface OnFoodClickListener{
        void onFoodClick(Food food, int position);
    }

    DatabaseReference refFood = FirebaseDatabase.getInstance().getReference("food");
    DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("users");

    private final OnFoodClickListener onClickListener;
    private final LayoutInflater inflater;
    private final List<Food> foods;
    RelativeLayout root;

    public FoodAdapter(Context context, List<Food> foods, OnFoodClickListener onClickListener) {
            this.onClickListener = onClickListener;
            this.foods = foods;
            this.inflater = LayoutInflater.from(context);
    }

    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.food_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodAdapter.ViewHolder holder, int position) {
        Food food = foods.get(position);
        holder.flagView.setImageResource(food.getFlagResource());
        holder.titleView.setText(food.getName());
        holder.containerView.setText("Состав: " + food.getContainer());
        holder.weightView.setText("Вес: " + food.getWeight() + " г.");
        holder.button.setText("Добавить в корзину - " + food.getPrice() + "Р");
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("orders").child("current").child(food.getName())
                        .setValue(new OrderItem(food)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(inflater.getContext(), "Товар добавлен в корзину!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onFoodClick(food, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView flagView;
        final TextView titleView, weightView, containerView;
        final Button button;

        ViewHolder(View view){
            super(view);
            flagView = (ImageView)view.findViewById(R.id.image_food);
            titleView = (TextView)view.findViewById(R.id.title_food);
            weightView = (TextView)view.findViewById(R.id.weight_food);
            containerView = (TextView)view.findViewById(R.id.describe_food);
            button = (Button)view.findViewById(R.id.send_food);
        }
    }
}