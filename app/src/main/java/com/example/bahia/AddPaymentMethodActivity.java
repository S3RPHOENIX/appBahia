package com.example.bahia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddPaymentMethodActivity extends AppCompatActivity {

    private EditText editTextCardNumber, editTextExpirationDate, editTextCvv;
    private Button buttonSaveCard;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String addressId;
    private ArrayList<Seafood> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_method);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextCardNumber = findViewById(R.id.edit_text_card_number);
        editTextExpirationDate = findViewById(R.id.edit_text_expiration_date);
        editTextCvv = findViewById(R.id.edit_text_cvv);
        buttonSaveCard = findViewById(R.id.button_save_card);

        addressId = getIntent().getStringExtra("addressId");
        cartList = (ArrayList<Seafood>) getIntent().getSerializableExtra("cartList");

        buttonSaveCard.setOnClickListener(v -> {
            String cardNumber = editTextCardNumber.getText().toString().trim();
            String expirationDate = editTextExpirationDate.getText().toString().trim();
            String cvv = editTextCvv.getText().toString().trim();

            if (cardNumber.isEmpty() || expirationDate.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(AddPaymentMethodActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    try {
                        String encryptedCardNumber = EncryptionUtil.encrypt(cardNumber);
                        String encryptedExpirationDate = EncryptionUtil.encrypt(expirationDate);
                        String encryptedCvv = EncryptionUtil.encrypt(cvv);
                        saveCardToFirestore(userId, encryptedCardNumber, encryptedExpirationDate, encryptedCvv);
                    } catch (Exception e) {
                        Toast.makeText(AddPaymentMethodActivity.this, "Error al encriptar la información", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AddPaymentMethodActivity.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveCardToFirestore(String userId, String cardNumber, String expirationDate, String cvv) {
        Card newCard = new Card(cardNumber, expirationDate, cvv);
        db.collection("users")
                .document(userId)
                .collection("cards")
                .add(newCard)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddPaymentMethodActivity.this, "Tarjeta de crédito guardada exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddPaymentMethodActivity.this, PaymentOptionActivity.class);
                    intent.putExtra("addressId", addressId);
                    intent.putExtra("cartList", cartList);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddPaymentMethodActivity.this, "Error al guardar la tarjeta de crédito", Toast.LENGTH_SHORT).show();
                });
    }

    public static class Card {
        private String cardNumber;
        private String expirationDate;
        private String cvv;

        public Card() {
        }

        public Card(String cardNumber, String expirationDate, String cvv) {
            this.cardNumber = cardNumber;
            this.expirationDate = expirationDate;
            this.cvv = cvv;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(String expirationDate) {
            this.expirationDate = expirationDate;
        }

        public String getCvv() {
            return cvv;
        }

        public void setCvv(String cvv) {
            this.cvv = cvv;
        }
    }
}