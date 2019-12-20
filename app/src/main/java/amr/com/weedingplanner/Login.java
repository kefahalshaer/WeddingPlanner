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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import amr.com.weedingplanner.Network.NetworkClass;


public class Login extends AppCompatActivity {

    EditText email;
    EditText Password;

    TextView SignUp;
    TextView WrongLogin;

    Button Login;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    ProgressBar progressBar;

    /*
        @Override
        protected void onStart() {
            super.onStart();
            firebaseAuth = FirebaseAuth.getInstance();
            if (firebaseAuth.getCurrentUser() != null) {

                Intent intent = new Intent(getApplicationContext(), MainBuyer.class);
                startActivity(intent);
                finish();

            }

        }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Full Screen
        getWindow().addFlags(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Start Coding
        email = findViewById(R.id.edt_email);
        Password = findViewById(R.id.edt_password);

        SignUp = findViewById(R.id.txt_signup);

        Login = findViewById(R.id.bt_login);
        WrongLogin = findViewById(R.id.txt_wrong_login);

        progressBar = findViewById(R.id.progress);




        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkClass.TestNetwork(getApplicationContext())) {
                    // There is internet


                    showProgress(true);
                    WrongLogin.setVisibility(View.INVISIBLE);

                    if (Validate(email.getText().toString(), Password.getText().toString())) {

                        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    SharedPreferences sharedpreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("userId", firebaseAuth.getUid());
                                    editor.apply();

                                    Start(firebaseAuth.getUid(), database);

                                } else {

                                    showProgress(false);
                                    WrongLogin.setVisibility(View.VISIBLE);
                                    // show error

                                }
                            }
                        });


                    }
                }else{
                    // There is no internet

                }


            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), signup_type.class);
                startActivity(intent);
            }
        });


    }

    // Validate the email and password from be code or empty etc.
    public static Boolean Validate(String email, String Password) {
        boolean Valid = false;

        if (!email.isEmpty() && !Password.isEmpty()) {
            Valid = true;
        }

        return Valid;
    }

    void Start(final String UserId, final FirebaseDatabase database) {
        database.getReference("Users").child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String object = new Gson().toJson(dataSnapshot.getValue());

                SharedPreferences sharedpreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("userName", dataSnapshot.child("name").getValue().toString());

                try {
                    JSONObject jsonObject = new JSONObject(object);
                    if (jsonObject != null) {
                        if (jsonObject.getString("type").equals("Buyer")) {
                            Intent intent = new Intent(getApplicationContext(), MainBuyer.class);
                            editor.putString("type", "Buyer");
                            editor.apply();
                            startActivity(intent);

                        } else if (jsonObject.getString("type").equals("Seller")) {
                            Intent intent = new Intent(getApplicationContext(), MainSeller.class);
                            editor.putString("type", "Seller");
                            editor.apply();
                            startActivity(intent);

                        }



                        finish();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong Please Try Again!",Toast.LENGTH_SHORT).show();

            }
        });
    }

    void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            Login.setText("");
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Login.setText("Log In");
        }
    }

}
