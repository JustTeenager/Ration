package com.ration.qcode.application.MainPack.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ration.qcode.application.MainPack.adapter.ProductsInfoListAdapter;
import com.ration.qcode.application.ProductDataBase.DataBaseHelper;
import com.ration.qcode.application.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ration.qcode.application.utils.Constants.CARBOHYDRATES;
import static com.ration.qcode.application.utils.Constants.COMPLICATED;
import static com.ration.qcode.application.utils.Constants.DATE;
import static com.ration.qcode.application.utils.Constants.FA;
import static com.ration.qcode.application.utils.Constants.FATS;
import static com.ration.qcode.application.utils.Constants.GR;
import static com.ration.qcode.application.utils.Constants.ID_PRODUCT;
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

    private ArrayList<String> isComplicated;
    private List<String> parse1;
    private List<String> parse2;
    String date, menu;
    Intent intentF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_activity);
        dataBaseHelper = DataBaseHelper.getInstance(getApplicationContext());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        intentF = getIntent();

        initList();
        getall = dataBaseHelper.getALLProduct(intentF.getStringExtra(PROD_SEARCH));
        parse1 = new ArrayList<>();
        parse2 = new ArrayList<>();
        for (int i = 0; i < getall.size(); i++) {
            parse1 = Arrays.asList(getall.get(i).split("\\|"));

            parse2 = Arrays.asList(parse1.get(1).split("\\s+"));
            setListData(parse1.get(0), parse2.get(2), parse2.get(1), parse2.get(3), parse2.get(4), parse2.get(5), parse2.get(6),parse2.get(7));
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

        isComplicated = new ArrayList<>();
    }

    private void setListData(String products, String proteins, String fats, String carbohydrates,
                             String fas, String kl, String gr,String complicated) {
        this.products.add(products);
        this.proteins.add(proteins);
        this.fats.add(fats);
        this.carbohydrates.add(carbohydrates);
        this.fas.add(fas);
        this.kl.add(kl);
        this.gr.add(gr);
        this.isComplicated.add(complicated);
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
        Intent intent;
        if (getIntent().getStringExtra(SearchComplicatedProductActivity.PROD_SEARCH_COMPL)==null) {
            intent = new Intent(this, AddProductActivity.class);
        }
        else intent = new Intent(this, SearchComplicatedProductActivity.class);
        if (intentF.getStringExtra("From menu") != null) {
            this.date = intentF.getStringExtra(DATE);
            this.menu = intentF.getStringExtra(MENU);
            intent.putExtra("From menu", "MENU");
            intent.putExtra(MENU, menu);
            intent.putExtra(DATE, date);
        }
        intent.putExtra(PRODUCTS, products.get(i));
        intent.putExtra(PROTEINS, proteins.get(i));
        intent.putExtra(FATS, fats.get(i));
        intent.putExtra(CARBOHYDRATES, carbohydrates.get(i));
        intent.putExtra(FA, fas.get(i));
        intent.putExtra(KL, kl.get(i));
        intent.putExtra(GR, gr.get(i));
        intent.putExtra(SearchComplicatedProductActivity.NAME,getIntent().getStringExtra(SearchComplicatedProductActivity.NAME));
        intent.putExtra(ID_PRODUCT,getIntent().getIntExtra(ID_PRODUCT,-1));
        intent.putExtra(COMPLICATED,isComplicated.get(i));


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}