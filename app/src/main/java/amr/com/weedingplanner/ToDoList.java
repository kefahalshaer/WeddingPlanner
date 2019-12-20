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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amr.com.weedingplanner.Adapters.ToDoAdapter;
import amr.com.weedingplanner.Objects.ToDoItem;

public class ToDoList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase database;

    RadioButton RadioDone;
    RadioButton RadioToDo;
    RadioButton RadioAll;
    RadioGroup RadioGroup;
    RecyclerView Items;
    ToDoAdapter Adapter;

    ArrayList<ToDoItem> arrayList = new ArrayList<>();

    FloatingActionButton AddItem;
    String UserId;

    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout drawer;


    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        setTitle("Notes");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setNavigationViewListener();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        SharedPreferences sharedpreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE);
        String uname =sharedpreferences.getString("userName",null);

        NavigationView navigationView =  findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);

        TextView name =header.findViewById(R.id.name);
        name.setText(uname);


        drawer = findViewById(R.id.drawer_layout);
        progressBar = findViewById(R.id.progress);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                R.string.nav_open,
                R.string.nav_close
        );

        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        Items = findViewById(R.id.recycler_to_do);
        RadioDone = findViewById(R.id.radio_done);
        RadioToDo = findViewById(R.id.radio_todo);
        RadioAll = findViewById(R.id.radio_all);
        RadioGroup = findViewById(R.id.radiogroup);
        AddItem = findViewById(R.id.floating_action_button);
        progressBar = findViewById(R.id.progress);

        database = FirebaseDatabase.getInstance();


        AddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddToDoItem.class);
                startActivity(intent);
            }
        });

        UserId = sharedpreferences.getString("userId", null);
        if (UserId != null) {
            showProgress(true);
            database.getReference("Users").child(UserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    arrayList.clear();

                    if (dataSnapshot.hasChild("Notes")) {

                        for (DataSnapshot snapshot : dataSnapshot.child("Notes").getChildren()) {
                            try {
                                String name = snapshot.getKey();
                                String json = new Gson().toJson(snapshot.getValue());
                                getItems(name, json);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    showProgress(false);
                    Adapter.notifyDataSetChanged();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showProgress(false);
                    Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again!",Toast.LENGTH_SHORT).show();

                }
            });


        }

        Adapter = new ToDoAdapter(arrayList, database, UserId);
        Items.setAdapter(Adapter);
        Items.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.RadioGroup radioGroup, int i) {

                setNewData(i);

            }
        });

    }


    void setNewData(int id) {
        ArrayList<ToDoItem> ModifiedList = new ArrayList<>();

        switch (id) {
            case R.id.radio_done:
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).isDone()) {
                        ModifiedList.add(arrayList.get(i));
                    }
                }
                break;
            case R.id.radio_todo:
                Toast.makeText(getApplicationContext(), "ToDo", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < arrayList.size(); i++) {
                    if (!arrayList.get(i).isDone()) {
                        ModifiedList.add(arrayList.get(i));
                    }
                }
                break;
            case R.id.radio_all:
                Toast.makeText(getApplicationContext(), "All", Toast.LENGTH_SHORT).show();
                ModifiedList = arrayList;
                break;
        }
        Adapter = new ToDoAdapter(ModifiedList, database, UserId);
        Items.setAdapter(Adapter);
    }

    void getItems(String name, String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        String Description = object.getString("description");
        String LastDate = object.getString("lastDate");
        boolean Done = object.getBoolean("done");

        arrayList.add(new ToDoItem(name, Description, LastDate, Done));

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
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent4 = new Intent(getApplicationContext(), MainBuyer.class);
                startActivity(intent4);
                break;
            case R.id.messages:
                Intent intent1 = new Intent(getApplicationContext(), Messages.class);
                startActivity(intent1);
                break;

            case R.id.notes:
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
            Items.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Items.setVisibility(View.VISIBLE);
        }
    }

}
