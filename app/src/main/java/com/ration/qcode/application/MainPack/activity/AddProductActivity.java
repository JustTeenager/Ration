package com.ration.qcode.application.MainPack.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ration.qcode.application.R;

import java.text.DecimalFormat;
import java.util.Calendar;

import static com.ration.qcode.application.utils.Constants.CARBOHYDRATES;
import static com.ration.qcode.application.utils.Constants.COMPLICATED;
import static com.ration.qcode.application.utils.Constants.DATE;
import static com.ration.qcode.application.utils.Constants.DAY;
import static com.ration.qcode.application.utils.Constants.FA;
import static com.ration.qcode.application.utils.Constants.FATS;
import static com.ration.qcode.application.utils.Constants.GR;
import static com.ration.qcode.application.utils.Constants.HOUR;
import static com.ration.qcode.application.utils.Constants.ID_PRODUCT;
import static com.ration.qcode.application.utils.Constants.INFO;
import static com.ration.qcode.application.utils.Constants.KL;
import static com.ration.qcode.application.utils.Constants.MENU;
import static com.ration.qcode.application.utils.Constants.MINUTE;
import static com.ration.qcode.application.utils.Constants.MONTH;
import static com.ration.qcode.application.utils.Constants.PRODUCTS;
import static com.ration.qcode.application.utils.Constants.PROD_SEARCH;
import static com.ration.qcode.application.utils.Constants.PROTEINS;
import static com.ration.qcode.application.utils.Constants.YEAR;


/**
 * @author deepdev on 10.04.17.
 */

public class AddProductActivity extends AppCompatActivity {

    private EditText editSearch, editGram;
    private TextView textViewProducts, textViewProteins, textViewFats, textViewCarbohydrates,
            textViewFA, textViewKl, textViewGr, textFALevel, textBelLevel;
    private Intent intent;
    private DecimalFormat decimalFormat;
    private double proteins = 0, fats, carb, fa, kl, gr;
    private String isComplicated;

    private double proteins100, fats100, carb100, fa100, kl100, gr100;
    private String menu, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_activity);

        decimalFormat = new DecimalFormat("#.##");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        editSearch = (EditText) findViewById(R.id.editSearch);
        editGram = (EditText) findViewById(R.id.editGram);
        textViewProducts = (TextView) findViewById(R.id.textViewProduct);
        textViewProteins = (TextView) findViewById(R.id.textViewProteins);
        textViewFats = (TextView) findViewById(R.id.textViewFats);
        textViewCarbohydrates = (TextView) findViewById(R.id.textViewCarbohydrates);
        textViewFA = (TextView) findViewById(R.id.textViewFA);
        textViewKl = (TextView) findViewById(R.id.textViewKl);
        textViewGr = (TextView) findViewById(R.id.textViewGr);
        textFALevel = (TextView) findViewById(R.id.textFA);
        textBelLevel = (TextView) findViewById(R.id.textBel);


        getIntents();

        if(dateOfPay()) {
            ((Button) findViewById(R.id.btnCalculate)).setVisibility(View.GONE);
        }
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

    private void getIntents() {

        intent = getIntent();

        if (intent.getStringExtra("From menu") != null) {
            this.date = intent.getStringExtra(DATE);
            this.menu = intent.getStringExtra(MENU);

        }
        if (intent != null && intent.getStringExtra(PRODUCTS) != null && intent.getStringExtra(INFO) == null) {

            proteins = Double.parseDouble(intent.getStringExtra(PROTEINS));
            fats = Double.parseDouble(intent.getStringExtra(FATS));
            carb = Double.parseDouble(intent.getStringExtra(CARBOHYDRATES));
            fa = Double.parseDouble(intent.getStringExtra(FA));
            kl = Double.parseDouble(intent.getStringExtra(KL));
            gr = Double.parseDouble(intent.getStringExtra(GR));
            isComplicated=intent.getStringExtra(COMPLICATED);

            proteins100 = proteins;
            fats100 = fats;
            carb100 = carb;
            fa100 = fa;
            kl100 = kl;
            gr100 = gr;

            textViewProducts.setText(intent.getStringExtra(PRODUCTS));
            textViewProteins.setText(decimalFormat.format(proteins));
            textViewFats.setText(decimalFormat.format(fats));
            textViewCarbohydrates.setText(decimalFormat.format(carb));
            textViewFA.setText(decimalFormat.format(fa));
            textViewKl.setText(decimalFormat.format(kl));
            textViewGr.setText(decimalFormat.format(gr));

        }
    }


    public void addItem(View v) {

        if(!dateOfPay()) {
            calculation();
        }

        Intent intentProduct = new Intent(this, ProductInfoActivity.class);
        if (!textViewProducts.getText().toString().isEmpty()) {
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
                ProductInfoActivity.products.set(item, textViewProducts.getText().toString());
                ProductInfoActivity.proteins.set(item, "" + proteins);
                ProductInfoActivity.fats.set(item, "" + fats);
                ProductInfoActivity.carbohydrates.add(item, "" + carb);
                ProductInfoActivity.fas.set(item, "" + fa);
                ProductInfoActivity.kl.set(item, "" + kl);
                ProductInfoActivity.gr.set(item, "" + gr);
                ProductInfoActivity.isComplicated.set(item,"" + isComplicated);


            } else {
                if (!textViewProducts.getText().toString().isEmpty()) {
                    intentProduct.putExtra("from Add", "yes");
                    ProductInfoActivity.products.add(textViewProducts.getText().toString());
                    ProductInfoActivity.proteins.add("" + proteins);
                    ProductInfoActivity.fats.add("" + fats);
                    ProductInfoActivity.carbohydrates.add("" + carb);
                    ProductInfoActivity.fas.add("" + fa);
                    ProductInfoActivity.kl.add("" + kl);
                    ProductInfoActivity.gr.add("" + gr);
                    ProductInfoActivity.isComplicated.add(""+isComplicated);
                }
            }
            intentProduct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentProduct);
        } else {
            onBackPressed();
        }
    }

    public void searchProduct(View v) {

        Intent inten = new Intent(this, ProductsListActivity.class);
        if (intent.getStringExtra("From menu") != null) {
            inten.putExtra("From menu", "yes");
            this.date = intent.getStringExtra(DATE);
            this.menu = intent.getStringExtra(MENU);
            inten.putExtra(MENU, menu);
            inten.putExtra(DATE, date);
        }


        inten.putExtra(PROD_SEARCH, editSearch.getText().toString());
        inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(inten);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void calculate(View v) {
        calculation();
    }

    private void calculation() {
        if (!editGram.getText().toString().isEmpty()) {
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
    }
}
