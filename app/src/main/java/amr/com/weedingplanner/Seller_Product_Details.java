package amr.com.weedingplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import amr.com.weedingplanner.Objects.product;

public class Seller_Product_Details extends AppCompatActivity {

    Button Rents;
    TextView ProductName;
    TextView ProductCost;
    TextView ProductDescription;
    TextView ShopeName;
    ImageView ProductImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller__product__details);

        Rents = findViewById(R.id.bt_rent);
        ProductName = findViewById(R.id.txt_name_);
        ProductCost = findViewById(R.id.txt_price);
        ProductDescription = findViewById(R.id.txt_description);
        ShopeName = findViewById(R.id.txt_store_name);
        ProductImg = findViewById(R.id.img_product);


        final product product = getIntent().getExtras().getParcelable("product");
        final String Catagrioe = getIntent().getStringExtra("cat");

        ProductName.setText(product.getName());
        ProductDescription.setText(product.getDescription());
        ProductCost.setText(product.getPrice() + "$");
        ShopeName.setText(product.getShopName());

        Picasso.get().load(product.getImgUrl()).into(ProductImg);

        Rents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Rents_Seller.class);
                intent.putExtra("product", product);
                intent.putExtra("cat", Catagrioe);

                startActivity(intent);
            }
        });




}
}
