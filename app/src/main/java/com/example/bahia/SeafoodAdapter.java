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

public class SeafoodAdapter extends RecyclerView.Adapter<SeafoodAdapter.SeafoodViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Seafood item);
        void onDeleteClick(Seafood item);
    }

    private List<Seafood> seafoodList;
    private OnItemClickListener listener;

    public SeafoodAdapter(List<Seafood> seafoodList, OnItemClickListener listener) {
        this.seafoodList = seafoodList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SeafoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seafood_admin, parent, false);
        return new SeafoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeafoodViewHolder holder, int position) {
        holder.bind(seafoodList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return seafoodList.size();
    }

    public static class SeafoodViewHolder extends RecyclerView.ViewHolder {
        public TextView idTextView;
        public TextView nameTextView;
        public TextView descriptionTextView;
        public TextView priceTextView;
        public ImageView imageView;
        public ImageView deleteButton;

        public SeafoodViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.seafood_item_id);
            nameTextView = itemView.findViewById(R.id.seafood_item_name);
            descriptionTextView = itemView.findViewById(R.id.seafood_item_description);
            priceTextView = itemView.findViewById(R.id.seafood_item_price);
            imageView = itemView.findViewById(R.id.seafood_item_image);
            deleteButton = itemView.findViewById(R.id.btn_delete_product);
        }

        public void bind(final Seafood seafood, final OnItemClickListener listener) {
            idTextView.setText("ID: " + seafood.getId());
            nameTextView.setText(seafood.getName());
            descriptionTextView.setText(seafood.getDescription());
            priceTextView.setText(String.format("$%.2f", seafood.getPrice()));
            Glide.with(itemView.getContext()).load(seafood.getImageUrl()).into(imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(seafood);
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteClick(seafood);
                }
            });
        }
    }
}