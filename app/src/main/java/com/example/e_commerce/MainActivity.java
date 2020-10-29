package com.example.e_commerce;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProductAdapter mProductAdapter;
    private GridView mProductGridView;
    private String mUserName;
    private String userType;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mDatabaseReference;
    private FloatingActionButton button;
    private String seller = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        button = findViewById(R.id.add_fab_button);
        userType = sharedpreferences.getString(getString(R.string.pref_user_key), getString(R.string.pref_user_label_buyer));
        mAuth = FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductBuyActivity.class);
                startActivity(intent);
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("products").child(seller);

        final List<Product> products = new ArrayList<>();
        mProductAdapter = new ProductAdapter(this, R.layout.product_grid, products);
        mProductGridView = findViewById(R.id.grid_view);
        mProductGridView.setStackFromBottom(true);
        mProductGridView.setAdapter(mProductAdapter);
        mProductGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ProductDescription.class);
                Product currentProduct = products.get(position);
                intent.putExtra("name", currentProduct.getName());
                intent.putExtra("description", currentProduct.getDescription());
                intent.putExtra("userName", currentProduct.getUserName());
                intent.putExtra("photoUrl", currentProduct.getPhotoUrl());
                intent.putExtra("prise", currentProduct.getPrise());
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                //to sign out
                mAuth.signOut();
                mProductAdapter.clear();
                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                return true;

            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getString(R.string.pref_user_label_buyer).equals(userType)) {
            button.setVisibility(View.GONE);
            seller = "";
        } else {
            button.setVisibility(View.VISIBLE);
            seller = mAuth.getCurrentUser().getDisplayName().toString();
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("products");

        }
        attachDatabaseReadListener();

    }


    @Override
    protected void onPause() {
        super.onPause();
        mProductAdapter.clear();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Product product;
                    product = dataSnapshot.getValue(Product.class);
                    if (product.getUserName().equals(seller) && !seller.isEmpty())
                        mProductAdapter.add(product);
                    else if (seller.isEmpty()) {
                        mProductAdapter.add(product);
                    }
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}