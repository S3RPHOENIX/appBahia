package com.example.bahia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SeafoodAdapterUser extends RecyclerView.Adapter<SeafoodAdapterUser.ViewHolder> {

    private List<Seafood> seafoodList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Seafood item);
    }

    public SeafoodAdapterUser(List<Seafood> seafoodList, OnItemClickListener listener) {
        this.seafoodList = seafoodList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seafood_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(seafoodList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return seafoodList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView descriptionTextView;
        public TextView priceTextView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.seafood_item_name);
            descriptionTextView = itemView.findViewById(R.id.seafood_item_description);
            priceTextView = itemView.findViewById(R.id.seafood_item_price);
            imageView = itemView.findViewById(R.id.seafood_item_image);
        }

        public void bind(final Seafood seafood, final OnItemClickListener listener) {
            nameTextView.setText(seafood.getName());
            descriptionTextView.setText(seafood.getDescription());
            priceTextView.setText(String.format("$%.2f", seafood.getPrice()));

            Glide.with(itemView.getContext())
                    .load(seafood.getImageUrl())
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(imageView);

            itemView.setOnClickListener(v -> listener.onItemClick(seafood));
        }
    }
}