package com.yesipov.gusto;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SpecialList extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.describer_special);
        ImageView flagView = (ImageView) findViewById(R.id.special_image_dialog);
        TextView titleView = (TextView) findViewById(R.id.title_special);
        TextView describeView = (TextView) findViewById(R.id.describe_special);
        int flag = getIntent().getIntExtra("image", 0);
        String title = getIntent().getStringExtra("title");
        String describe = getIntent().getStringExtra("describe");
        flagView.setImageResource(flag);
        titleView.setText(title);
        describeView.setText(describe);
    }
}
