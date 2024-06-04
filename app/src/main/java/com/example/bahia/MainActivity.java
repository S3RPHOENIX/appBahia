package com.example.bahia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button buttonCart;
    private Button btnLogout;
    private Button btnPedidos;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SeafoodAdapter seafoodAdapter;
    private List<Seafood> seafoodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btn_signup);
        buttonCart = findViewById(R.id.button_cart);
        btnPedidos = findViewById(R.id.btn_pedidos);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                Toast.makeText(MainActivity.this, "Has cerrado sesión correctamente", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });

        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        btnPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyPurchasesActivity.class);
                Toast.makeText(MainActivity.this, "Bienvenido a su apartado de Pedidos", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        seafoodAdapter = new SeafoodAdapter(seafoodList, new SeafoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Seafood item) {
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("PRODUCT_ID", item.getId());
                intent.putExtra("PRODUCT_NAME", item.getName());
                intent.putExtra("PRODUCT_DESCRIPTION", item.getDescription());
                intent.putExtra("PRODUCT_IMAGE", item.getImageResId());
                intent.putExtra("PRODUCT_PRICE", item.getPrice());
                startActivity(intent);
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
                                String imageName = document.getString("image");
                                int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());

                                if (name != null && description != null && price != null && imageName != null && imageResId != 0) {
                                    seafoodList.add(new Seafood(id, name, description + " - $" + price, price, imageResId));
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
}