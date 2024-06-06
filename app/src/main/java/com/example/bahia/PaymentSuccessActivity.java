package com.example.bahia;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
public class PaymentSuccessActivity extends AppCompatActivity {

    private String addressId;
    private String paymentMethod;
    private ArrayList<Seafood> cartList;
    private TextView textViewSuccessMessage;
    private Button buttonContinueShopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);


        addressId = getIntent().getStringExtra("addressId");
        paymentMethod = getIntent().getStringExtra("paymentMethod");
        cartList = (ArrayList<Seafood>) getIntent().getSerializableExtra("cartList");

        textViewSuccessMessage = findViewById(R.id.text_view_success_message);
        buttonContinueShopping = findViewById(R.id.button_continue_shopping);

        textViewSuccessMessage.setText("¡Su pedido ha sido tomado con éxito!");

        buttonContinueShopping.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentSuccessActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}