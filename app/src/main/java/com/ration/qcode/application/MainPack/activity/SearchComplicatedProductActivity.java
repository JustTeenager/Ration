package com.ration.qcode.application.MainPack.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ration.qcode.application.MainPack.adapter.ComplicatedProductAdapter;
import com.ration.qcode.application.ProductDataBase.DataBaseHelper;
import com.ration.qcode.application.R;
import com.ration.qcode.application.utils.Constants;
import com.ration.qcode.application.utils.NetworkService;
import com.ration.qcode.application.utils.internet.AddProductResponse;
import com.ration.qcode.application.utils.internet.UpdateGrAPI;
import com.ration.qcode.application.utils.internet.UpdateProductAPI;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ration.qcode.application.utils.Constants.CARBOHYDRATES;
import static com.ration.qcode.application.utils.Constants.DATE;
import static com.ration.qcode.application.utils.Constants.DAY;
import static com.ration.qcode.application.utils.Constants.FA;
import static com.ration.qcode.application.utils.Constants.FATS;
import static com.ration.qcode.application.utils.Constants.GR;
import static com.ration.qcode.application.utils.Constants.HOUR;
import static com.ration.qcode.application.utils.Constants.ID_PRODUCT;
import static com.ration.qcode.application.utils.Constants.INFO;
import static com.ration.qcode.application.utils.Constants.KL;
import static com.ration.qcode.application.utils.Constants.MAIN_URL_CONST;
import static com.ration.qcode.application.utils.Constants.MENU;
import static com.ration.qcode.application.utils.Constants.MINUTE;
import static com.ration.qcode.application.utils.Constants.MONTH;
import static com.ration.qcode.application.utils.Constants.PRODUCTS;
import static com.ration.qcode.application.utils.Constants.PROD_SEARCH;
import static com.ration.qcode.application.utils.Constants.PROTEINS;
import static com.ration.qcode.application.utils.Constants.YEAR;

public class SearchComplicatedProductActivity extends AppCompatActivity {
    public static final String PROD_SEARCH_COMPL = "Complicated";
    private EditText editTextSearch;
    private EditText productNameEditText;
    private ImageView searchProduct;
    private RecyclerView productRecView;
    private static ComplicatedProductAdapter adapter;

    private Retrofit retrofit;

    private TextView textBelLevel;
    private TextView textFALevel;

    private double proteins = 0, fats, carb, fa, kl, gr;

    private double proteins100, fats100, carb100, fa100, kl100, gr100;

    private String productName;

    private String menu, date;

    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_complicated_product);

        Gson gson= new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(MAIN_URL_CONST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        editTextSearch= (EditText) findViewById(R.id.editSearch);
        productNameEditText= (EditText) findViewById(R.id.product_name);
        searchProduct= (ImageView) findViewById(R.id.search_image_view);

        textBelLevel= (TextView) findViewById(R.id.textBel);
        textFALevel=(TextView) findViewById(R.id.textFA);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        searchProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchProduct(view);
            }
        });

        productNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                productName=productNameEditText.getText().toString();
            }
        });
        productRecView= (RecyclerView) findViewById(R.id.product_rec_view);
        productRecView.setLayoutManager(new LinearLayoutManager(this));

        intent=getIntent();
        if (intent.getStringExtra("From menu") != null) {
            this.date = intent.getStringExtra(DATE);
            this.menu = intent.getStringExtra(MENU);
        }
        updateUI();
    }

    private void updateUI(){
        //Просто создание
        if(intent != null && intent.getStringExtra(PRODUCTS) != null && intent.getStringExtra(INFO) == null && intent.getStringExtra("COMPL")==null) {
            if (adapter == null) {
                adapter = new ComplicatedProductAdapter(this);
            }
            adapter.addProduct(intent);
            productRecView.setAdapter(adapter);
        }



        //распаковка сложного продукта
        else if(intent != null && intent.getStringExtra(PRODUCTS) != null && intent.getStringExtra(INFO) == null && intent.getStringExtra("COMPL")!=null) {
            if (adapter == null) {
                adapter = new ComplicatedProductAdapter(this);



                ArrayList<Intent> intentsList = DataBaseHelper.getInstance(this).getFromComplicated(intent.getStringExtra(PRODUCTS));
                productNameEditText.setText(intent.getStringExtra(PRODUCTS));
                for (Intent intent : intentsList) {
                    adapter.addProduct(intent);
                    productRecView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            else {
                ArrayList<Intent> intentsList = DataBaseHelper.getInstance(this).getFromComplicated(intent.getStringExtra(PRODUCTS));
                productNameEditText.setText(intent.getStringExtra(PRODUCTS));
                for (Intent intent : intentsList) {
                    adapter.addProduct(intent);
                    productRecView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        }

    }

    public void searchProduct(View view) {

        Intent inten = new Intent(this, ProductsListActivity.class);
        if (intent.getStringExtra("From menu") != null) {
            inten.putExtra("From menu", "yes");
            this.date = intent.getStringExtra(DATE);
            this.menu = intent.getStringExtra(MENU);
            inten.putExtra(MENU, menu);
            inten.putExtra(DATE, date);
        }

        inten.putExtra(PROD_SEARCH, editTextSearch.getText().toString());
        inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        inten.putExtra(PROD_SEARCH_COMPL,"YES");
        startActivity(inten);
    }

    public void calculate(View view) {
        calculation();
    }

    private void calculation(){
        if (productRecView.getAdapter().getItemCount()>0) {
            gr100=0;
            proteins100=0;
            fats100=0;
            carb100=0;
            fa100=0;
            kl100=0;
            gr=0;
            proteins=0;
            fats=0;
            carb=0;
            fa=0;
            kl=0;
            for (int i = 0; i < adapter.getItemCount(); i++) {
                double grams = adapter.getListGr().get(i);
                gr100=adapter.getListGr().get(i);
                proteins100=adapter.getListProteins().get(i);
                fats100=adapter.getListFats().get(i);
                carb100=adapter.getListCarb().get(i);
                fa100=adapter.getListFa().get(i);
                kl100=adapter.getListKl().get(i);

                proteins += proteins100 / 100 * grams;
                fats += fats100 / 100 * grams;
                carb += carb100 / 100 * grams;
                fa += fa100 / 100 * grams;
                kl += kl100 / 100 * grams;
                gr += grams;


                textBelLevel.setText(getString(R.string.proteins_level) + "\n" + new DecimalFormat("#.##").format(proteins));
                textFALevel.setText(getString(R.string.fa_level) + "\n" + new DecimalFormat("#.##").format(fa));
            }
        }
        else super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        adapter=null;
    }



    private boolean dateOfPay() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Calendar alarmStartTime = Calendar.getInstance();
        Calendar alarmNowTime = Calendar.getInstance();
        alarmStartTime.set(Calendar.YEAR,  prefs.getInt(YEAR, 0));
        alarmStartTime.set(Calendar.MONTH,  prefs.getInt(MONTH, 0));
        alarmStartTime.set(Calendar.DAY_OF_MONTH,  prefs.getInt(DAY, 0));
        alarmStartTime.set(Calendar.HOUR_OF_DAY,  prefs.getInt(HOUR, 0));
        alarmStartTime.set(Calendar.MINUTE,  prefs.getInt(MINUTE, 0));
        return alarmNowTime.getTime().compareTo(alarmStartTime.getTime()) >= 0;
    }

    public void addItem(View view) {

        if (productNameEditText.getText().toString().isEmpty()){
            Toast.makeText(this,getString(R.string.enter_a_name),Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intentProduct = new Intent(this, ProductInfoActivity.class);
        if (productRecView.getAdapter().getItemCount()>0) {
            if(!dateOfPay()) {
                calculation();
            }
            if (intent.getStringExtra("From menu") != null) {
                intentProduct.putExtra("From menu", "yes");
                this.date = intent.getStringExtra(DATE);
                this.menu = intent.getStringExtra(MENU);
                intentProduct.putExtra(MENU, menu);
                intentProduct.putExtra(DATE, date);
            }
            if (intent.getIntExtra(ID_PRODUCT, -1) != -1) {
                int item = intent.getIntExtra(ID_PRODUCT, -1);

                intentProduct.putExtra("from Add", "yes");
                ProductInfoActivity.products.set(item, productName);
                ProductInfoActivity.proteins.set(item, "" + proteins);
                ProductInfoActivity.fats.set(item, "" + fats);
                ProductInfoActivity.carbohydrates.add(item, "" + carb);
                ProductInfoActivity.fas.set(item, "" + fa);
                ProductInfoActivity.kl.set(item, "" + kl);
                ProductInfoActivity.gr.set(item, "" + gr);
                ProductInfoActivity.isComplicated.set(item,"1");
            } else {
                intentProduct.putExtra("from Add", "yes");
                ProductInfoActivity.products.add(productName);
                ProductInfoActivity.proteins.add("" + proteins);
                ProductInfoActivity.fats.add("" + fats);
                ProductInfoActivity.carbohydrates.add("" + carb);
                ProductInfoActivity.fas.add("" + fa);
                ProductInfoActivity.kl.add("" + kl);
                ProductInfoActivity.gr.add("" + gr);
                ProductInfoActivity.isComplicated.add("1");
            }
//
            ComplicatedProductAdapter complicatedProductAdapter= (ComplicatedProductAdapter) productRecView.getAdapter();
            for (int i=0;i<complicatedProductAdapter.getItemCount();i++){
                Log.e("adapter_removeSize", String.valueOf(productRecView.getAdapter().getItemCount()));
                if (!DataBaseHelper.getInstance(this).getCheckFromComplicated(productName,complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(PRODUCTS)))
                {
                    Log.e("adapter_removeSize", String.valueOf(productRecView.getAdapter().getItemCount()));
                    DataBaseHelper.getInstance(this).insertIntoComplicated(productName, complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(PRODUCTS),complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(FATS),
                            complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(PROTEINS), complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(CARBOHYDRATES),
                            complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(FA), complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(KL),
                    String.valueOf(complicatedProductAdapter.getListGr().get(i)), "0");
                    addComplicatedOntoHosting(this,complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(PRODUCTS),String.valueOf(complicatedProductAdapter.getListFa().get(i)),
                            String.valueOf(complicatedProductAdapter.getListKl().get(i)), String.valueOf(complicatedProductAdapter.getListProteins().get(i)), String.valueOf(complicatedProductAdapter.getListCarb().get(i)), String.valueOf(complicatedProductAdapter.getListFats().get(i)),String.valueOf(complicatedProductAdapter.getListGr().get(i)));
                }

                else if(DataBaseHelper.getInstance(this).getCheckGrFromComplicated(productName,complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(PRODUCTS),String.valueOf(complicatedProductAdapter.getListGr().get(i)))){
                    Log.e("another","working");
                    DataBaseHelper.getInstance(this).updateGrams(productName,complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(PRODUCTS),String.valueOf(complicatedProductAdapter.getListFats().get(i)),String.valueOf(complicatedProductAdapter.getListProteins().get(i)),String.valueOf(complicatedProductAdapter.getListCarb().get(i)),String.valueOf(complicatedProductAdapter.getListFa().get(i)),String.valueOf(complicatedProductAdapter.getListKl().get(i)),String.valueOf(complicatedProductAdapter.getListGr().get(i)));
                    updateGrIntoHosting(i);
                }
            }

            for (int i=0;i<complicatedProductAdapter.getProductMaterials().size();i++){

                Log.e("Helper", String.valueOf(DataBaseHelper.getInstance(this).getCheckFromComplicated(productName,complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(PRODUCTS))));
                Log.e("Helper2", String.valueOf((complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(PRODUCTS))==null));
                Log.e("Helper_adapter", String.valueOf(complicatedProductAdapter.getProductMaterials().size()));
                if (DataBaseHelper.getInstance(this).getCheckFromComplicated(productName,complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(PRODUCTS)) && (complicatedProductAdapter.getListFa().size()<complicatedProductAdapter.getProductMaterials().size())){
                    Log.e("IF","удаление");
                    for (int j=0;j<complicatedProductAdapter.getDeletedPositionsList().size();j++) {
                        if (i==complicatedProductAdapter.getDeletedPositionsList().get(j)) {
                            Log.e("IF","удаление2");

                           // Log.d("data_gr",)



                            //TODO удаление из таблицы составных и апдейт составного продукта в общем списке продуктов (локал и хост)
                            DataBaseHelper.getInstance(this).changeLocalDBAfterDeletingInRecView(productName, complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(PRODUCTS), complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(FATS),
                                    complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(PROTEINS), complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(CARBOHYDRATES),
                                    complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(FA), complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(KL),
                                    String.valueOf(complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(GR)));
                            updateProductIntoHosting(i, complicatedProductAdapter);
                            complicatedProductAdapter.removeFromMaterials(i);
                            productRecView.setAdapter(complicatedProductAdapter);
                        }
                    }
                }
            }

            DataBaseHelper.getInstance(this).insertIntoProduct(productName + "|",String.valueOf(fats),
                    String.valueOf(proteins),String.valueOf(carb),String.valueOf(fa),String.valueOf(kl),String.valueOf(gr),"1");

            addComplicatedProductOntoHosting(this,productName,String.valueOf(fa),
                    String.valueOf(kl),String.valueOf(proteins),String.valueOf(carb),String.valueOf(fats));


            intentProduct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentProduct);
        } else {
            onBackPressed();
        }
        adapter=null;
    }


    private void addComplicatedProductOntoHosting(Context context, String name, String fa, String kkal, String belok, String uglevod, String jiry){
        NetworkService.getInstance(Constants.MAIN_URL_CONST)
                .getJSONApi()
                .insertProduct(name, fa, kkal, belok, uglevod, jiry, "1")
                .enqueue(
                        new Callback<AddProductResponse>() {
                            @Override
                            public void onResponse(Call<AddProductResponse> call, Response<AddProductResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().getStatus().equals("ok")) {
                                        Toast.makeText(context, response.body().getAnswer(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<AddProductResponse> call, Throwable t) {}
                        });
    }

    private void addComplicatedOntoHosting(Context context, String name,String fa, String kkal, String belok, String uglevod, String jiry,String gr){
        NetworkService.getInstance(Constants.MAIN_URL_CONST)
                .getComplicatedApi()
                .insertComplicated(productName,name,jiry,belok, uglevod, fa,kkal,gr,String.valueOf(1))
                .enqueue(
                        new Callback<AddProductResponse>() {
                            @Override
                            public void onResponse(Call<AddProductResponse> call, Response<AddProductResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().getStatus().equals("ok")) {
                                        Toast.makeText(context, response.body().getAnswer(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<AddProductResponse> call, Throwable t) {
                            }
                        });
    }

    private void updateGrIntoHosting(int i) {
        UpdateGrAPI updateGrAPI=retrofit.create(UpdateGrAPI.class);
        Call<String> call=updateGrAPI.updateGrApi(productName,adapter.getProductMaterials().get(i).getStringExtra(PRODUCTS),String.valueOf(adapter.getListFats().get(i)),String.valueOf(adapter.getListProteins().get(i)),String.valueOf(adapter.getListCarb().get(i)),String.valueOf(adapter.getListFa().get(i)),String.valueOf(adapter.getListKl().get(i)),String.valueOf(adapter.getListGr().get(i)));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void updateProductIntoHosting(int i,ComplicatedProductAdapter complicatedProductAdapter) {
        UpdateProductAPI updateProductAPI=retrofit.create(UpdateProductAPI.class);
        Call<String> call=updateProductAPI.updateComplicated(productName, complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(PRODUCTS),complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(FATS),
                complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(PROTEINS), complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(CARBOHYDRATES),
                complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(FA), complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(KL),
                String.valueOf(complicatedProductAdapter.getProductMaterials().get(i).getStringExtra(GR)));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    Log.e("vseharasho","response");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.e("home","tapped");
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}