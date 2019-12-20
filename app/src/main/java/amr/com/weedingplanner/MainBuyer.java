package amr.com.weedingplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import amr.com.weedingplanner.Adapters.CatagroiesAdapter;
import amr.com.weedingplanner.Objects.Categorie;

public class MainBuyer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle mDrawerToggle;
    RecyclerView Catagroies;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        setTitle("Categories");


        SharedPreferences sharedpreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE);
        String uname =sharedpreferences.getString("userName",null);


        NavigationView navigationView =  findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);

        TextView  name =header.findViewById(R.id.name);
        name.setText(uname);


        // Main work

        Catagroies = findViewById(R.id.cat);

        ArrayList<Categorie> Cat = new ArrayList<>();

        Cat.add(new Categorie("Men's Clothes", "Men"));
        Cat.add(new Categorie("Women's Clothes", "Women"));
        Cat.add(new Categorie("Jewelry", "Jewelry"));
        Cat.add(new Categorie("Restaurants", "Restaurants"));
        Cat.add(new Categorie("Wedding Cars", "Cars"));
        Cat.add(new Categorie("Wedding Halls", "Halls"));
        Cat.add(new Categorie("Hairdressing", "Hairdressing"));

        CatagroiesAdapter catagroiesAdapter = new CatagroiesAdapter(Cat, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

        Catagroies.setAdapter(catagroiesAdapter);
        Catagroies.setLayoutManager(mLayoutManager);


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
            case R.id.notes:
                Intent intent2 = new Intent(getApplicationContext(), ToDoList.class);
                startActivity(intent2);

                break;
            case R.id.rents:
                Intent intent3 = new Intent(getApplicationContext(), Rents_Buyer.class);
                startActivity(intent3);
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
