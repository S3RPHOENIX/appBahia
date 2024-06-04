package com.example.bahia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView emailTextView;
        public TextView roleTextView;
        public TextView addressesTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.user_name);
            emailTextView = itemView.findViewById(R.id.user_email);
            roleTextView = itemView.findViewById(R.id.user_role);
            addressesTextView = itemView.findViewById(R.id.user_addresses);
        }

        public void bind(User user) {
            nameTextView.setText("Nombre: " + user.getName());
            emailTextView.setText("Correo: " + user.getEmail());
            roleTextView.setText("Rol: " + user.getRole());
            addressesTextView.setText("Direcciones: " + formatAddresses(user.getAddresses()));
        }

        private String formatAddresses(List<Address> addresses) {
            if (addresses == null || addresses.isEmpty()) {
                return "No addresses";
            }
            StringBuilder formattedAddresses = new StringBuilder();
            for (Address address : addresses) {
                formattedAddresses.append("ID: ").append(address.getId())
                        .append(", Ciudad: ").append(address.getCity())
                        .append(", Código postal: ").append(address.getPostalCode())
                        .append(", Dirección: ").append(address.getAddress())
                        .append(", Estado: ").append(address.getState())
                        .append(", Nombre: ").append(address.getName())
                        .append(", Teléfono: ").append(address.getPhone())
                        .append("\n");
            }
            return formattedAddresses.toString();
        }
    }
}