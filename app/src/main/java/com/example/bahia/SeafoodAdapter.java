package com.example.bahia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SeafoodAdapter extends RecyclerView.Adapter<SeafoodAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Seafood item);
    }

    private List<Seafood> seafoodList;
    private OnItemClickListener listener;

    public SeafoodAdapter(List<Seafood> seafoodList, OnItemClickListener listener) {
        this.seafoodList = seafoodList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seafood, parent, false);
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
        public TextView idTextView;
        public TextView nameTextView;
        public TextView descriptionTextView;
        public TextView priceTextView;
        public TextView imageNameTextView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.seafood_item_id);
            nameTextView = itemView.findViewById(R.id.seafood_item_name);
            descriptionTextView = itemView.findViewById(R.id.seafood_item_description);
            priceTextView = itemView.findViewById(R.id.seafood_item_price);
            imageNameTextView = itemView.findViewById(R.id.seafood_item_image_name);
            imageView = itemView.findViewById(R.id.seafood_item_image);
        }

        public void bind(final Seafood seafood, final OnItemClickListener listener) {
            idTextView.setText("ID: " + seafood.getId());
            nameTextView.setText("Name: " + seafood.getName());
            descriptionTextView.setText("Description: " + seafood.getDescription());
            priceTextView.setText(String.format("Price: $%.2f", seafood.getPrice()));
            imageNameTextView.setText("Image Name: " + seafood.getImageResId());
            imageView.setImageResource(seafood.getImageResId());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(seafood);
                }
            });
        }
    }
}