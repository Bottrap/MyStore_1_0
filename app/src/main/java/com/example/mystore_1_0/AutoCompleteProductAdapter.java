package com.example.mystore_1_0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.mystore_1_0.Prodotto.Prodotto;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteProductAdapter extends ArrayAdapter<Prodotto> {
    private List<Prodotto> fullProductList;

    public AutoCompleteProductAdapter(@NonNull Context context, @NonNull List<Prodotto> productList) {
        super(context, 0, productList);
        fullProductList = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return productFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_autocomplete_row, parent, false);
        }

        MaterialTextView productName = convertView.findViewById(R.id.productName);
        MaterialTextView productCode = convertView.findViewById(R.id.productCode);
        MaterialTextView productPrice = convertView.findViewById(R.id.productPrice);
        ImageView productImg = convertView.findViewById(R.id.productImg);

        Prodotto prodotto = getItem(position);

        if(prodotto != null){
            productName.setText(prodotto.getNome());
            productCode.setText(prodotto.getCodice());
            productPrice.setText(String.valueOf(prodotto.getPrezzo()));
            Glide.with(productImg).load(prodotto.getURLImmagine()).into(productImg);
        }


        return convertView;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Prodotto> suggestions = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                suggestions.addAll(fullProductList);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Prodotto prodotto : fullProductList){
                    if(prodotto.getNome().toLowerCase().contains(filterPattern)){
                        suggestions.add(prodotto);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List)results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Prodotto) resultValue).getNome();
        }



    };
}
