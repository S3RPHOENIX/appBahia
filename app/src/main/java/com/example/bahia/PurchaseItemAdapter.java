package com.example.bahia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PurchaseItemAdapter extends RecyclerView.Adapter<PurchaseItemAdapter.PurchaseItemViewHolder> {

    private List<Seafood> itemsList;

    public PurchaseItemAdapter(List<Seafood> itemsList) {
        this.itemsList = itemsList != null ? itemsList : new ArrayList<>(); // Ensure itemsList is not null
    }

    @NonNull
    @Override
    public PurchaseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_detail, parent, false);
        return new PurchaseItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseItemViewHolder holder, int position) {
        Seafood item = itemsList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class PurchaseItemViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView descriptionTextView;
        private TextView priceTextView;
        private TextView quantityTextView;
        private ImageView imageView;

        public PurchaseItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_name);
            descriptionTextView = itemView.findViewById(R.id.item_description);
            priceTextView = itemView.findViewById(R.id.item_price);
            quantityTextView = itemView.findViewById(R.id.item_quantity);
            imageView = itemView.findViewById(R.id.item_image);
        }

        public void bind(Seafood item) {
            nameTextView.setText(item.getName());
            descriptionTextView.setText(item.getDescription());
            priceTextView.setText(String.format("$%.2f", item.getPrice()));
            quantityTextView.setText(String.valueOf(item.getQuantity()));
            imageView.setImageResource(item.getImageResId());
        }
    }
}