package amr.com.weedingplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.card.MaterialCardView;

public class signup_type extends AppCompatActivity {

    MaterialCardView SellerCard;
    MaterialCardView BuyerCard;

    CheckBox SellerCheck;
    CheckBox BuyerCheck;

    Button Next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_type);

        getWindow().addFlags(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        SellerCard = findViewById(R.id.card_seller);
        BuyerCard = findViewById(R.id.card_client);

        SellerCheck = findViewById(R.id.check_seller);
        BuyerCheck = findViewById(R.id.check_client);

        Next = findViewById(R.id.bt_next);

        BuyerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SellerCheck.isChecked()) {
                    BuyerCheck.setChecked(true);
                    SellerCheck.setChecked(false);
                }
            }
        });

        SellerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BuyerCheck.isChecked()) {
                    BuyerCheck.setChecked(false);
                    SellerCheck.setChecked(true);
                }
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);

                if (SellerCheck.isChecked()) {
                    intent.putExtra("type", "Seller");
                } else {
                    intent.putExtra("type", "Buyer");
                }

                startActivity(intent);
                finish();
            }
        });


    }
}
