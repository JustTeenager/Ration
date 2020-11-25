package com.ration.qcode.application.MainPack.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ration.qcode.application.MainPack.adapter.AnalyzesListAdapter;
import com.ration.qcode.application.MainPack.dialog.AddAnalysisDialog;
import com.ration.qcode.application.ProductDataBase.DataBaseHelper;
import com.ration.qcode.application.R;
import com.ration.qcode.application.utils.AdapterUpdatable;
import com.ration.qcode.application.utils.SwipeDetector;

import java.util.ArrayList;

/**
 * Created by deepdev on 05.04.17.
 */

public class AnalyzesListFragment extends Fragment implements AdapterView.OnItemClickListener,
        AddAnalysisDialog.OnReceivedData, AdapterUpdatable {

    private ArrayList<String> analyzes;
    private ArrayList<String> timeDate;
    private ArrayList<String> notices;
    private AnalyzesListAdapter adapter;
    private ListView listMain;
    ArrayList<String> all;
    private SwipeDetector swipeDetector;
    DataBaseHelper db;
    private Fragment mFragment;
    String[] Date;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_list_frament, container, false);
        db = DataBaseHelper.getInstance(getActivity());
        mFragment = this;

        setListData();

        listMain = (ListView) view.findViewById(R.id.listMain);
        adapter = new AnalyzesListAdapter(getActivity(), R.layout.analyzes_list_item,
                analyzes, notices, timeDate);
        listMain.setAdapter(adapter);
        listMain.setOnItemClickListener(this);
        swipeDetector = new SwipeDetector();
        listMain.setOnTouchListener(swipeDetector);
        /*-----------------------------------------------------------*/


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAnalysisDialog dialog = new AddAnalysisDialog();
                FragmentManager fragmentManager = getFragmentManager();
                dialog.setTargetFragment(mFragment, 0);
                dialog.show(fragmentManager, "dialog");
            }
        });
        return view;
    }

    private void setListData() {
        analyzes = new ArrayList<>();
        notices = new ArrayList<>();
        timeDate = new ArrayList<>();

        all = db.getAnalizes();
        Date = new String[all.size()];
        for (int i = 0; i < all.size(); i++) {

            String[] oneAll = all.get(i).split("\\s+");
            Date[i] = oneAll[1];
            analyzes.add(getString(R.string.fa) + " = " + oneAll[0]);
            timeDate.add(oneAll[1]);
            try {
                notices.add(oneAll[2]);
            } catch (ArrayIndexOutOfBoundsException e) {
                notices.add("");
            }
        }
    }


    private void deleteFromList(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.delete_question))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        analyzes.remove(position);
                        timeDate.remove(position);
                        notices.remove(position);

                        db.deleteAnalize(Date[position]);

                        adapter = new AnalyzesListAdapter(getActivity(), R.layout.analyzes_list_item,
                                analyzes, notices, timeDate);
                        listMain.setAdapter(adapter);
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (swipeDetector.swipeDetected()) {
            if (swipeDetector.getAction() == SwipeDetector.Action.LR) {
                deleteFromList(i);
            }
        }
    }

    @Override
    public void onDataReceived(String date, String faLevel, String notice) {
        analyzes.add(getString(R.string.fa) + " = " + faLevel);
        timeDate.add(date);
        notices.add(notice);

        adapter = new AnalyzesListAdapter(getActivity(), R.layout.analyzes_list_item,
                analyzes, notices, timeDate);
        listMain.setAdapter(adapter);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    @Override
    public void updateAdapter() {

    }
}