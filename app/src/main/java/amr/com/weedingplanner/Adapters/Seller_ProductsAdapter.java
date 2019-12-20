package amr.com.weedingplanner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import amr.com.weedingplanner.Objects.ToDoItem;
import amr.com.weedingplanner.Objects.product;
import amr.com.weedingplanner.Product_Details;
import amr.com.weedingplanner.R;
import amr.com.weedingplanner.Seller_Product_Details;

public class Seller_ProductsAdapter extends RecyclerView.Adapter<Seller_ProductsAdapter.ViewHold> {

    ArrayList<product> Products;
    Context context;
    private FirebaseDatabase database;
    String Catagroie;



    public Seller_ProductsAdapter(ArrayList<product> Products, Context context, FirebaseDatabase database, String Catagroie) {
        this.Products = Products;
        this.context = context;
        this.database = database;
        this.Catagroie = Catagroie;


    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seller_product, parent, false);
        return new Seller_ProductsAdapter.ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHold holder, int position) {

        holder.Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Seller_Product_Details.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("product", Products.get(holder.getAdapterPosition()));
                intent.putExtra("cat", Catagroie);
                context.startActivity(intent);
            }
        });
        SharedPreferences sharedpreferences = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);

        final String SellerId = sharedpreferences.getString("userId", null);
        final String Category = sharedpreferences.getString("category", null);


        holder.ProductDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.getReference("Categories").child(Category).child("Stores").
                        child(SellerId).child("products").child(Products.get(holder.getAdapterPosition()).getName()).removeValue();
                notifyDataSetChanged();

            }
        });

        holder.ProductName.setText(Products.get(holder.getAdapterPosition()).getName());
        holder.ProductDescription.setText(Products.get(holder.getAdapterPosition()).getDescription());
        holder.ProductPrice.setText(Products.get(holder.getAdapterPosition()).getPrice() + "$");

        Glide.with(context).load(Products.get(holder.getAdapterPosition()).getImgUrl()).into(holder.ProductImg);

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
        ImageView ProductDelete;

        public ViewHold(@NonNull View itemView) {
            super(itemView);
            Layout = itemView.findViewById(R.id.main);
            ProductImg = itemView.findViewById(R.id.img_product_img);
            ProductName = itemView.findViewById(R.id.txt_product_name);
            ProductPrice = itemView.findViewById(R.id.txt_product_price);
            ProductDescription = itemView.findViewById(R.id.txt_product_description);
            ProductDelete = itemView.findViewById(R.id.img_delete);

        }
    }
}
