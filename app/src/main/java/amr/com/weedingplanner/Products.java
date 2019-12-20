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

import amr.com.weedingplanner.Adapters.ProductsAdapter;
import amr.com.weedingplanner.Objects.product;

public class Products extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView Products;
    private FirebaseDatabase database;
    ArrayList<product> productArrayList = new ArrayList<>();
    FirebaseStorage storage;
    String Catagrioe;

    DrawerLayout drawer;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        setTitle("Products");
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

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                R.string.nav_open,
                R.string.nav_close
        );

        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();



        Products = findViewById(R.id.list_products);

         Catagrioe = getIntent().getStringExtra("Catagrioe");


        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance("gs://weeding-planner.appspot.com");

        database.getReference("Categories").child(Catagrioe).child("Stores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                productArrayList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    String SellerId = data.getKey();
                    String ShopName = data.child("storeName").getValue().toString() + " Store";

                    for (DataSnapshot product : data.child("products").getChildren()) {
                        String json = new Gson().toJson(product.getValue());
                        try {
                            JSONObject object = new JSONObject(json);
                            productArrayList.add(new product(product.getKey(), object.getString("description"), object.getString("imgUrl"),
                                    object.getDouble("price"), SellerId, ShopName,object.getInt("productRent")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    setRecycler();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again!",Toast.LENGTH_SHORT).show();
            }
        });

    }


    void setRecycler() {

        ProductsAdapter adapter = new ProductsAdapter(productArrayList, getApplicationContext(),storage,Catagrioe);
        Products.setAdapter(adapter);
        Products.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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
