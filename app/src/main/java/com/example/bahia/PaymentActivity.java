package com.example.bahia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    private String addressId;
    private String paymentMethod;
    private ArrayList<Seafood> cartList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        addressId = getIntent().getStringExtra("addressId");
        paymentMethod = getIntent().getStringExtra("paymentMethod");
        cartList = (ArrayList<Seafood>) getIntent().getSerializableExtra("cartList");

        Toast.makeText(this, "Address ID: " + addressId + "\nPayment Method: " + paymentMethod, Toast.LENGTH_LONG).show();

        savePurchase();
    }

    private void savePurchase() {
        String userId = mAuth.getCurrentUser().getUid();
        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put("addressId", addressId);
        purchaseData.put("paymentMethod", paymentMethod);
        purchaseData.put("items", cartList);
        purchaseData.put("timestamp", System.currentTimeMillis());

        db.collection("users").document(userId).collection("purchases")
                .add(purchaseData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(PaymentActivity.this, "Compra guardada exitosamente", Toast.LENGTH_SHORT).show();
                    clearCart();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PaymentActivity.this, "Error al guardar la compra", Toast.LENGTH_SHORT).show();
                    Log.e("PaymentActivity", "Error al guardar la compra", e);
                });
    }

    private void clearCart() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("cart")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("users").document(userId).collection("cart").document(document.getId()).delete();
                        }

                        Intent intent = new Intent(PaymentActivity.this, PaymentSuccessActivity.class);
                        intent.putExtra("cartList", cartList);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(PaymentActivity.this, "Error al limpiar el carrito", Toast.LENGTH_SHORT).show();
                        Log.e("PaymentActivity", "Error al limpiar el carrito", task.getException());
                    }
                });
    }
}