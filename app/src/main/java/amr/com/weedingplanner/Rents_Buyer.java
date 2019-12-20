package amr.com.weedingplanner;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amr.com.weedingplanner.Adapters.Rents_BuyerAdapter;
import amr.com.weedingplanner.Objects.rent;

public class Rents_Buyer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView RentsList;
    private FirebaseDatabase database;
    ArrayList<rent> RentsArray = new ArrayList<>();
    FirebaseStorage storage;
    SharedPreferences sharedpreferences;

    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout drawer;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rents__buyer);
        setTitle("Rents");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawer = findViewById(R.id.drawer_layout);
        progressBar= findViewById(R.id.progress);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                R.string.nav_open,
                R.string.nav_close
        );

        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        RentsList = findViewById(R.id.list);
        progressBar = findViewById(R.id.progress);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance("gs://weeding-planner.appspot.com");

        sharedpreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE);

        String uname =sharedpreferences.getString("userName",null);

        View header=navigationView.getHeaderView(0);

        TextView name =header.findViewById(R.id.name);
        name.setText(uname);

        String UserId = sharedpreferences.getString("userId", null);

        database.getReference("Users").child(UserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showProgress(true);

                if (dataSnapshot.hasChild("Rents")){
                    RentsArray.clear();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Rents").getChildren()) {
                       String ProductName = dataSnapshot1.getKey();

                       for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                           String date = dataSnapshot2.getKey();

                           try {
                               String Json = new Gson().toJson(dataSnapshot2.getValue());
                               JSONObject object = new JSONObject(Json);
                               String status = object.getString("status");
                               String SellerId = object.getString("userId");
                               String StoreName = object.getString("userName");
                               String Catagrioe = object.getString("productCategory");

                               rent Rent  = new rent(SellerId,status,date,ProductName,StoreName,Catagrioe);
                               addtoList(Rent);
                           } catch (JSONException e) {
                               e.printStackTrace();
                           }

                       }

                    }

                    setRecycler();
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

    void  addtoList(rent Rent){
        RentsArray.add(Rent);
    }

    void setRecycler() {

        Rents_BuyerAdapter adapter = new Rents_BuyerAdapter(RentsArray, getApplicationContext(),database);
        RentsList.setAdapter(adapter);
        RentsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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
            RentsList.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            RentsList.setVisibility(View.VISIBLE);
        }
    }
}
