package com.example.bahia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PaymentOptionActivity extends AppCompatActivity {

    private RadioGroup paymentOptionsGroup;
    private Button buttonProceedToPayment;
    private String addressId;
    private ArrayList<Seafood> cartList;
    private static final double DELIVERY_CHARGE = 50.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);

        paymentOptionsGroup = findViewById(R.id.payment_options_group);
        buttonProceedToPayment = findViewById(R.id.button_proceed_to_payment);

        addressId = getIntent().getStringExtra("addressId");
        cartList = (ArrayList<Seafood>) getIntent().getSerializableExtra("cartList");

        buttonProceedToPayment.setOnClickListener(v -> {
            int selectedOptionId = paymentOptionsGroup.getCheckedRadioButtonId();
            if (selectedOptionId == -1) {
                Toast.makeText(PaymentOptionActivity.this, "Por favor, seleccione una opción de pago", Toast.LENGTH_SHORT).show();
            } else {
                RadioButton selectedOption = findViewById(selectedOptionId);
                String paymentMethod = selectedOption.getText().toString();
                if ("Pago contra entrega".equals(paymentMethod)) {
                    showCashOnDeliveryDialog();
                } else {
                    proceedToPayment(paymentMethod);
                }
            }
        });

        Button buttonAddPaymentMethod = findViewById(R.id.button_add_payment_method);
        buttonAddPaymentMethod.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentOptionActivity.this, AddPaymentMethodActivity.class);
            intent.putExtra("addressId", addressId);
            intent.putExtra("cartList", cartList);
            startActivity(intent);
        });
    }

    private void showCashOnDeliveryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pago contra entrega");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setMessage("El total a pagar es de $" + (getTotalPrice() + DELIVERY_CHARGE) + " (incluyendo $50 de envío). Con cuánto pagará?");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String amountGiven = input.getText().toString();
                if (!amountGiven.isEmpty()) {
                    proceedToPayment("Pago contra entrega");
                } else {
                    Toast.makeText(PaymentOptionActivity.this, "Por favor, ingrese la cantidad con la que pagará", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void proceedToPayment(String paymentMethod) {
        Intent intent = new Intent(PaymentOptionActivity.this, PaymentActivity.class);
        intent.putExtra("addressId", addressId);
        intent.putExtra("paymentMethod", paymentMethod);
        intent.putExtra("cartList", cartList);
        startActivity(intent);
        Toast.makeText(PaymentOptionActivity.this, "Método de pago seleccionado: " + paymentMethod, Toast.LENGTH_SHORT).show();
    }

    private double getTotalPrice() {
        double total = 0.0;
        for (Seafood item : cartList) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
}