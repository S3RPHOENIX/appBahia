package com.example.bahia;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewProductsActivity extends AppCompatActivity {

    private static final String TAG = "ViewProductsActivity";

    private RecyclerView productsRecyclerView;
    private SeafoodAdapter seafoodAdapter;
    private List<Seafood> seafoodList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        db = FirebaseFirestore.getInstance();

        productsRecyclerView = findViewById(R.id.products_recycler_view);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        seafoodAdapter = new SeafoodAdapter(seafoodList, new SeafoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Seafood item) {
                // Handle item click if needed
            }
        });
        productsRecyclerView.setAdapter(seafoodAdapter);

        fetchSeafoodData();
    }

    private void fetchSeafoodData() {
        Log.d(TAG, "Fetching seafood data from Firestore...");
        db.collection("seafood")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            seafoodList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String name = document.getString("name");
                                String description = document.getString("description");
                                Double price = document.getDouble("price");
                                String imageName = document.getString("image");
                                int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());

                                if (name != null && description != null && price != null && imageName != null && imageResId != 0) {
                                    seafoodList.add(new Seafood(id, name, description, price, imageResId));
                                    Log.d(TAG, "Added seafood item: " + name + " with price: " + price);
                                } else {
                                    Log.w(TAG, "Missing field in document: " + document.getId());
                                }
                            }
                            seafoodAdapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            Toast.makeText(ViewProductsActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}