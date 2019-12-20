package amr.com.weedingplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class ContactUs extends AppCompatActivity {

    EditText Subject;
    EditText Message;
    Button Send;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setTitle("Contact Us");

        Subject = findViewById(R.id.Subject);
        Message = findViewById(R.id.Message);
        Send = findViewById(R.id.bt_send);


        database = FirebaseDatabase.getInstance();


        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE);
                String UserId = sharedpreferences.getString("userId", null);
                if (UserId != null) {
                    if (!Message.getText().toString().isEmpty() && !Message.getText().toString().isEmpty())  {

                        database.getReference("Contacts").child(UserId).child(Subject.getText().toString())
                                .child("message").setValue(Message.getText().toString());

                        Subject.setText("");
                        Message.setText("");

                    }else{
                        Subject.setError("Please Add Subject");
                        Message.setError("Please Add Message");
                    }
                }

                Toast.makeText(getApplicationContext(),"Thank you , Your Voice Matters!",Toast.LENGTH_LONG).show();
            }
        });







    }
}
