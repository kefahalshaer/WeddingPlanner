package amr.com.weedingplanner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import amr.com.weedingplanner.Chat;
import amr.com.weedingplanner.Objects.messages;
import amr.com.weedingplanner.R;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHold> {

    ArrayList<messages> Messages;
    Context context;

    public MessagesAdapter(ArrayList<messages> Messages, Context context) {
        this.Messages = Messages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messages, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHold holder, int position) {
        holder.StoreName.setText(Messages.get(holder.getAdapterPosition()).getReciverName());
        holder.LasMessage.setText(Messages.get(holder.getAdapterPosition()).getMessage());
        holder.Date.setText(Messages.get(holder.getAdapterPosition()).getDate());


        holder.Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("type", "Chat");
                intent.putExtra("reciver", Messages.get(holder.getAdapterPosition()).getReciverId());
                intent.putExtra("ReciverName", Messages.get(holder.getAdapterPosition()).getReciverName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return Messages.size();
    }

    class ViewHold extends RecyclerView.ViewHolder {
        RelativeLayout Layout;
        TextView StoreName;
        TextView LasMessage;
        TextView Date;

        ViewHold(@NonNull View itemView) {
            super(itemView);
            StoreName = itemView.findViewById(R.id.txt_store_name);
            LasMessage = itemView.findViewById(R.id.txt_last_message);
            Date = itemView.findViewById(R.id.txt_date);
            Layout = itemView.findViewById(R.id.main);
        }


    }
}
