package amr.com.weedingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import amr.com.weedingplanner.Objects.ToDoItem;
import amr.com.weedingplanner.R;
import amr.com.weedingplanner.ToDoList;

public class AddToDoItem extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {


    private FirebaseDatabase database;

    EditText WorkName;
    EditText LastDate;
    EditText Description;

    Calendar calendar, selctedCalendar;
    DatePickerDialog datePickerDialog;

    Button Add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do_item);


        database = FirebaseDatabase.getInstance();

        WorkName = findViewById(R.id.work_name);
        LastDate = findViewById(R.id.last_date);
        Description = findViewById(R.id.edt_work_description);

        Add = findViewById(R.id.bt_add);

        calendar = Calendar.getInstance();
        selctedCalendar = Calendar.getInstance();

        LastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialog = DatePickerDialog.newInstance(AddToDoItem.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setTitle("Choose Work Date");

                datePickerDialog.setMinDate(calendar);
                Calendar Lastcalendar = Calendar.getInstance();
                Lastcalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);

                datePickerDialog.setMaxDate(Lastcalendar);
                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");


            }
        });



        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedpreferences = getSharedPreferences("Pref", Context.MODE_PRIVATE);
                String UserId = sharedpreferences.getString("userId", null);
                if (UserId != null) {

                    DatabaseReference reference = database.getReference("Users").child(UserId);
                    ToDoItem item = new ToDoItem(Description.getText().toString(), LastDate.getText().toString(), false);
                    reference.child("Notes").child(WorkName.getText().toString()).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Intent intent = new Intent(getApplicationContext(), ToDoList.class);
                                startActivity(intent);
                                finish();

                            }
                        }
                    });
                }

            }
        });


    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        selctedCalendar.set(Calendar.YEAR, year);
        selctedCalendar.set(Calendar.MONTH, monthOfYear);
        selctedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        final DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String date = formatter.format(selctedCalendar.getTime());

        LastDate.setText(date);

    }
}