package com.ration.qcode.application.MainPack.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ration.qcode.application.MainPack.adapter.ProductsInfoListAdapter;
import com.ration.qcode.application.ProductDataBase.DataBaseHelper;
import com.ration.qcode.application.R;

import java.util.ArrayList;

import static com.ration.qcode.application.utils.Constants.CARBOHYDRATES;
import static com.ration.qcode.application.utils.Constants.DATE;
import static com.ration.qcode.application.utils.Constants.FA;
import static com.ration.qcode.application.utils.Constants.FATS;
import static com.ration.qcode.application.utils.Constants.GR;
import static com.ration.qcode.application.utils.Constants.KL;
import static com.ration.qcode.application.utils.Constants.MENU;
import static com.ration.qcode.application.utils.Constants.PRODUCTS;
import static com.ration.qcode.application.utils.Constants.PROD_SEARCH;
import static com.ration.qcode.application.utils.Constants.PROTEINS;

/**
 * Created by deepdev on 10.04.17.
 */

public class ProductsListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<String> getall;
    private ListView listViewProducts;
    DataBaseHelper dataBaseHelper;
    private ArrayList<String> products;
    private ArrayList<String> proteins;
    private ArrayList<String> fats;
    private ArrayList<String> carbohydrates;
    private ArrayList<String> fas;
    private ArrayList<String> kl;
    private ArrayList<String> gr;

    private String[] masproducts;
    private String[] parse1;
    private String[] parse2;
    String date, menu;
    Intent intentF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_activity);
        Log.e("PRODUCT_LIST_ACTIVITY", ProductsListActivity.class.toString());
        dataBaseHelper = DataBaseHelper.getInstance(getApplicationContext());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        intentF = getIntent();

        initList();
        getall = dataBaseHelper.getALLProduct(intentF.getStringExtra(PROD_SEARCH));
        for (int i = 0; i < getall.size(); i++) {
            parse1 = new String[getall.size()];
            parse1 = getall.get(i).split("\\|");

            parse2 = new String[getall.size() - 1];
            parse2 = parse1[1].split("\\s+");
            Log.e("parse1", parse1[0] + " " + parse2[0] + " " + parse2[1] + "");
            setListData(parse1[0], parse2[2], parse2[1], parse2[3], parse2[4], parse2[5], parse2[6]);
        }
        listViewProducts = (ListView) findViewById(R.id.listProducts);
        listViewProducts.setOnItemClickListener(this);

        setAdapterList();

    }

    private void initList() {
        getall = new ArrayList<>();

        products = new ArrayList<>();
        proteins = new ArrayList<>();
        fats = new ArrayList<>();
        carbohydrates = new ArrayList<>();
        fas = new ArrayList<>();
        kl = new ArrayList<>();
        gr = new ArrayList<>();

        masproducts = new String[getall.size()];
    }

    private void setListData(String products, String proteins, String fats, String carbohydrates,
                             String fas, String kl, String gr) {
        this.products.add(products);
        this.proteins.add(proteins);
        this.fats.add(fats);
        this.carbohydrates.add(carbohydrates);
        this.fas.add(fas);
        this.kl.add(kl);
        this.gr.add(gr);
    }

    private void setAdapterList() {
        ProductsInfoListAdapter adapter = new ProductsInfoListAdapter(this, R.layout.productsinfo_list_item,
                products, proteins, fats, carbohydrates, fas, kl, gr);
        listViewProducts.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, AddProductActivity.class);
        if (intentF.getStringExtra("From menu") != null) {
            this.date = intentF.getStringExtra(DATE);
            this.menu = intentF.getStringExtra(MENU);
            intent.putExtra("From menu", "MENU");
            intent.putExtra(MENU, menu);
            intent.putExtra(DATE, date);
            Log.e("ListA", date + " " + menu);
        }
        intent.putExtra(PRODUCTS, products.get(i));
        intent.putExtra(PROTEINS, proteins.get(i));
        intent.putExtra(FATS, fats.get(i));
        intent.putExtra(CARBOHYDRATES, carbohydrates.get(i));
        intent.putExtra(FA, fas.get(i));
        intent.putExtra(KL, kl.get(i));
        intent.putExtra(GR, gr.get(i));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
