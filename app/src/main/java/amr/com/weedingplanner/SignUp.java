package amr.com.weedingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import amr.com.weedingplanner.Objects.seller_user;
import amr.com.weedingplanner.Objects.user;


public class SignUp extends AppCompatActivity {
    EditText txtName;
    EditText Email;
    EditText Password;
    EditText PasswordRep;

    Button Signup;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Full Screen
        getWindow().addFlags(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        txtName = findViewById(R.id.edt_name);
        Email = findViewById(R.id.edt_email);
        Password = findViewById(R.id.edt_password);
        PasswordRep = findViewById(R.id.edt_password2);

        Signup = findViewById(R.id.bt_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        final String type = getIntent().getStringExtra("type");




        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type.equals("Seller")) {


                    if (!txtName.getText().toString().isEmpty() && !Email.getText().toString().isEmpty() &&
                            !Password.getText().toString().isEmpty() && !PasswordRep.getText().toString().isEmpty()
                            && !type.isEmpty()) {

                        if (txtName.getText().toString().length() > 6) {
                            if (Password.length() > 6 && PasswordRep.length() > 6) {

                                if (Password.getText().toString().equals(PasswordRep.getText().toString())) {


                                                seller_user Seller = new seller_user(txtName.getText().toString(), Email.getText().toString(), Password.getText().toString());
                                                Intent intent = new Intent(getApplicationContext(), ShopInfo.class);
                                                intent.putExtra("SellerData", Seller);
                                                startActivity(intent);

                                            }

                                } else {
                                    Password.setError("Password and Repeated Password must be the same!");
                                }

                            } else {
                                Password.setError("Password and Repeated Password must be be more than 6 Characters!");

                            }

                        } else {
                            txtName.setError("Name must be more than 6 Characters");
                        }

                    } else {

                    if (!txtName.getText().toString().isEmpty() && !Email.getText().toString().isEmpty() &&
                            !Password.getText().toString().isEmpty() && !PasswordRep.getText().toString().isEmpty()
                            && !type.isEmpty()) {

                        if (txtName.getText().toString().length() > 6) {
                            if (Password.length() > 6 && PasswordRep.length() > 6) {

                                if (Password.getText().toString().equals(PasswordRep.getText().toString())) {

                                    firebaseAuth.createUserWithEmailAndPassword(Email.getText().toString(), Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if (task.isSuccessful()) {
                                                SignupDatabase(firebaseAuth.getCurrentUser().getUid(), txtName.getText().toString(), Email.getText().toString(), "Buyer");
                                                SharedPreferences sharedpreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                                editor.putString("userId", firebaseAuth.getUid());
                                                editor.putString("userName", txtName.getText().toString());

                                                editor.apply();

                                                Intent intent = new Intent(getApplicationContext(), MainBuyer.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        }
                                    });


                                } else {
                                    Password.setError("Password and Repeated Password must be the same!");
                                    Password.setFocusable(true);
                                }

                            } else {
                                Password.setError("Password and Repeated Password must be be more than 6 Characters!");

                            }

                        } else {
                            txtName.setError("Name must be more than 6 Characters");
                        }

                    }
                }
            }
        });



    }


    void SignupDatabase (String Userid, String Name, String Email, String Type){
        database.getReference("Users").child(Userid).setValue(new user(Name, Email, Type)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "SignUp Sucess", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}





