package amr.com.weedingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import amr.com.weedingplanner.Objects.product;

public class Seller_AddProduct extends AppCompatActivity {

    EditText ProductName;
    EditText ProductCost;
    EditText ProductDescription;
    Button AddProduct;

    ImageView AddImage;

    ImageView Image1;

    RadioButton RadioYes;
    RadioButton RadioNo;

    ProgressBar progressBar;

    private FirebaseDatabase database;
    private FirebaseStorage storage;

    private static final int PICK_IMAGE_REQUEST1 = 111;

    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller__add_product);
        setTitle("Add Product");

        ProductName = findViewById(R.id.product_name);
        ProductCost = findViewById(R.id.product_cost);
        ProductDescription = findViewById(R.id.edt_product_description);
        AddProduct = findViewById(R.id.bt_add);
        progressBar = findViewById(R.id.progress);

        Image1 = findViewById(R.id.img_product);

        AddImage = findViewById(R.id.img_add);
        RadioYes = findViewById(R.id.radio_yes);
        RadioNo = findViewById(R.id.radio_no);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        AddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });


        AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE);
                final String SellerId = sharedpreferences.getString("userId", null);
                final String Category = sharedpreferences.getString("category", null);

                showProgress(true);

                UploadPic(SellerId, Category);
            }
        });


    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Image1.setImageBitmap(bitmap);
                AddImage.setVisibility(View.INVISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void UploadPic(final String SellerId, final String Category) {

        if (filePath != null) {

            StorageReference storageRef = storage.getReferenceFromUrl("gs://weeding-planner.appspot.com/");
            StorageReference storageReference = storageRef.child("Categories").child(Category).child("Stores").child(SellerId).child("products").child(ProductName.getText().toString());

            final StorageReference reference = storageReference.child("pic.jpg");

            reference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getFile(reference, SellerId, Category);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        }

    }


    void getFile(StorageReference reference, final String SellerId, final String Category) {

        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if (task.isSuccessful()) {
                    AddProductFirebase(SellerId, Category, task.getResult().toString());

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void AddProductFirebase(final String SellerId, final String Category, final String ImgUrl) {

        database.getReference("Categories").child(Category).child("Stores").child(SellerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DatabaseReference reference = dataSnapshot.getRef();
                double cost = Double.valueOf(ProductCost.getText().toString());
                int PrRent = 0;

                if (RadioYes.isChecked()) {
                    PrRent = 1;
                }
                product product_ = new product(ProductDescription.getText().toString(), ImgUrl, cost, PrRent);
                reference.child("products").child(ProductName.getText().toString()).setValue(product_);

                Toast.makeText(getApplicationContext(), "Product Added! ", Toast.LENGTH_LONG).show();
                showProgress(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again!",Toast.LENGTH_SHORT).show();

            }
        });

        Intent intent = new Intent(getApplicationContext(), MainSeller.class);
        startActivity(intent);
        finish();


    }

    void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            AddProduct.setEnabled(false);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            AddProduct.setEnabled(true);
        }
    }

}
