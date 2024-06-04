package com.example.bahia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewProductsActivity extends AppCompatActivity {

    private static final String TAG = "ViewProductsActivity";
    private RecyclerView recyclerView;
    private SeafoodAdapter seafoodAdapter;
    private List<Seafood> seafoodList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recycler_view_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        seafoodList = new ArrayList<>();
        seafoodAdapter = new SeafoodAdapter(seafoodList, new SeafoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Seafood item) {
                Intent intent = new Intent(ViewProductsActivity.this, UpdateProductActivity.class);
                intent.putExtra("PRODUCT_ID", item.getId());
                intent.putExtra("PRODUCT_NAME", item.getName());
                intent.putExtra("PRODUCT_DESCRIPTION", item.getDescription());
                intent.putExtra("PRODUCT_PRICE", item.getPrice());
                intent.putExtra("PRODUCT_IMAGE_URL", item.getImageUrl());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Seafood item) {
                deleteProduct(item);
            }
        });
        recyclerView.setAdapter(seafoodAdapter);

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
                                String imageUrl = document.getString("imageUrl");

                                if (name != null && description != null && price != null && imageUrl != null) {
                                    seafoodList.add(new Seafood(id, name, description, price, imageUrl));
                                    Log.d(TAG, "Added seafood item: " + name);
                                } else {
                                    Log.w(TAG, "Missing field in document: " + document.getId());
                                }
                            }
                            seafoodAdapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void deleteProduct(Seafood item) {
        db.collection("seafood").document(item.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        seafoodList.remove(item);
                        seafoodAdapter.notifyDataSetChanged();
                        Toast.makeText(ViewProductsActivity.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewProductsActivity.this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}