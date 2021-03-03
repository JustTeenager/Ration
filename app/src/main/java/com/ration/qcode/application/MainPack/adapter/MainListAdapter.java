package com.ration.qcode.application.MainPack.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ration.qcode.application.MainPack.activity.ProductInfoActivity;
import com.ration.qcode.application.ProductDataBase.DataBaseHelper;
import com.ration.qcode.application.R;
import com.ration.qcode.application.utils.NetworkService;
import com.ration.qcode.application.utils.SharedPrefManager;
import com.ration.qcode.application.utils.SwipeDetector;
import com.ration.qcode.application.utils.internet.RemoveFromMenuAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ration.qcode.application.utils.Constants.DATE;
import static com.ration.qcode.application.utils.Constants.INFO;
import static com.ration.qcode.application.utils.Constants.MENU;

public class MainListAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> faAll;
    private List<String> timeDate;
    private List<String> proteinsAll;
    private List<String> kls;
    private List<ArrayList<String>> eatingType;
    private List<ArrayList<String>> fas;
    private List<ArrayList<String>> bel;
    private List<ArrayList<String>> kl;

    DataBaseHelper db;

    private SwipeDetector swipeDetector;
    private MainListInnerAdapter adapter;
    private ListView listView;
    private int pos;
    String date;


    public MainListAdapter(Context context, int resource, List<String> faAll,
                           List<String> timeDate, List<String> proteinsAll, List<String> kls,
                           List<ArrayList<String>> eatingType, List<ArrayList<String>> fas,
                           List<ArrayList<String>> bel, List<ArrayList<String>> kl) {
        super(context, resource, faAll);
        this.mContext = context;
        this.faAll = faAll;
        this.timeDate = timeDate;
        this.fas = fas;
        this.bel = bel;
        this.kl = kl;
        this.eatingType = eatingType;
        this.kls = kls;
        this.proteinsAll = proteinsAll;
        swipeDetector = new SwipeDetector();
    }

    @Override
    public View getView(final int position, View convertView,final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.main_list_item, null);
            db = DataBaseHelper.getInstance(mContext);
        }

        listView = (ListView) convertView.findViewById(R.id.listMainInner);
        adapter = new MainListInnerAdapter(mContext, R.layout.main_ls_inner_item,
                eatingType.get(position), fas.get(position), bel.get(position), kl.get(position));
        listView.setOnTouchListener(swipeDetector);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == SwipeDetector.Action.LR) {
                        pos = position;
                        deleteFromList(i);
                    }
                } else {
                    pos = position;

                    addtoList();

                    Intent intent = new Intent(mContext, ProductInfoActivity.class);
                    intent.putExtra(INFO, "list");
                    intent.putExtra(MENU, eatingType.get(pos).get(i));
                    intent.putExtra(DATE, date);
                    intent.putExtra("From menu", "menu");
                    mContext.startActivity(intent);
                }
            }
        });

        listView.setAdapter(adapter);

        TextView textEatingType = (TextView) convertView.findViewById(R.id.textFAAll);
        TextView textDate = (TextView) convertView.findViewById(R.id.textDate);
        TextView textProteinsAll = (TextView) convertView.findViewById(R.id.textProteinsAll);
        TextView textKls = (TextView) convertView.findViewById(R.id.textKLAll);

        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) listView.getLayoutParams();

        final float scale = getContext().getResources().getDisplayMetrics().density;
        lp.height = (fas.get(position).size() * ((int) (35 * scale + 0.5f)));
        listView.setLayoutParams(lp);

        textEatingType.setText(faAll.get(position));

        textDate.setText(timeDate.get(position));

        textProteinsAll.setText(proteinsAll.get(position));
        textKls.setText(kls.get(position));


        return convertView;
    }

    private void deleteFromList(final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(mContext.getString(R.string.delete_question))
                .setCancelable(false)
                .setPositiveButton(mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        date = timeDate.get(pos);
                        db.deleteFromMenu(date, eatingType.get(pos).get(i),mContext);
                        removeFromHostingMenu(i);
                        eatingType.get(pos).remove(i);
                        fas.get(pos).remove(i);

                        if (fas.get(pos).isEmpty() && eatingType.get(pos).isEmpty()) {
                            faAll.remove(pos);
                            timeDate.remove(pos);
                            eatingType.remove(pos);
                            fas.remove(pos);
                            bel.remove(pos);
                            kl.remove(pos);
                        }
                        Log.d("tut_list", bel.toString());
                        Log.d("tut_list", kl.toString());
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(mContext.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void removeFromHostingMenu(int i) {
        RemoveFromMenuAPI removeFromMenuAPI = NetworkService.getInstance(SharedPrefManager.getManager(mContext).getUrl()).getApi(RemoveFromMenuAPI.class);
        Call<String> call= removeFromMenuAPI.removeFromMenu(eatingType.get(pos).get(i),date);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void addtoList() {
        date = timeDate.get(pos);
        notifyDataSetChanged();
    }
}