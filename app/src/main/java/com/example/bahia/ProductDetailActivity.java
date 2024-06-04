package com.example.bahia;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName;
    private TextView productDescription;
    private TextView productPrice;
    private EditText productQuantity;
    private Button addToCartButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productDescription = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        productQuantity = findViewById(R.id.product_quantity);
        addToCartButton = findViewById(R.id.add_to_cart_button);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        String id = getIntent().getStringExtra("PRODUCT_ID");
        String name = getIntent().getStringExtra("PRODUCT_NAME");
        String description = getIntent().getStringExtra("PRODUCT_DESCRIPTION");
        String imageUrl = getIntent().getStringExtra("PRODUCT_IMAGE_URL");
        double price = getIntent().getDoubleExtra("PRODUCT_PRICE", 0.0);

        productName.setText(name);
        productDescription.setText(description);
        productPrice.setText(String.format("$%.2f", price));

        Glide.with(this).load(imageUrl).into(productImage);

        addToCartButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(productQuantity.getText().toString());
            addToCart(id, name, description, price, imageUrl, quantity);
        });
    }

    private void addToCart(String id, String name, String description, double price, String imageUrl, int quantity) {
        String userId = mAuth.getCurrentUser().getUid();
        Seafood seafood = new Seafood(id, name, description, price, imageUrl, quantity);

        db.collection("users").document(userId).collection("cart").document(id)
                .set(seafood.toMap())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProductDetailActivity.this, "Producto añadido al carrito", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProductDetailActivity.this, "Error al añadir el producto", Toast.LENGTH_SHORT).show();
                });
    }
}