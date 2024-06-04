package com.example.bahia;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextCorreo, editTextContrasena;
    private Button buttonRegistrarse;
    private RadioGroup radioGroupRole;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextNombre = findViewById(R.id.nombre);
        editTextCorreo = findViewById(R.id.correo);
        editTextContrasena = findViewById(R.id.contrasena);
        buttonRegistrarse = findViewById(R.id.btn_iniciar);
        radioGroupRole = findViewById(R.id.radioGroupRole);

        buttonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editTextNombre.getText().toString().trim();
                String correo = editTextCorreo.getText().toString().trim();
                String contrasena = editTextContrasena.getText().toString().trim();
                int selectedRoleId = radioGroupRole.getCheckedRadioButtonId();

                if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(contrasena) || selectedRoleId == -1) {
                    Toast.makeText(RegisterActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    RadioButton selectedRadioButton = findViewById(selectedRoleId);
                    String role = selectedRadioButton.getText().toString();
                    registerUser(nombre, correo, contrasena, role);
                }
            }
        });
    }

    private void registerUser(String nombre, String correo, String contrasena, String role) {
        mAuth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                saveUserToFirestore(currentUser.getUid(), nombre, correo, role);
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error de registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserToFirestore(String userId, String nombre, String correo, String rol) {
        Map<String, Object> user = new HashMap<>();
        user.put("nombre", nombre);
        user.put("correo", correo);
        user.put("rol", rol);

        db.collection("users").document(userId).set(user)
                .addOnSuccessListener(aVoid -> {
                    if ("Administrador".equals(rol)) {
                        Toast.makeText(RegisterActivity.this, "Registro exitoso: Administrador creado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registro exitoso: Usuario creado", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Error al guardar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}