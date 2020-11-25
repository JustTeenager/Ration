package com.ration.qcode.application.MainPack.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ration.qcode.application.MainPack.activity.ProductInfoActivity;
import com.ration.qcode.application.MainPack.adapter.MainListAdapter;
import com.ration.qcode.application.ProductDataBase.DataBaseHelper;
import com.ration.qcode.application.R;
import com.ration.qcode.application.utils.AdapterUpdatable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainListFragment extends Fragment implements AdapterUpdatable {

    DataBaseHelper DB;
    private static final int TYPE_ITEM = 0;

    private ListView listMain;
    private List<String> faAll;
    private List<String> proteinsAll;
    private List<String> KLAll;
    private List<String> timeDate;
    private List<ArrayList<String>> eatingTypeList;
    private List<ArrayList<String>> fasList;
    private List<ArrayList<String>> bel;
    private List<ArrayList<String>> kl;
    private ArrayList<String> eatingType;
    private ArrayList<String> fas;
    private ArrayList<String> belas;
    private ArrayList<String> kls;
    private ArrayList<String> sortB;

    private MainListAdapter mainAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_list_frament, container, false);


        listMain = (ListView) view.findViewById(R.id.listMain);
        updateAdapter();



        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ProductInfoActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }


    private void setListData() {
        faAll = new ArrayList<>();
        timeDate = new ArrayList<>();
        eatingType = new ArrayList<>();
        proteinsAll = new ArrayList<>();
        eatingTypeList = new ArrayList<>();
        fasList = new ArrayList<>();
        bel = new ArrayList<>();
        kl = new ArrayList<>();
        fas = new ArrayList<>();
        belas = new ArrayList<>();
        kls = new ArrayList<>();
        KLAll = new ArrayList<>();
        setMode();
    }

    public void setMode() {

        ArrayList<String> dateslist;
        ArrayList<String> datesmenulistLAST;
        dateslist = DB.getDates();
        String Split[];
        double FA = 0, FAall = 0, BeAll = 0, Belki = 0, KL = 0, KLaLL = 0;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        DecimalFormat decimalFormatKL = new DecimalFormat("#");

        for (int i = 0; i < dateslist.size(); i++) {
            timeDate.add(dateslist.get(i));
            int sizemenu = DB.getMenues(dateslist.get(i)).size(); // размер
            datesmenulistLAST = DB.getMenues(dateslist.get(i));

            for (int j = 0; j < sizemenu; j++) {

                sortB = (DB.getMenu1screen(dateslist.get(i), datesmenulistLAST.get(j)));

                for (int c = 0; c < sortB.size(); c++) {

                    Split = sortB.get(c).split("\\s+");

                    Belki += Double.parseDouble(Split[1]);
                    FA += Double.parseDouble(Split[0]);
                    KL += Double.parseDouble(Split[2]);
                }
                KLaLL += KL;
                BeAll += Belki;
                FAall += FA;

                eatingType.add(datesmenulistLAST.get(j));
                fas.add("" + decimalFormat.format(FA));
                belas.add("" + decimalFormat.format(Belki));
                kls.add("" + decimalFormatKL.format(KL));

                KL = 0;
                FA = 0;
                Belki = 0;
            }

            faAll.add(getString(R.string.fa) + ": " + decimalFormat.format(FAall));
            proteinsAll.add(getString(R.string.proteins) + ": " + decimalFormat.format(BeAll));
            KLAll.add(getString(R.string.kl) + ": " + decimalFormat.format(KLaLL));

            eatingTypeList.add(eatingType);
            fasList.add(fas);
            bel.add(belas);
            kl.add(kls);

            eatingType = new ArrayList<>();
            fas = new ArrayList<>();
            belas = new ArrayList<>();
            kls = new ArrayList<>();

            FA = 0;
            FAall = 0;
            KLaLL = 0;
            KL = 0;
            Belki = 0;
            BeAll = 0;
        }
    }

    public void updateAdapter(){
        DB = DataBaseHelper.getInstance(getActivity());
        setListData();
        mainAdapter = new MainListAdapter(getActivity(),
                R.layout.main_list_item, faAll, timeDate, proteinsAll, KLAll, eatingTypeList, fasList,
                bel, kl);
        listMain.setAdapter(mainAdapter);
    }

}