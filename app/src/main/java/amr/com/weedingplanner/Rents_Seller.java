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
import android.widget.TextView;
import android.widget.Toast;

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

import amr.com.weedingplanner.Adapters.RentsAdapter;
import amr.com.weedingplanner.Objects.product;
import amr.com.weedingplanner.Objects.rent;

public class Rents_Seller extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView RentsList;
    private FirebaseDatabase database;
    ArrayList<rent> RentsArray = new ArrayList<>();
    FirebaseStorage storage;
    SharedPreferences sharedpreferences;


    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rents_seller);
        setTitle("Rents");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setNavigationViewListener();

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


        RentsList = findViewById(R.id.list);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance("gs://weeding-planner.appspot.com");

        sharedpreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE);


        String uname =sharedpreferences.getString("userName",null);

        NavigationView navigationView =  findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);

        TextView name =header.findViewById(R.id.name);
        name.setText(uname);



        final product product = getIntent().getExtras().getParcelable("product");
        final String Catagrioe = getIntent().getStringExtra("cat");

        String SellerId = product.getSellerId();
        final String ProductName = product.getName();


        database.getReference("Categories").child(Catagrioe).child("Stores").child(SellerId).child("products").child(ProductName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Rents")) {

                    RentsArray.clear();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Rents").getChildren()) {
                        String date = dataSnapshot1.getKey();
                        try {
                            String Json = new Gson().toJson(dataSnapshot1.getValue());
                            JSONObject object = new JSONObject(Json);
                            String status = object.getString("status");
                            String userId = object.getString("userId");
                            String UserName = object.getString("userName");

                            rent Rent  = new rent(userId,status,date,ProductName,UserName,Catagrioe);
                            addtoList(Rent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
                setRecycler(ProductName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again!",Toast.LENGTH_SHORT).show();

            }
        });


    }

    void  addtoList(rent Rent){
        RentsArray.add(Rent);
    }

    void setRecycler(String ProductName) {

        RentsAdapter adapter = new RentsAdapter(RentsArray, getApplicationContext(),database,ProductName);
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
                Intent intent1 = new Intent(getApplicationContext(), MainSeller.class);
                startActivity(intent1);
                break;
            case R.id.messages:
                Intent intent2 = new Intent(getApplicationContext(), Messages.class);
                startActivity(intent2);
                break;
            case R.id.contact:
                Intent intent5 = new Intent(getApplicationContext(), ContactUs.class);
                startActivity(intent5);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void setNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);

    }



}
