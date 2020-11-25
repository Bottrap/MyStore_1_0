package com.example.mystore_1_0;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

//import java.util.ArrayList;
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

        holder.titleTextView.setText(utente.getId());
        holder.nome.setText(utente.getNome());

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

        MaterialTextView titleTextView, nome;

        ConstraintLayout layoutEspandibile;

        public UserVH(@NonNull View itemView) {
            super(itemView);

            layoutEspandibile = itemView.findViewById(R.id.layoutEspandibile);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            nome = itemView.findViewById(R.id.show_nome);

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
