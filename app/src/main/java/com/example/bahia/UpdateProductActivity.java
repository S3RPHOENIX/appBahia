package com.example.bahia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class UpdateProductActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextPrice;
    private Button btnSelectImage, btnUpdateProduct;
    private ImageView imageViewProduct;
    private Uri imageUri;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        editTextName = findViewById(R.id.edit_product_name);
        editTextDescription = findViewById(R.id.edit_product_description);
        editTextPrice = findViewById(R.id.edit_product_price);
        btnSelectImage = findViewById(R.id.btn_select_image);
        btnUpdateProduct = findViewById(R.id.btn_update_product);
        imageViewProduct = findViewById(R.id.image_view_product);

        productId = getIntent().getStringExtra("PRODUCT_ID");
        editTextName.setText(getIntent().getStringExtra("PRODUCT_NAME"));
        editTextDescription.setText(getIntent().getStringExtra("PRODUCT_DESCRIPTION"));
        editTextPrice.setText(String.valueOf(getIntent().getDoubleExtra("PRODUCT_PRICE", 0)));

        btnSelectImage.setOnClickListener(v -> selectImage());
        btnUpdateProduct.setOnClickListener(v -> updateProduct());
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewProduct.setImageURI(imageUri);
        }
    }

    private void updateProduct() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        double price = Double.parseDouble(editTextPrice.getText().toString().trim());

        if (imageUri != null) {
            uploadImage(name, description, price);
        } else {
            saveProductToFirestore(name, description, price, null);
        }
    }

    private void uploadImage(String name, String description, double price) {
        String imageFileName = UUID.randomUUID().toString();
        StorageReference imageRef = storageReference.child("images/" + imageFileName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveProductToFirestore(name, description, price, imageUrl);
                }))
                .addOnFailureListener(e -> Toast.makeText(UpdateProductActivity.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show());
    }

    private void saveProductToFirestore(String name, String description, double price, String imageUrl) {
        db.collection("seafood").document(productId)
                .update("name", name,
                        "description", description,
                        "price", price,
                        "imageUrl", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateProductActivity.this, "Producto actualizado con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(UpdateProductActivity.this, "Error al actualizar el producto", Toast.LENGTH_SHORT).show());
    }
}