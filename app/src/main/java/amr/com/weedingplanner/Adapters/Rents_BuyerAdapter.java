package amr.com.weedingplanner.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import amr.com.weedingplanner.Objects.rent;
import amr.com.weedingplanner.R;

public class Rents_BuyerAdapter extends RecyclerView.Adapter<Rents_BuyerAdapter.viewhold> {

    private ArrayList<rent> RentList;
    private Context context;
    FirebaseDatabase database;

    public Rents_BuyerAdapter(ArrayList<rent> RentList, Context context, FirebaseDatabase database) {
        this.RentList = RentList;
        this.context = context;
        this.database = database;
    }

    @NonNull
    @Override
    public viewhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rent_buyer, parent, false);
        Rents_BuyerAdapter.viewhold View = new Rents_BuyerAdapter.viewhold(v);
        return View;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewhold holder, int position) {

        holder.RentStatus.setText(RentList.get(holder.getAdapterPosition()).getStatus());
        holder.StoreName.setText(RentList.get(holder.getAdapterPosition()).getUserName());
        holder.ProductName.setText(RentList.get(holder.getAdapterPosition()).getProductName());
        holder.RentDate.setText(RentList.get(holder.getAdapterPosition()).getDate());

        SharedPreferences sharedpreferences = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);

        final String UserId = sharedpreferences.getString("userId", null);

        if (RentList.get(holder.getAdapterPosition()).getStatus().equals("Requested")) {
            holder.Complete.setVisibility(View.INVISIBLE);

            holder.Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Delete From User and Seller
                    DatabaseReference ref1 = database.getReference("Categories")
                            .child(RentList.get(holder.getAdapterPosition()).getProductCategory())
                            .child("Stores").child(RentList.get(holder.getAdapterPosition()).getUserId()).child("products")
                            .child(RentList.get(holder.getAdapterPosition()).getProductName()).child("Rents")
                            .child(RentList.get(holder.getAdapterPosition()).getDate());

                    ref1.removeValue();

                    // Buyer Status
                    DatabaseReference ref2 = database.getReference("Users").child(UserId)
                            .child("Rents").child(RentList.get(holder.getAdapterPosition()).getProductName())
                            .child(RentList.get(holder.getAdapterPosition()).getDate());

                    ref2.removeValue();

                    RentList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();


                }
            });


        } else if (RentList.get(holder.getAdapterPosition()).getStatus().equals("Accepted")) {

            holder.Complete.setVisibility(View.VISIBLE);

            holder.Complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Complete For Buyer
                    database.getReference("Users").child(UserId)
                            .child("Rents").child(RentList.get(holder.getAdapterPosition())
                            .getProductName()).child(RentList.get(holder.getAdapterPosition()).getDate())
                            .child("status").setValue("Completed");

                    // Complete For Seller
                    database.getReference("Categories")
                            .child(RentList.get(holder.getAdapterPosition()).getProductCategory())
                            .child("Stores").child(RentList.get(holder.getAdapterPosition()).getUserId()).child("products")
                            .child(RentList.get(holder.getAdapterPosition()).getProductName()).child("Rents")
                            .child(RentList.get(holder.getAdapterPosition()).getDate()).child("status").setValue("Completed");

                }
            });


            // if declined  - accepted
            holder.Cancel.setVisibility(View.INVISIBLE);

        } else if (RentList.get(holder.getAdapterPosition()).getStatus().equals("Declined")) {
            holder.Cancel.setVisibility(View.INVISIBLE);
            holder.Complete.setVisibility(View.INVISIBLE);

            holder.DeleteRent.setVisibility(View.VISIBLE);

        } else if (RentList.get(holder.getAdapterPosition()).getStatus().equals("Completed")) {
            holder.DeleteRent.setVisibility(View.VISIBLE);

            holder.Cancel.setVisibility(View.INVISIBLE);
            holder.Complete.setVisibility(View.INVISIBLE);

        }

        holder.DeleteRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Buyer Status
                DatabaseReference ref2 = database.getReference("Users").child(UserId)
                        .child("Rents").child(RentList.get(holder.getAdapterPosition()).getProductName())
                        .child(RentList.get(holder.getAdapterPosition()).getDate());

                ref2.removeValue();

                RentList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return RentList.size();
    }

    class viewhold extends RecyclerView.ViewHolder {

        TextView RentStatus;
        TextView StoreName;
        TextView ProductName;
        TextView RentDate;
        Button Complete;
        Button Cancel;
        ImageView DeleteRent;

        public viewhold(@NonNull View itemView) {
            super(itemView);

            RentStatus = itemView.findViewById(R.id.txt_rent_status);
            StoreName = itemView.findViewById(R.id.txt_store_name);
            ProductName = itemView.findViewById(R.id.txt_product_name);
            RentDate = itemView.findViewById(R.id.txt_rent_date);
            Complete = itemView.findViewById(R.id.bt_complete);
            Cancel = itemView.findViewById(R.id.bt_cancel);
            DeleteRent = itemView.findViewById(R.id.img_delete);


        }
    }

}
