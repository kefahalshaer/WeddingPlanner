package amr.com.weedingplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amr.com.weedingplanner.Adapters.MessagesAdapter;
import amr.com.weedingplanner.Objects.messages;

public class Messages extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private FirebaseDatabase database;

    RecyclerView Messages;

    ArrayList<messages> messages = new ArrayList<>();
    SharedPreferences sharedpreferences;
    MessagesAdapter messagesAdapter;

    long MessagesNumber = 0;

    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout drawer;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        setTitle("Messages");
        setNavigationViewListener();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);




        drawer = findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                R.string.nav_open,
                R.string.nav_close
        );

        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        Messages = findViewById(R.id.list);
        progressBar = findViewById(R.id.progress);

        database = FirebaseDatabase.getInstance();
        messagesAdapter = new MessagesAdapter(messages, getApplicationContext());
        sharedpreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE);

        String uname =sharedpreferences.getString("userName",null);

        NavigationView navigationView =  findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);

        TextView name =header.findViewById(R.id.name);
        name.setText(uname);


        getMessges();
        Messages.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Messages.setAdapter(messagesAdapter);


    }


    void getMessges() {
        final String UserId = sharedpreferences.getString("userId", null);


        database.getReference("Users").child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showProgress(true);

                if (dataSnapshot.hasChild("Messages")) {
                    for (DataSnapshot snapshot : dataSnapshot.child("Messages").getChildren()) {

                        MessagesNumber = snapshot.child("msgs").getChildrenCount();
                        String ReciverId = snapshot.getKey();
                        String ReciverName = snapshot.child("ReciverName").getValue().toString();
                        String json = new Gson().toJson(snapshot.child("msgs").getValue());
                        ReadMessages(json,ReciverId,ReciverName);

                    }
                    showProgress(false);

                }

                showProgress(false);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showProgress(false);
                Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again!",Toast.LENGTH_SHORT).show();

            }
        });
    }


    void ReadMessages(String json, String ReciverId, String ReciverName) {
        messages.clear();
        int length;

        try {
            JSONObject object = new JSONObject(json);
            length = object.length();

            String mess = object.getJSONObject("msg" + (length - 1)).getString("message");
            String date = object.getJSONObject("msg" + (length - 1)).getString("date");
            messages.add(new messages(ReciverId, ReciverName, mess, date));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        messagesAdapter.notifyDataSetChanged();
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final String Type = sharedpreferences.getString("type", null);
        if (Type.equals("Seller")){
            // show only home and messages

            switch (item.getItemId()) {
                case R.id.home:
                    Intent intent1 = new Intent(getApplicationContext(), MainSeller.class);
                    startActivity(intent1);
                    break;
                case R.id.messages:
                    break;
            }
        }else if (Type.equals("Buyer")){
            switch (item.getItemId()) {
                case R.id.home:
                    Intent intent4 = new Intent(getApplicationContext(), MainBuyer.class);
                    startActivity(intent4);
                    break;

                case R.id.messages:
                    break;

                case R.id.notes:
                    Intent intent2 = new Intent(getApplicationContext(), ToDoList.class);
                    startActivity(intent2);
                    break;
                case R.id.rents:
                    Intent intent3 = new Intent(getApplicationContext(), Rents_Buyer.class);
                    startActivity(intent3);
                    break;
                case R.id.contact:
                    Intent intent5 = new Intent(getApplicationContext(), ContactUs.class);
                    startActivity(intent5);
                    break;
            }

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            Messages.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Messages.setVisibility(View.VISIBLE);
        }
    }

}
