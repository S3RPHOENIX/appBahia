package com.example.bahia;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewOrdersActivity extends AppCompatActivity {

    private static final String TAG = "ViewOrdersActivity";

    private RecyclerView purchasesRecyclerView;
    private PurchaseAdapter purchaseAdapter;
    private List<Purchase> purchaseList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        db = FirebaseFirestore.getInstance();

        purchasesRecyclerView = findViewById(R.id.purchases_recycler_view);
        purchasesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        purchaseAdapter = new PurchaseAdapter(purchaseList);
        purchasesRecyclerView.setAdapter(purchaseAdapter);

        fetchUserPurchases();
    }

    private void fetchUserPurchases() {
        Log.d(TAG, "Fetching user purchases...");
        db.collectionGroup("purchases")  // Use collectionGroup to get all purchases across all users
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        purchaseList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            Purchase purchase = document.toObject(Purchase.class);
                            purchaseList.add(purchase);
                        }
                        if (purchaseList.isEmpty()) {
                            Log.d(TAG, "No purchases found");
                            Toast.makeText(ViewOrdersActivity.this, "No se encontraron compras de los usuarios", Toast.LENGTH_SHORT).show();
                        } else {
                            purchaseAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        Toast.makeText(ViewOrdersActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}