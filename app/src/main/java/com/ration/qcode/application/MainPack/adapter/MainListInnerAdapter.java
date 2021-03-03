package com.ration.qcode.application.MainPack.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ration.qcode.application.R;

import java.util.List;

/**
 * Created by deepdev on 06.04.17.
 */

public class MainListInnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> eatingType;
    private List<String> fas;
    private List<String> bel;
    private List<String> kl;


    public MainListInnerAdapter(Context context, int resource, List<String> eatingType,
                                List<String> fas, List<String> bel, List<String> kl) {
        super(context, resource, eatingType);
        this.context = context;
        this.eatingType = eatingType;
        this.fas = fas;
        this.bel = bel;
        this.kl = kl;


    }


    @Override
    public View getView(int position, View convertView, @NonNull final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.main_ls_inner_item, null);
        }

        TextView textEatingType = (TextView) convertView.findViewById(R.id.textEatingType);
        TextView textFA = (TextView) convertView.findViewById(R.id.textFA);
        TextView textBel = (TextView) convertView.findViewById(R.id.textBel);
        TextView textKl = (TextView) convertView.findViewById(R.id.textKl);
        try {
            textEatingType.setText(eatingType.get(position));
            textFA.setText(fas.get(position));
            textBel.setText(bel.get(position));
            textKl.setText(kl.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}