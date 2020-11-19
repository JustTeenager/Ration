package com.ration.qcode.application.MainPack.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ration.qcode.application.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by deepdev on 13.04.17.
 */

public class ProductsInfoListAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<String> products;
    private ArrayList<String> proteins;
    private ArrayList<String> fats;
    private ArrayList<String> carbohydrates;
    private ArrayList<String> fas;
    private ArrayList<String> kl;
    private ArrayList<String> gr;

    public ProductsInfoListAdapter(Context context, int resource, ArrayList<String> products,
                               ArrayList<String> proteins, ArrayList<String> fats,
                               ArrayList<String> carbohydrates, ArrayList<String> fas,
                                   ArrayList<String> kl, ArrayList<String> gr) {
        super(context, resource, products);
        this.context = context;
        this.products = products;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.fas = fas;
        this.kl = kl;
        this.gr = gr;
    }
    @Override
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.productsinfo_list_item, null);
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        TextView textViewProduct = (TextView) convertView.findViewById(R.id.textViewProduct);
        TextView textViewProteins = (TextView) convertView.findViewById(R.id.textViewProteins);
        TextView textViewFats = (TextView) convertView.findViewById(R.id.textViewFats);
        TextView textViewCarbohydrates = (TextView) convertView.findViewById(R.id.textViewCarbohydrates);
        TextView textViewFA = (TextView) convertView.findViewById(R.id.textViewFA);
        TextView textViewKL = (TextView) convertView.findViewById(R.id.textViewKg);
        TextView textViewGR = (TextView) convertView.findViewById(R.id.edit_text_gr);

        textViewProduct.setText(products.get(position));
        textViewProteins.setText(decimalFormat.format(Double.parseDouble(proteins.get(position).replace(",", "."))));
        textViewFats.setText(decimalFormat.format(Double.parseDouble(fats.get(position).replace(",", "."))));
        textViewCarbohydrates.setText(decimalFormat.format(Double.parseDouble(carbohydrates.get(position).replace(",", "."))));
        textViewFA.setText(decimalFormat.format(Double.parseDouble(fas.get(position).replace(",", "."))));
        textViewKL.setText(decimalFormat.format(Double.parseDouble(kl.get(position).replace(",", "."))));
        textViewGR.setText(decimalFormat.format(Double.parseDouble(gr.get(position).replace(",", "."))));

        return convertView;
    }
}