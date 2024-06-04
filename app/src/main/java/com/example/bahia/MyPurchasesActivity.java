package com.example.bahia;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyPurchasesActivity extends AppCompatActivity {

    private static final String TAG = "MyPurchasesActivity";
    private RecyclerView purchasesRecyclerView;
    private PurchaseAdapter purchasesAdapter;
    private List<Purchase> purchasesList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView totalPriceTextView;
    private static final double DELIVERY_CHARGE = 50.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purchases);

        purchasesRecyclerView = findViewById(R.id.purchases_recycler_view);
        totalPriceTextView = findViewById(R.id.total_price_text_view);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        purchasesList = new ArrayList<>();
        purchasesAdapter = new PurchaseAdapter(purchasesList);
        purchasesRecyclerView.setAdapter(purchasesAdapter);
        purchasesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchPurchasesData();
    }

    private void fetchPurchasesData() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("purchases")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        purchasesList.clear();
                        double totalAmount = 0.0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Purchase purchase = document.toObject(Purchase.class);
                            purchasesList.add(purchase);
                            totalAmount += calculateTotalAmount(purchase);
                        }
                        totalAmount += DELIVERY_CHARGE;
                        totalPriceTextView.setText(String.format("Total: $%.2f", totalAmount));
                        purchasesAdapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting documents: ", task.getException());
                        Toast.makeText(MyPurchasesActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private double calculateTotalAmount(Purchase purchase) {
        double total = 0.0;
        for (Seafood item : purchase.getItems()) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
}