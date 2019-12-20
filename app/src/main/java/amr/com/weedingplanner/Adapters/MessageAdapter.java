package amr.com.weedingplanner.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import amr.com.weedingplanner.Objects.message;
import amr.com.weedingplanner.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHold> {


    private ArrayList<message> MessageArray;
    private Context context;
    private String ReciverName;

    public MessageAdapter(ArrayList<message> MessageArray, Context context, String ReciverName) {
        this.MessageArray = MessageArray;
        this.context = context;
        this.ReciverName = ReciverName;
    }

    public MessageAdapter(ArrayList<message> MessageArray, Context context) {
        this.MessageArray = MessageArray;
        this.context = context;
    }


    public String getReciverName() {
        return ReciverName;
    }

    public void setReciverName(String reciverName) {
        ReciverName = reciverName;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageAdapter.ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {

        SharedPreferences sharedpreferences = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        String UserId = sharedpreferences.getString("userId", null);
        if (UserId != null) {
            if (MessageArray.get(position).getSender().equals(UserId)) {
                holder.Sender.setText("Me");

                holder.Layout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                holder.Img.setImageResource(R.drawable.user2);
            } else {
                holder.Layout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                holder.Sender.setText(ReciverName);

            }

        }

        holder.Message.setText(MessageArray.get(position).getMessage());
        holder.Date.setText(MessageArray.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return MessageArray.size();
    }

    class ViewHold extends RecyclerView.ViewHolder {
        TextView Sender;
        TextView Message;
        TextView Date;
        ImageView Img;
        RelativeLayout Layout;


        public ViewHold(@NonNull View itemView) {
            super(itemView);

            Sender = itemView.findViewById(R.id.txt_sender);
            Message = itemView.findViewById(R.id.txt_message);
            Date = itemView.findViewById(R.id.txt_date);
            Img = itemView.findViewById(R.id.img_profile);
            Layout = itemView.findViewById(R.id.main);
        }
    }
}
