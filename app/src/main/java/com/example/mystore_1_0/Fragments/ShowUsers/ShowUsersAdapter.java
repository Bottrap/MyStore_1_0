package com.example.mystore_1_0.Fragments.ShowUsers;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class ShowUsersAdapter extends RecyclerView.Adapter<ShowUsersAdapter.UserVH> {
    private static final String TAG = "UserAdapter";
    List<Utente> users;

    public ShowUsersAdapter(List<Utente> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.showusers_row, parent, false);
        return new UserVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserVH holder, int position) {
        Utente utente = users.get(position);

        holder.titleTextView.setText(Html.fromHtml("<b>Id: " + utente.getId() + "</b>"));
        holder.nome.setText(Html.fromHtml("<b><u>Nome:</u></b><i> " + utente.getNome() + "</i>"));
        holder.cognome.setText(Html.fromHtml("<b><u>Cognome:</u></b><i> " + utente.getCognome() + "</i>"));
        holder.dataNascita.setText(Html.fromHtml("<i>" + utente.getDataNascita() + "</i>"));
        holder.permessi.setText(Html.fromHtml("<b><u>Permessi:</u></b><i> " + utente.getPermessi() + "</i>"));
        holder.password.setText(Html.fromHtml("<b><u>Passw:</u></b><i> " + utente.getPassword() + "</i>"));
        holder.telefono.setText(Html.fromHtml("<b><u>Cell:</u></b><i> " + utente.getTelefono() + "</i>"));

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
