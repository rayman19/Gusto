package com.yesipov.gusto.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yesipov.gusto.Models.Order;
import com.yesipov.gusto.Models.OrderItem;
import com.yesipov.gusto.R;

import java.util.ArrayList;
import java.util.List;

public class FullOrderAdapter extends ArrayAdapter<OrderItem> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<OrderItem> productList;

    public FullOrderAdapter(Context context, int resource, ArrayList<OrderItem> products) {
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
        viewHolder.countView.setText("Кол-во: " + String.valueOf(product.getCount()));
        product.setFullPrice(product.getFood().getPrice() * product.getCount());
        viewHolder.flagView.setImageResource(product.getFood().getFlagResource());
        viewHolder.priceView.setText(String.valueOf(product.getFullPrice()) + " Руб");

        return convertView;
    }
    private class ViewHolder {
        final TextView nameView, countView, priceView;
        final ImageView flagView;

        ViewHolder(View view){
            nameView = (TextView) view.findViewById(R.id.title_food_fullorder);
            countView = (TextView) view.findViewById(R.id.count_food_fullorder);
            priceView = (TextView) view.findViewById(R.id.price_food_fullorder);
            flagView = (ImageView) view.findViewById(R.id.image_food_fullorder);
        }
    }
}