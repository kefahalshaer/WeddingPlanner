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

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

import amr.com.weedingplanner.Adapters.Seller_ProductsAdapter;
import amr.com.weedingplanner.Objects.product;

public class MainSeller extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView ProductList;
    FloatingActionButton AddProduct;
    DrawerLayout drawer;
    ActionBarDrawerToggle mDrawerToggle;


    private FirebaseDatabase database;
    ArrayList<product> productArrayList = new ArrayList<>();
    Seller_ProductsAdapter adapter;
    SharedPreferences sharedpreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seller);

        setTitle("Products");
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

        ProductList = findViewById(R.id.list_products);
        AddProduct = findViewById(R.id.floating_action_button);

        sharedpreferences= getSharedPreferences("Pref", Context.MODE_PRIVATE);

        String uname =sharedpreferences.getString("userName",null);

        NavigationView navigationView =  findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);

        TextView name =header.findViewById(R.id.name);
        name.setText(uname);



        database = FirebaseDatabase.getInstance();
        String Category = sharedpreferences.getString("category", null);

        adapter = new Seller_ProductsAdapter(productArrayList, getApplicationContext(),database,Category);
        getProducts();

        ProductList.setAdapter(adapter);
        ProductList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Seller_AddProduct.class);
                startActivity(intent);
            }
        });


    }

    void ShowProducts(String ProductName, String ShopName, String SellerId, String json) {
        try {
            JSONObject object = new JSONObject(json);
            double Price = object.getDouble("price");
            String ImgUrl = object.getString("imgUrl");
            String Description = object.getString("description");
            int productRent = object.getInt("productRent");
            product pro = new product(ProductName, Description, ImgUrl, Price, SellerId, ShopName,productRent);
            productArrayList.add(pro);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    void getProducts() {

        final String SellerId = sharedpreferences.getString("userId", null);
        String Category = sharedpreferences.getString("category", null);

        database.getReference("Categories").child(Category).child("Stores").child(SellerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productArrayList.clear();

                String ShopName = dataSnapshot.child("storeName").getValue().toString() + " Store";

                for (DataSnapshot snapshot : dataSnapshot.child("products").getChildren()) {

                    String ProductName = snapshot.getKey();
                    String json = new Gson().toJson(snapshot.getValue());
                    ShowProducts(ProductName, ShopName, SellerId, json);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again!",Toast.LENGTH_SHORT).show();

            }
        });


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
                break;
            case R.id.messages:
                Intent intent1 = new Intent(getApplicationContext(), Messages.class);
                startActivity(intent1);
                break;
            case R.id.contact:
                Intent intent4 = new Intent(getApplicationContext(), ContactUs.class);
                startActivity(intent4);
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
