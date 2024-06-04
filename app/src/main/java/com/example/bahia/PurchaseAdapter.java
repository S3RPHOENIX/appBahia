package com.example.bahia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {

    private List<Purchase> purchasesList;
    private static final double DELIVERY_CHARGE = 50.0; // Define la constante aquí

    public PurchaseAdapter(List<Purchase> purchasesList) {
        this.purchasesList = purchasesList;
    }

    @NonNull
    @Override
    public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase, parent, false);
        return new PurchaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int position) {
        Purchase purchase = purchasesList.get(position);
        holder.bind(purchase);
    }

    @Override
    public int getItemCount() {
        return purchasesList.size();
    }

    public static class PurchaseViewHolder extends RecyclerView.ViewHolder {

        private TextView addressTextView;
        private TextView paymentMethodTextView;
        private TextView itemsTextView;
        private TextView deliveryChargeTextView;
        private TextView totalPriceTextView;
        private TextView timestampTextView;

        public PurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            addressTextView = itemView.findViewById(R.id.purchase_address);
            paymentMethodTextView = itemView.findViewById(R.id.purchase_payment_method);
            itemsTextView = itemView.findViewById(R.id.purchase_items);
            deliveryChargeTextView = itemView.findViewById(R.id.purchase_delivery_charge);
            totalPriceTextView = itemView.findViewById(R.id.purchase_total_price);
            timestampTextView = itemView.findViewById(R.id.purchase_timestamp);
        }

        public void bind(Purchase purchase) {
            addressTextView.setText("Address: " + purchase.getAddressId());
            paymentMethodTextView.setText("Metodo de pago: " + purchase.getPaymentMethod());
            itemsTextView.setText("Productos: " + formatItems(purchase.getItems()));
            deliveryChargeTextView.setText(String.format("Cargo por envio: $%.2f", DELIVERY_CHARGE));
            totalPriceTextView.setText(String.format("Total: $%.2f", calculateTotalAmount(purchase) + DELIVERY_CHARGE));
            timestampTextView.setText("Fecha y hora: " + formatTimestamp(purchase.getTimestamp()));
        }

        private String formatItems(List<Seafood> items) {
            StringBuilder formattedItems = new StringBuilder();
            for (Seafood item : items) {
                formattedItems.append(item.getName())
                        .append(" (x")
                        .append(item.getQuantity())
                        .append("), ");
            }
            // Eliminar la última coma y espacio
            if (formattedItems.length() > 0) {
                formattedItems.setLength(formattedItems.length() - 2);
            }
            return formattedItems.toString();
        }

        private double calculateTotalAmount(Purchase purchase) {
            double total = 0.0;
            for (Seafood item : purchase.getItems()) {
                total += item.getPrice() * item.getQuantity();
            }
            return total;
        }

        private String formatTimestamp(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(timestamp);
        }
    }
}