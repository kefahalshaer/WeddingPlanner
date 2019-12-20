package amr.com.weedingplanner.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


import amr.com.weedingplanner.Objects.ToDoItem;
import amr.com.weedingplanner.R;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHold> {

    ArrayList<ToDoItem> arrayList;
    private FirebaseDatabase database;
    private String UserId;

    public ToDoAdapter(ArrayList<ToDoItem> arrayList, FirebaseDatabase database, String UserId) {
        this.arrayList = arrayList;
        this.database = database;
        this.UserId = UserId;

    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHold holder, int position) {
        holder.Name.setText(arrayList.get(holder.getAdapterPosition()).getName());
        holder.Desc.setText(arrayList.get(holder.getAdapterPosition()).getDescription());
        holder.LastDate.setText(arrayList.get(holder.getAdapterPosition()).getLastDate());
        holder.Done.setChecked(arrayList.get(holder.getAdapterPosition()).isDone());

        holder.Done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                database.getReference("Users").child(UserId).child("Notes").
                        child(arrayList.get(holder.getAdapterPosition()).getName())
                        .setValue(new ToDoItem(arrayList.get(holder.getAdapterPosition()).getDescription(),
                                arrayList.get(holder.getAdapterPosition()).getLastDate(), b));

            }
        });

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.getReference("Users").child(UserId).child("Notes").
                        child(arrayList.get(holder.getAdapterPosition()).getName()).removeValue();
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHold extends RecyclerView.ViewHolder {

        TextView Name;
        TextView Desc;
        TextView LastDate;
        CheckBox Done;
        ImageView Delete;

        ViewHold(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.txt_name);
            Desc = itemView.findViewById(R.id.txt_description);
            LastDate = itemView.findViewById(R.id.txt_last_date);
            Done = itemView.findViewById(R.id.check_done);
            Delete = itemView.findViewById(R.id.img_delete);
        }
    }
}
