package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;

public class ProductDescription extends AppCompatActivity {

    ImageView imageView;
    Button button;
    TextView textName;
    TextView textDescription;
    TextView textPrise;
    TextView textUser;
    URL url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);
        imageView = findViewById(R.id.buy_image);
        textUser = findViewById(R.id.buy_user);
        textPrise = findViewById(R.id.buy_prise);
        textDescription = findViewById(R.id.buy_description);
        textName = findViewById(R.id.buy_name);
        button = findViewById(R.id.buy_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        textName.setText(getIntent().getStringExtra("name"));
        textDescription.setText(getIntent().getStringExtra("description"));
        textPrise.setText(String.format("Rs%s",getIntent().getStringExtra("prise")));
        textUser.setText(String.format("Sold By:-%s",getIntent().getStringExtra("userName")));

        try {
            url = new URL(getIntent().getStringExtra("photoUrl"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Glide.with(this)
                .load(url)
                .into(imageView);
    }
}