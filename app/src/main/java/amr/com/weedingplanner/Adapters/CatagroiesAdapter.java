package amr.com.weedingplanner.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import amr.com.weedingplanner.Objects.Categorie;
import amr.com.weedingplanner.Products;
import amr.com.weedingplanner.R;

public class CatagroiesAdapter extends RecyclerView.Adapter<CatagroiesAdapter.viewHold> {

    private ArrayList<Categorie> CatList;
    private Context context;

    public CatagroiesAdapter(ArrayList<Categorie> CatList, Context context) {
        this.CatList = CatList;
        this.context = context;
    }


    @NonNull
    @Override
    public viewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catagroie, parent, false);
        viewHold View = new viewHold(v);
        return View;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHold holder, int position) {
        switch (position) {
            // Men's Clothes
            case 0:
                holder.CatImg.setImageResource(R.drawable.men1);
                break;

            // Women's Clothes
            case 1:
                holder.CatImg.setImageResource(R.drawable.woman1);
                break;

            // Jewelry
            case 2:
                holder.CatImg.setImageResource(R.drawable.jewelry1);
                break;

            // Restaurants
            case 3:
                holder.CatImg.setImageResource(R.drawable.food1);
                break;

            // Wedding Cars
            case 4:
                holder.CatImg.setImageResource(R.drawable.car1);
                break;

            // Wedding Halls
            case 5:
                holder.CatImg.setImageResource(R.drawable.hall1);
                break;

            // hairdressing salon
            case 6:
                holder.CatImg.setImageResource(R.drawable.hairdressing1);
                break;
        }

        holder.CatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Products.class);
                intent.putExtra("Catagrioe", CatList.get(holder.getLayoutPosition()).getCategorieDatabaseName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        holder.CatName.setText(CatList.get(holder.getLayoutPosition()).getCategorieName());
    }

    @Override
    public int getItemCount() {
        return CatList.size();
    }

    class viewHold extends RecyclerView.ViewHolder {
        ImageView CatImg;
        TextView CatName;

        public viewHold(@NonNull View itemView) {
            super(itemView);

            CatImg = itemView.findViewById(R.id.img_cat);
            CatName = itemView.findViewById(R.id.txt_catname);

        }
    }

}
