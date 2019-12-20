package amr.com.weedingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import amr.com.weedingplanner.Objects.seller_user;
import amr.com.weedingplanner.Objects.store;
import amr.com.weedingplanner.Objects.user;

public class ShopInfo extends AppCompatActivity {


    EditText ShopName;
    EditText PhoneNumber;
    Button SignUp;
    Spinner Catagrioes;


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);
        setTitle("Shop Information");


        SignUp = findViewById(R.id.bt_signup);
        ShopName = findViewById(R.id.edt_shopname);
        Catagrioes = findViewById(R.id.spin_catagroie);
        PhoneNumber = findViewById(R.id.edt_phone_number);

        final seller_user Seller = (seller_user) getIntent().getSerializableExtra("SellerData");
        System.out.println(Seller.getEmail() + "------" + Seller.getName() + "------" + Seller.getPassword());
        System.out.println("------------+++++++++++++------");

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        getCatagrioes();

        Catagrioes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Catagrioes.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Test Internet
                firebaseAuth.createUserWithEmailAndPassword(Seller.getEmail(), Seller.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            SignupDatabase(firebaseAuth.getCurrentUser().getUid(), Seller.getName(), Seller.getEmail(), "Seller");
                            MakeStore(firebaseAuth.getUid(), Catagrioes.getSelectedItem().toString());

                            SharedPreferences sharedpreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("category", Catagrioes.getSelectedItem().toString());
                            editor.putString("userId", firebaseAuth.getUid());
                            editor.putString("userName",Seller.getName());
                            editor.apply();


                            Intent intent = new Intent(getApplicationContext(), MainSeller.class);
                            finish();
                            startActivity(intent);

                        } else {
                           Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();;

                        }
                    }
                });


            }
        });


    }

    void SignupDatabase(String Userid, String Name, String Email, String Type) {
        database.getReference("Users").child(Userid).setValue(new user(Name, Email, Type, PhoneNumber.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "SignUp Sucess", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    void MakeStore(String SellerId, String Category) {
        database.getReference("Categories").child(Category)
                .child("Stores").child(SellerId).setValue(new store(ShopName.getText().toString(), Category)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Store Made Sucessfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void getCatagrioes() {

        final ArrayList<String> CatList = new ArrayList<>();
        CatList.add("Men");
        CatList.add("Women");
        CatList.add("Jewelry");
        CatList.add("Restaurants");
        CatList.add("Cars");
        CatList.add("Halls");
        CatList.add("Hairdressing");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, CatList);
        Catagrioes.setAdapter(adapter);
        Catagrioes.setSelection(0);
    }


}
