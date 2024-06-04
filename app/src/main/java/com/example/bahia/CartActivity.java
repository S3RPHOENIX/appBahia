package com.example.bahia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private static final String TAG = "CartActivity";
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<Seafood> cartList;
    private TextView totalPrice;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        totalPrice = findViewById(R.id.total_price);

        checkoutButton = findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, AddAddressActivity.class);
            intent.putExtra("cartList", (Serializable) cartList);
            startActivity(intent);
        });

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(cartList, this::updateTotalPrice, this::removeFromCart);
        cartRecyclerView.setAdapter(cartAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchCartData();
    }

    private void fetchCartData() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("cart")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        cartList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String name = document.getString("name");
                            String description = document.getString("description");
                            Double price = document.getDouble("price");
                            String imageUrl = document.getString("imageUrl");
                            Long quantity = document.getLong("quantity");

                            if (name != null && description != null && price != null && imageUrl != null && quantity != null) {
                                cartList.add(new Seafood(id, name, description, price, imageUrl, quantity.intValue()));
                            } else {
                                Log.w(TAG, "Missing field in document: " + id);
                            }
                        }
                        cartAdapter.notifyDataSetChanged();
                        updateTotalPrice();
                    } else {
                        Log.w(TAG, "Error getting documents: ", task.getException());
                        Toast.makeText(CartActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateTotalPrice() {
        double total = 0.0;
        for (Seafood item : cartList) {
            total += item.getPrice() * item.getQuantity();
        }
        totalPrice.setText(String.format("Total: $%.2f", total));
    }

    private void removeFromCart(Seafood item) {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("cart").document(item.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    cartList.remove(item);
                    cartAdapter.notifyDataSetChanged();
                    updateTotalPrice();
                    Toast.makeText(CartActivity.this, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CartActivity.this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show();
                });
    }
}