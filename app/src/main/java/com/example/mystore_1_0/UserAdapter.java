package com.example.mystore_1_0;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserVH> {
    private static final String TAG = "UserAdapter";
    List<Utente> users;

    public UserAdapter(List<Utente> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_row, parent, false);
        return new UserVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserVH holder, int position) {
        Utente utente = users.get(position);

        holder.titleTextView.setText("Id: " + utente.getId());
        holder.nome.setText("Nome: " + utente.getNome());
        holder.cognome.setText("Cognome: " + utente.getCognome());
        holder.dataNascita.setText(utente.getDataNascita());
        holder.permessi.setText("Permessi: " + utente.getPermessi());
        holder.password.setText("Passw: " + utente.getPassword());
        holder.telefono.setText("Cell: " + utente.getTelefono());

        boolean isExpanded = users.get(position).isExpanded();
        if (isExpanded) holder.layoutEspandibile.setVisibility(View.VISIBLE);
        else holder.layoutEspandibile.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserVH extends RecyclerView.ViewHolder{
        private static final String TAG = "UserVH";

        MaterialTextView titleTextView, nome, cognome, telefono, permessi, password, dataNascita;

        ConstraintLayout layoutEspandibile;

        public UserVH(@NonNull View itemView) {
            super(itemView);

            layoutEspandibile = itemView.findViewById(R.id.layoutEspandibile);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            nome = itemView.findViewById(R.id.show_nome);
            cognome = itemView.findViewById(R.id.show_cognome);
            password = itemView.findViewById(R.id.show_password);
            telefono = itemView.findViewById(R.id.show_telefono);
            dataNascita = itemView.findViewById(R.id.show_dataNascita);
            permessi = itemView.findViewById(R.id.show_permessi);

            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utente utente = users.get(getAdapterPosition());
                    utente.setExpanded(!utente.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
