package amr.com.weedingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import amr.com.weedingplanner.Objects.product;
import amr.com.weedingplanner.Objects.rent;

public class Product_Details extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {

    Button Chat;
    Button Rent;
    TextView ProductName;
    TextView ProductCost;
    TextView ProductDescription;
    TextView ShopeName;
    ImageView ProductImg;


    private FirebaseDatabase database;
    SharedPreferences sharedpreferences;


    DatePickerDialog datePickerDialog;
    Calendar calendar, selctedCalendar;
    ArrayList<Calendar> RentedCalendars = new ArrayList<>();


    String UserId = "";
    String UserName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__details);

        Chat = findViewById(R.id.bt_chat);
        Rent = findViewById(R.id.bt_rent);
        ProductName = findViewById(R.id.txt_name_);
        ProductCost = findViewById(R.id.txt_price);
        ProductDescription = findViewById(R.id.txt_description);
        ShopeName = findViewById(R.id.txt_store_name);
        ProductImg = findViewById(R.id.img_product);

        calendar = Calendar.getInstance();
        selctedCalendar = Calendar.getInstance();
        database = FirebaseDatabase.getInstance();
        sharedpreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE);


        UserId = sharedpreferences.getString("userId", null);
        UserName = sharedpreferences.getString("userName", null);

        final product product = getIntent().getExtras().getParcelable("product");
        final String Catagrioe = getIntent().getStringExtra("cat");

        ProductName.setText(product.getName());
        ProductDescription.setText(product.getDescription());
        ProductCost.setText(product.getPrice() + "$");
        ShopeName.setText(product.getShopName());

        Picasso.get().load(product.getImgUrl()).into(ProductImg);

        if (product.getProductRent() == 0) {
            Rent.setVisibility(View.INVISIBLE);
        }

        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Chat.class);
                intent.putExtra("SellerId", product.getSellerId());
                intent.putExtra("type", "Store");
                startActivity(intent);
            }
        });

        Rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialog = DatePickerDialog.newInstance(Product_Details.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setTitle("Date Picker");

                datePickerDialog.setMinDate(calendar);
                Calendar Lastcalendar = Calendar.getInstance();
                Lastcalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);

                datePickerDialog.setMaxDate(Lastcalendar);// 1 YEAR
                final String SellerId = product.getSellerId();


                database.getReference("Categories").child(Catagrioe).child("Stores").child(SellerId).child("products").child(product.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("Rents")){
                            final DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            RentedCalendars.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Rents").getChildren()){
                                String date = dataSnapshot1.getKey();
                                if (dataSnapshot1.child("status").getValue().equals("Accepted")
                                        || dataSnapshot1.child("status").getValue().equals("Completed")){

                                    try {
                                        Calendar DisabledCal = Calendar.getInstance();
                                        DisabledCal.setTime(formatter.parse(date));
                                        RentedCalendars.add(DisabledCal);



                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                            for (int i=0;i<RentedCalendars.size();i++){
                                Calendar disapleDays []  = new Calendar[1];
                                disapleDays[0] =RentedCalendars.get(i);
                                datePickerDialog.setDisabledDays(disapleDays);
                                datePickerDialog.setHighlightedDays(disapleDays);
                            }

                            datePickerDialog.show(getFragmentManager(), "Pick Your Rent Date!");
                        }else{
                            datePickerDialog.show(getFragmentManager(), "Pick Your Rent Date!");

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again!",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });


    }


    void setRent() {

        final product product = getIntent().getExtras().getParcelable("product");
        final String Catagrioe = getIntent().getStringExtra("cat");

        final String SellerId = product.getSellerId();
        final String ProductName = product.getName();
        final String ShopName = product.getShopName();
        final DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        // add to seller
        database.getReference("Categories").child(Catagrioe).child("Stores").child(SellerId).child("products").child(ProductName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference reference = dataSnapshot.getRef();

                rent Rent = new rent();
                Rent.setUserName(UserName);
                Rent.setUserId(UserId);
                Rent.setStatus("Requested");
                Rent.setProductCategory(Catagrioe);

                String date = formatter.format(selctedCalendar.getTime());
                reference.child("Rents").child(date).setValue(Rent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again!",Toast.LENGTH_SHORT).show();

            }
        });


        // add to buyer
        database.getReference("Users").child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference reference = dataSnapshot.getRef();

                String date = formatter.format(selctedCalendar.getTime());

                rent Rent = new rent();
                Rent.setUserName(ShopName); // shop name
                Rent.setUserId(SellerId); // seller id
                Rent.setStatus("Requested");
                Rent.setProductCategory(Catagrioe);

                reference.child("Rents").child(ProductName).child(date).setValue(Rent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again!",Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        selctedCalendar.set(Calendar.YEAR, year);
        selctedCalendar.set(Calendar.MONTH, monthOfYear);
        selctedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        setRent();

    }


}
