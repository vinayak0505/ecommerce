package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class ProductBuyActivity extends AppCompatActivity {
    ImageView productImage;
    TextView userText;
    EditText nameText;
    EditText descriptionText;
    EditText priceText;
    Button button;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_buy);

        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        username = mAuth.getCurrentUser().getDisplayName();
        storageReference = storage.getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("products");

        productImage = findViewById(R.id.add_image);
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        nameText = findViewById(R.id.add_name);
        priceText = findViewById(R.id.add_prise);
        userText = findViewById(R.id.add_user);
        button = findViewById(R.id.upload_button);
        userText.setText(String.format("Sold By:-%s", username));
        descriptionText = findViewById(R.id.add_description);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                productImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                productImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                          @Override
                                          public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                              if (!task.isSuccessful()) {
                                                  throw task.getException();
                                              }

                                              // Continue with the task to get the download URL
                                              return ref.getDownloadUrl();
                                          }
                                      }
                    ).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        uploadData(downloadUri);
                        progressDialog.dismiss();
                        Toast.makeText(ProductBuyActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(ProductBuyActivity.this, "Failed ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void uploadData(Uri downloadUri) {
        String downloadURL =  downloadUri.toString();
        Product product = new Product(username,nameText.getText().toString(),descriptionText.getText().toString(),downloadURL,priceText.getText().toString(),null);
        databaseReference.push().setValue(product);
    }

}













