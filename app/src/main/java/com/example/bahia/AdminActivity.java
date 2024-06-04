package com.example.bahia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CardView btnViewOrders, btnViewUsers, btnAddProduct, btnUpdateProduct, btnViewProducts, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnViewOrders = findViewById(R.id.btn_view_orders);
        btnViewUsers = findViewById(R.id.btn_view_users);
        btnAddProduct = findViewById(R.id.btn_add_product);
        btnUpdateProduct = findViewById(R.id.btn_update_product);
        btnViewProducts = findViewById(R.id.btn_view_products);
        btnLogout = findViewById(R.id.btn_logout);

        checkAdminRole();
    }

    private void checkAdminRole() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String role = document.getString("rol");
                            if (role == null || !role.equals("Administrador")) {
                                Toast.makeText(AdminActivity.this, "No tienes permiso para acceder a esta actividad", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                setupListeners();
                            }
                        } else {
                            Toast.makeText(AdminActivity.this, "No se pudo verificar tu rol", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Log.d("AdminActivity", "Error al verificar el rol: ", task.getException());
                        Toast.makeText(AdminActivity.this, "Error al verificar el rol", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void setupListeners() {
        btnViewOrders.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ViewOrdersActivity.class);
            startActivity(intent);
        });

        btnViewUsers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ViewUsersActivity.class);
            startActivity(intent);
        });

        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        btnUpdateProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, UpdateProductActivity.class);
            startActivity(intent);
        });

        btnViewProducts.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ViewProductsActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(AdminActivity.this, "Has cerrado sesión correctamente, administrador", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}