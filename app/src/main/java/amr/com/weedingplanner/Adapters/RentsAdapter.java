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

public class RentsAdapter extends RecyclerView.Adapter<RentsAdapter.viewhold> {

    private ArrayList<rent> RentList;
    private Context context;
    FirebaseDatabase database;
    String ProductName;

    public RentsAdapter(ArrayList<rent> RentList, Context context, FirebaseDatabase database,String ProductName) {
        this.RentList = RentList;
        this.context = context;
        this.database =database;
        this.ProductName =ProductName;
    }

    @NonNull
    @Override
    public viewhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rent, parent, false);
        RentsAdapter.viewhold View = new RentsAdapter.viewhold(v);
        return View;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewhold holder, int position) {

        holder.RentStatus.setText(RentList.get(holder.getAdapterPosition()).getStatus());
        holder.BuyerName.setText(RentList.get(holder.getAdapterPosition()).getUserName());
        holder.RentDate.setText(RentList.get(holder.getAdapterPosition()).getDate());

        SharedPreferences sharedpreferences = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);

        final String SellerId = sharedpreferences.getString("userId", null);
        final String Category = sharedpreferences.getString("category", null);

        if (RentList.get(holder.getAdapterPosition()).getStatus().equals("Requested")){

            holder.Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    database.getReference("Categories").child(Category).child("Stores")
                            .child(SellerId).child("products").child(ProductName).child("Rents")
                            .child(RentList.get(holder.getAdapterPosition()).getDate()).child("status").setValue("Accepted");

                    // Buyer Status
                    database.getReference("Users").child(RentList.get(holder.getAdapterPosition()).getUserId())
                            .child("Rents").child(RentList.get(holder.getAdapterPosition())
                            .getProductName()).child(RentList.get(holder.getAdapterPosition()).getDate())
                            .child("status").setValue("Accepted");


                    notifyDataSetChanged();
                }
            });
            holder.Decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Seller Status
                    database.getReference("Categories").child(Category).child("Stores")
                            .child(SellerId).child("products").child(ProductName).child("Rents")
                            .child(RentList.get(holder.getAdapterPosition()).getDate()).child("status").setValue("Declined");

                    // Buyer Status
                    database.getReference("Users").child(RentList.get(holder.getAdapterPosition()).getUserId())
                            .child("Rents").child(RentList.get(holder.getAdapterPosition())
                            .getProductName()).child(RentList.get(holder.getAdapterPosition()).getDate())
                            .child("status").setValue("Declined");

                    notifyDataSetChanged();
                }
            });

        }else if(RentList.get(holder.getAdapterPosition()).getStatus().equals("Accepted")){
            holder.Decline.setVisibility(View.INVISIBLE);
            holder.Accept.setVisibility(View.INVISIBLE);
        }else if(RentList.get(holder.getAdapterPosition()).getStatus().equals("Declined")){
            holder.Decline.setVisibility(View.INVISIBLE);
            holder.Accept.setVisibility(View.INVISIBLE);
            holder.DeleteRent.setVisibility(View.VISIBLE);

        }else if(RentList.get(holder.getAdapterPosition()).getStatus().equals("Completed")){
            holder.Decline.setVisibility(View.INVISIBLE);
            holder.Accept.setVisibility(View.INVISIBLE);
            holder.DeleteRent.setVisibility(View.VISIBLE);
        }

        holder.DeleteRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Buyer Status
                DatabaseReference ref = database.getReference("Categories").child(Category).child("Stores")
                        .child(SellerId).child("products").child(ProductName).child("Rents")
                        .child(RentList.get(holder.getAdapterPosition()).getDate());

                ref.removeValue();

                RentList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return RentList.size();
    }

    class  viewhold extends RecyclerView.ViewHolder{

          ImageView DeleteRent;
          TextView RentStatus;
          TextView BuyerName;
          TextView RentDate;
          Button Accept;
          Button Decline;

        public viewhold(@NonNull View itemView) {
            super(itemView);

            DeleteRent = itemView.findViewById(R.id.img_delete);
            RentStatus= itemView.findViewById(R.id.txt_rent_status);
            BuyerName= itemView.findViewById(R.id.txt_buyer_name);
            RentDate= itemView.findViewById(R.id.txt_rent_date);
            Accept= itemView.findViewById(R.id.bt_accept);
            Decline= itemView.findViewById(R.id.bt_decline);



        }
    }
}
