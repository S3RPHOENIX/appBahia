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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewUsersActivity extends AppCompatActivity {

    private static final String TAG = "ViewUsersActivity";
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recycler_view_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);

        fetchUserData();
    }

    private void fetchUserData() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            userList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String name = document.getString("nombre");
                                String email = document.getString("correo");
                                String role = document.getString("rol");

                                if (name != null && email != null && role != null && !role.equals("Administrador")) {
                                    User user = new User(id, name, email, role);
                                    fetchUserAddresses(user);
                                } else {
                                    Log.w(TAG, "Missing field in document: " + document.getId());
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            Toast.makeText(ViewUsersActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void fetchUserAddresses(User user) {
        db.collection("users").document(user.getId()).collection("addresses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Address> addresses = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String city = document.getString("Ciudad");
                                String postalCode = document.getString("Codigo Postal");
                                String address = document.getString("Dirección");
                                String state = document.getString("Estado");
                                String name = document.getString("Nombre");
                                String phone = document.getString("Telefono");

                                if (city != null && postalCode != null && address != null && state != null && name != null && phone != null) {
                                    addresses.add(new Address(id, city, postalCode, address, state, name, phone));
                                } else {
                                    Log.w(TAG, "Missing field in address document: " + document.getId());
                                }
                            }
                            user.setAddresses(addresses);
                            userList.add(user);
                            userAdapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting address documents.", task.getException());
                        }
                    }
                });
    }
}