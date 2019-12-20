package amr.com.weedingplanner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;


import amr.com.weedingplanner.Objects.product;
import amr.com.weedingplanner.Product_Details;
import amr.com.weedingplanner.R;
import amr.com.weedingplanner.Seller_Product_Details;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHold> {
    ArrayList<product> Products;
    Context context;
    FirebaseStorage storage;
    String Catagroie;

    public ProductsAdapter(ArrayList<product> Products, Context context, FirebaseStorage storage, String Catagroie) {

        this.Products = Products;
        this.context = context;
        this.storage = storage;
        this.Catagroie = Catagroie;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHold holder, int position) {


        holder.Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go To Detail Page
                Intent intent = new Intent(context, Product_Details.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("product", Products.get(holder.getAdapterPosition()));
                intent.putExtra("cat", Catagroie);
                context.startActivity(intent);
            }
        });


        // Load Img
        holder.ProductName.setText(Products.get(position).getName());
        holder.ProductDescription.setText(Products.get(position).getDescription());
        holder.ProductPrice.setText(Products.get(position).getPrice() + "$");

        Picasso.get().load(Products.get(position).getImgUrl()).into(holder.ProductImg);

    }

    @Override
    public int getItemCount() {
        return Products.size();
    }

    class ViewHold extends RecyclerView.ViewHolder {
        RelativeLayout Layout;
        ImageView ProductImg;
        TextView ProductName;
        TextView ProductPrice;
        TextView ProductDescription;

        ViewHold(@NonNull View itemView) {
            super(itemView);
            Layout = itemView.findViewById(R.id.main);
            ProductImg = itemView.findViewById(R.id.img_product_img);
            ProductName = itemView.findViewById(R.id.txt_product_name);
            ProductPrice = itemView.findViewById(R.id.txt_product_price);
            ProductDescription = itemView.findViewById(R.id.txt_product_description);
        }


    }
}
