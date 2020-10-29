package com.example.e_commerce;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {
    public ProductAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_grid, parent, false);
        }

        ImageView photoImageView = convertView.findViewById(R.id.grid_image);
        TextView nameTextView = convertView.findViewById(R.id.grid_name);
        TextView priseTextView = convertView.findViewById(R.id.grid_prise);

        Product product = getItem(position);
        URL url = null;
        try {
            assert product != null;
            url = new URL(product.getPhotoUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Glide.with(photoImageView.getContext())
                .load(url)
                .into(photoImageView);
        nameTextView.setText(product.getName());
        priseTextView.setText(product.getPrise());
        return convertView;
    }

}
