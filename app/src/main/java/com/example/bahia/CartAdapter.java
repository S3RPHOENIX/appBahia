package com.example.bahia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Seafood> cartList;
    private OnQuantityChangeListener onQuantityChangeListener;
    private OnItemClickListener onItemClickListener;

    public CartAdapter(List<Seafood> cartList, OnQuantityChangeListener onQuantityChangeListener, OnItemClickListener onItemClickListener) {
        this.cartList = cartList;
        this.onQuantityChangeListener = onQuantityChangeListener;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Seafood item = cartList.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void removeItem(int position) {
        cartList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartList.size());
        onQuantityChangeListener.onQuantityChange();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView descriptionTextView;
        private TextView priceTextView;
        private TextView quantityTextView;
        private ImageView imageView;
        private ImageView removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cart_item_name);
            descriptionTextView = itemView.findViewById(R.id.cart_item_description);
            priceTextView = itemView.findViewById(R.id.cart_item_price);
            quantityTextView = itemView.findViewById(R.id.cart_item_quantity);
            imageView = itemView.findViewById(R.id.cart_item_image);
            removeButton = itemView.findViewById(R.id.delete_button);
        }

        public void bind(Seafood item, int position) {
            nameTextView.setText(item.getName());
            descriptionTextView.setText(item.getDescription());
            priceTextView.setText(String.format("$%.2f", item.getPrice()));
            quantityTextView.setText(String.valueOf(item.getQuantity()));

            if (item.getImageResId() == 0) {
                imageView.setImageResource(R.drawable.default_image);
            } else {
                imageView.setImageResource(item.getImageResId());
            }

            removeButton.setOnClickListener(v -> onItemClickListener.onItemClick(item));
        }
    }

    public interface OnQuantityChangeListener {
        void onQuantityChange();
    }

    public interface OnItemClickListener {
        void onItemClick(Seafood item);
    }
}