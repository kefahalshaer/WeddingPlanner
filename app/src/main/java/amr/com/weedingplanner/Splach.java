package amr.com.weedingplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Splach extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splach);
        // Full Screen
        getWindow().addFlags(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        // Start Coding
        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                        Thread.sleep(3000);
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                        finish();

                        } catch (InterruptedException e) {
                          e.printStackTrace();
                          }
                    }
                }
        );

        thread.start();
    }
}
