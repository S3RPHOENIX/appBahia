package com.example.bahia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    private EditText editTextName, editTextAddress, editTextCity, editTextState, editTextZip, editTextPhone;
    private Button buttonSaveAddress;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ArrayList<Seafood> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.edit_text_name);
        editTextAddress = findViewById(R.id.edit_text_address);
        editTextCity = findViewById(R.id.edit_text_city);
        editTextState = findViewById(R.id.edit_text_state);
        editTextZip = findViewById(R.id.edit_text_zip);
        editTextPhone = findViewById(R.id.edit_text_phone);
        buttonSaveAddress = findViewById(R.id.button_save_address);

        cartList = (ArrayList<Seafood>) getIntent().getSerializableExtra("cartList");

        buttonSaveAddress.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String address = editTextAddress.getText().toString();
            String city = editTextCity.getText().toString();
            String state = editTextState.getText().toString();
            String zip = editTextZip.getText().toString();
            String phone = editTextPhone.getText().toString();

            if (name.isEmpty() || address.isEmpty() || city.isEmpty() || state.isEmpty() || zip.isEmpty() || phone.isEmpty()) {
                Toast.makeText(AddAddressActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                saveAddressToFirestore(name, address, city, state, zip, phone);
            }
        });
    }

    private void saveAddressToFirestore(String name, String address, String city, String state, String zip, String phone) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            Map<String, Object> addressData = new HashMap<>();
            addressData.put("Nombre", name);
            addressData.put("Dirección", address);
            addressData.put("Ciudad", city);
            addressData.put("Estado", state);
            addressData.put("Codigo Postal", zip);
            addressData.put("Telefono", phone);

            db.collection("users").document(userId).collection("addresses").add(addressData)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddAddressActivity.this, "Dirección guardada", Toast.LENGTH_SHORT).show();
                                String addressId = task.getResult().getId();
                                Intent intent = new Intent(AddAddressActivity.this, PaymentOptionActivity.class);
                                intent.putExtra("addressId", addressId);
                                intent.putExtra("cartList", cartList);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(AddAddressActivity.this, "Error al guardar la dirección", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(AddAddressActivity.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}