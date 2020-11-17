package com.ration.qcode.application.MainPack.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ration.qcode.application.MainPack.adapter.ComplicatedProductAdapter;
import com.ration.qcode.application.R;

import java.text.DecimalFormat;
import java.util.Calendar;
import static com.ration.qcode.application.utils.Constants.DATE;
import static com.ration.qcode.application.utils.Constants.DAY;
import static com.ration.qcode.application.utils.Constants.HOUR;
import static com.ration.qcode.application.utils.Constants.ID_PRODUCT;
import static com.ration.qcode.application.utils.Constants.INFO;
import static com.ration.qcode.application.utils.Constants.MENU;
import static com.ration.qcode.application.utils.Constants.MINUTE;
import static com.ration.qcode.application.utils.Constants.MONTH;
import static com.ration.qcode.application.utils.Constants.PRODUCTS;
import static com.ration.qcode.application.utils.Constants.PROD_SEARCH;
import static com.ration.qcode.application.utils.Constants.YEAR;

public class SearchComplicatedProductActivity extends AppCompatActivity {
    public static final String PROD_SEARCH_COMPL = "Complicated";
    private EditText editTextSearch;
    private EditText productNameEditText;
    private ImageView searchProduct;
    private RecyclerView productRecView;
    private static ComplicatedProductAdapter adapter;

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
        editTextSearch= (EditText) findViewById(R.id.editSearch);
        productNameEditText= (EditText) findViewById(R.id.product_name);
        searchProduct= (ImageView) findViewById(R.id.search_image_view);

        textBelLevel= (TextView) findViewById(R.id.textBel);
        textFALevel=(TextView) findViewById(R.id.textFA);

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
                Log.e("gr=",productName);
            }
        });
        productRecView= (RecyclerView) findViewById(R.id.product_rec_view);
        productRecView.setLayoutManager(new LinearLayoutManager(this));

        intent=getIntent();
        if (intent.getStringExtra("From menu") != null) {
            this.date = intent.getStringExtra(DATE);
            this.menu = intent.getStringExtra(MENU);
            Log.e("AddProductActivity", date + " " + menu);
        }

        updateUI();
    }

    private void updateUI(){
        if(intent != null && intent.getStringExtra(PRODUCTS) != null && intent.getStringExtra(INFO) == null) {
            if (adapter == null) {
                Log.e("Tut","CreateNewAdapter");
                adapter = new ComplicatedProductAdapter(this);
                adapter.addProduct(intent);
                productRecView.setAdapter(adapter);
            } else {
                Log.e("Tut","continue adapter");
                Log.e("Tut", String.valueOf(adapter.getItemCount()));
                adapter.addProduct(intent);
                productRecView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void searchProduct(View view) {

        Intent inten = new Intent(this, ProductsListActivity.class);
        if (intent.getStringExtra("From menu") != null) {
            inten.putExtra("From menu", "yes");
            this.date = intent.getStringExtra(DATE);
            this.menu = intent.getStringExtra(MENU);
            Log.e("AddProductActivity", date + " " + menu);
            inten.putExtra(MENU, menu);
            inten.putExtra(DATE, date);
        }

        if (editTextSearch.getText().toString().isEmpty()) {
            inten.putExtra(PROD_SEARCH, " ");
        }
        else inten.putExtra(PROD_SEARCH, editTextSearch.getText().toString());
        inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        inten.putExtra(PROD_SEARCH_COMPL,"YES");
        startActivity(inten);
    }

    public void calculate(View view) {
        calculation();
        //adapter=null;
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
               // ComplicatedProductAdapter.ProductHolder holder= (ComplicatedProductAdapter.ProductHolder) productRecView.findViewHolderForLayoutPosition(i);
               // Log.e("Sentence", String.valueOf(holder!=null));
                double grams = adapter.getListGr().get(i);
                gr100=adapter.getListGr().get(i);
                //proteins=holder.getProteins();
                proteins100=adapter.getListProteins().get(i);
                //fats=holder.getFats();
                fats100=adapter.getListFats().get(i);
                //carb=holder.getCarb();
                carb100=adapter.getListCarb().get(i);
                //fa=holder.getFa();
                fa100=adapter.getListFa().get(i);
                //kl=holder.getKl();
                kl100=adapter.getListKl().get(i);


                Log.e("gr1=","from calc "+i+": "+grams);
                Log.e("gr1=","gr100= "+ gr100);
                Log.e("gr1=","proteins100= "+ proteins100);
                proteins += proteins100 / 100 * grams;
                Log.e("gr1=","proteins= "+ proteins);
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

    /*private void calculation() {
        if (productRecView.getAdapter().getItemCount()>0) {
            double grams = Double.parseDouble(editGram.getText().toString());
            if (!textViewProducts.getText().toString().isEmpty()) {
                proteins = proteins100 / gr100 * grams;
                fats = fats100 / gr100 * grams;
                carb = carb100 / gr100 * grams;
                fa = fa100 / gr100 * grams;
                kl = kl100 / gr100 * grams;
                gr = grams;

                textViewProteins.setText(decimalFormat.format(proteins));
                textViewFats.setText(decimalFormat.format(fats));
                textViewCarbohydrates.setText(decimalFormat.format(carb));
                textViewFA.setText(decimalFormat.format(fa));
                textViewKl.setText(decimalFormat.format(kl));
                textViewGr.setText(decimalFormat.format(grams));

                textBelLevel.setText(getString(R.string.proteins_level) + "\n" + decimalFormat.format(proteins));
                textFALevel.setText(getString(R.string.fa_level) + "\n" + decimalFormat.format(fa));
            }
        }
    }*/



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
                Log.e("ITEM_C", String.valueOf(item));
                Log.e("ITEM", String.valueOf(ProductInfoActivity.isComplicated.size()));
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
                Log.e("ITEM", String.valueOf(ProductInfoActivity.isComplicated.size()));
            }
            intentProduct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentProduct);
        } else {
            onBackPressed();
        }
        adapter=null;
    }
}
