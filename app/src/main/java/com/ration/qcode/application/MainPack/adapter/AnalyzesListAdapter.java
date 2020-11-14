package com.ration.qcode.application.MainPack.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ration.qcode.application.R;

import java.util.ArrayList;

/**
 * Created by deepdev on 05.04.17.
 */

public class AnalyzesListAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> analyze;
    private ArrayList<String> notice;
    private ArrayList<String> timeDate;


    public AnalyzesListAdapter(Context context, int resource, ArrayList<String> analyze,
                               ArrayList<String> notice, ArrayList<String> timeDate) {
        super(context, resource, analyze);
        this.context = context;
        this.analyze = analyze;
        this.notice = notice;
        this.timeDate = timeDate;

    }

    @Override
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.analyzes_list_item, null);
        }

        TextView textAnalyze = (TextView) convertView.findViewById(R.id.textAnalyze);
        TextView textDate = (TextView) convertView.findViewById(R.id.textDate);
        TextView textNotices = (TextView) convertView.findViewById(R.id.textNotices);

        textAnalyze.setText(analyze.get(position));
        textDate.setText(timeDate.get(position));
        textNotices.setText(notice.get(position));


        return convertView;
    }
}
