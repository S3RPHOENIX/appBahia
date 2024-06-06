package com.example.bahia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class AddProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editProductId, editProductName, editProductDescription, editProductPrice;
    private Button btnSelectImage, btnAddProduct;
    private ImageView imageViewProduct;
    private Uri imageUri;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        editProductId = findViewById(R.id.edit_product_id);
        editProductName = findViewById(R.id.edit_product_name);
        editProductDescription = findViewById(R.id.edit_product_description);
        editProductPrice = findViewById(R.id.edit_product_price);
        btnSelectImage = findViewById(R.id.btn_select_image);
        btnAddProduct = findViewById(R.id.btn_add_product);
        imageViewProduct = findViewById(R.id.image_view_product);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewProduct.setImageURI(imageUri);
        }
    }

    private void addProduct() {
        String productId = editProductId.getText().toString().trim();
        String productName = editProductName.getText().toString().trim();
        String productDescription = editProductDescription.getText().toString().trim();
        String productPriceStr = editProductPrice.getText().toString().trim();

        if (TextUtils.isEmpty(productId) || TextUtils.isEmpty(productName) || TextUtils.isEmpty(productDescription) || TextUtils.isEmpty(productPriceStr) || imageUri == null) {
            Toast.makeText(AddProductActivity.this, "Por favor, complete todos los campos y seleccione una imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        double productPrice = Double.parseDouble(productPriceStr);

        StorageReference fileReference = storageReference.child("products/" + UUID.randomUUID().toString());

        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();

                                Map<String, Object> product = new HashMap<>();
                                product.put("id", productId);
                                product.put("name", productName);
                                product.put("description", productDescription);
                                product.put("price", productPrice);
                                product.put("imageUrl", imageUrl);

                                db.collection("seafood").document(productId)
                                        .set(product)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(AddProductActivity.this, "Producto añadido exitosamente", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(AddProductActivity.this, "Error al añadir el producto: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProductActivity.this, "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}