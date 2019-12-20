package amr.com.weedingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import amr.com.weedingplanner.Adapters.MessageAdapter;
import amr.com.weedingplanner.Objects.message;

public class Chat extends AppCompatActivity {

    RecyclerView Messages_list;
    private FirebaseDatabase database;

    EditText Message;
    ImageView Send;
    ProgressBar progressBar;

    ArrayList<message> Messages = new ArrayList<>();
    SharedPreferences sharedpreferences;
    MessageAdapter adapter;
    long MessagesNumber = 0;

    String type;
    String ReciverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setTitle("Chat");

        Messages_list = findViewById(R.id.list_messages);
        Send = findViewById(R.id.img_send);
        Message = findViewById(R.id.edt_message);
        progressBar = findViewById(R.id.progress);

        sharedpreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();

        type = getIntent().getStringExtra("type");

        adapter = new MessageAdapter(Messages, getApplicationContext());

        showProgress(true);


        getMessges();


        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage();
            }
        });

        Messages_list.setAdapter(adapter);
        Messages_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    void getMessges() {
        if (type.equals("Store")) {

            final String SellerId = getIntent().getStringExtra("SellerId");
            final String UserId = sharedpreferences.getString("userId", null);

            database.getReference("Users").child(SellerId).child("Messages").child(UserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    MessagesNumber = dataSnapshot.child("msgs").getChildrenCount();
                    Messages.clear();

                    for (DataSnapshot data : dataSnapshot.child("msgs").getChildren()) {

                        String json = new Gson().toJson(data.getValue());
                        ReadMessages(json, dataSnapshot.child("ReciverName").getValue().toString());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else if (type.equals("Chat")) {
            ReciverId = getIntent().getStringExtra("reciver");
            final String UserId = sharedpreferences.getString("userId", null);

            database.getReference("Users").child(UserId).child("Messages").child(ReciverId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Messages.clear();
                    MessagesNumber = dataSnapshot.child("msgs").getChildrenCount();

                    for (DataSnapshot data : dataSnapshot.child("msgs").getChildren()) {

                        String json = new Gson().toJson(data.getValue());
                        ReadMessages(json, dataSnapshot.child("ReciverName").getValue().toString());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again!",Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

    void SendMessage() {

        String Reciver = "";
        if (type.equals("Store")) {

            Reciver = getIntent().getStringExtra("SellerId");

        } else if (type.equals("Chat")) {
            Reciver = getIntent().getStringExtra("reciver");
        }

        final String UserId = sharedpreferences.getString("userId", null);
        final String UserName = sharedpreferences.getString("userName", null);


        final String finalReciver = Reciver;

        database.getReference("Users").child(Reciver).child("Messages")
                .child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {

                    getReciverName(database, finalReciver, UserName, UserId);

                }else{
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
                    Date dd = new Date();
                    String date = formatter.format(dd);

                    database.getReference("Users").child(finalReciver)
                            .child("Messages").child(UserId).child("msgs").child("msg" + MessagesNumber)
                            .setValue(new message(UserId, finalReciver, Message.getText().toString(), date));


                    database.getReference("Users").child(UserId)
                            .child("Messages").child(finalReciver).child("msgs").child("msg" + MessagesNumber)
                            .setValue(new message(UserId, finalReciver, Message.getText().toString(), date));

                    getMessges();
                    Message.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again!",Toast.LENGTH_SHORT).show();

            }
        });



    }

    void ReadMessages(String json, String ReciverName) {
        adapter.setReciverName(ReciverName);

        try {
            JSONObject ob = new JSONObject(json);
            String sender = ob.getString("sender");
            String rec = ob.getString("reciver");
            String mess = ob.getString("message");
            String date = ob.getString("date");

            Messages.add(new message(sender, rec, mess, date));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        showProgress(false);
    }


    void getReciverName(final FirebaseDatabase database, final String ReciverId, final String UserName, final String UserId) {
        final String[] ReciverName = {""};

        database.getReference("Users").child(ReciverId)
                .child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ReciverName[0] = dataSnapshot.getValue().toString();


                database.getReference("Users").child(ReciverId)
                        .child("Messages").child(UserId).child("ReciverName")
                        .setValue(UserName);

                database.getReference("Users").child(UserId)
                        .child("Messages").child(ReciverId).child("ReciverName")
                        .setValue(ReciverName[0]);


                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
                Date dd = new Date();
                String date = formatter.format(dd);

                database.getReference("Users").child(ReciverId)
                        .child("Messages").child(UserId).child("msgs").child("msg" + MessagesNumber)
                        .setValue(new message(UserId, ReciverId, Message.getText().toString(), date));


                database.getReference("Users").child(UserId)
                        .child("Messages").child(ReciverId).child("msgs").child("msg" + MessagesNumber)
                        .setValue(new message(UserId, ReciverId, Message.getText().toString(), date));

                getMessges();
                Message.setText("");

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again!",Toast.LENGTH_SHORT).show();

            }
        });

    }

    void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            Messages_list.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Messages_list.setVisibility(View.VISIBLE);
        }
    }

}
