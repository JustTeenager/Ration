package com.ration.qcode.application.MainPack.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ration.qcode.application.MainPack.adapter.ProductsInfoListAdapter;
import com.ration.qcode.application.MainPack.dialog.ChooseProductDialog;
import com.ration.qcode.application.ProductDataBase.DataBaseHelper;
import com.ration.qcode.application.R;
import com.ration.qcode.application.utils.SwipeDetector;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.ration.qcode.application.utils.Constants.CARBOHYDRATES;
import static com.ration.qcode.application.utils.Constants.DATE;
import static com.ration.qcode.application.utils.Constants.FA;
import static com.ration.qcode.application.utils.Constants.FATS;
import static com.ration.qcode.application.utils.Constants.GR;
import static com.ration.qcode.application.utils.Constants.ID_PRODUCT;
import static com.ration.qcode.application.utils.Constants.INFO;
import static com.ration.qcode.application.utils.Constants.KL;
import static com.ration.qcode.application.utils.Constants.MENU;
import static com.ration.qcode.application.utils.Constants.PRODUCTS;
import static com.ration.qcode.application.utils.Constants.PROTEINS;


/**
 * Created by deepdev on 10.04.17.
 */

public class ProductInfoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView textProteins, textFats, textCarbohydrates, textKilos, textGramm, textFA;
    private ListView listViewProducts;

    DataBaseHelper db;

    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
    DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    Date datenows = new Date();
    Date timeNows = new Date();


    public ArrayList<String> all = new ArrayList<>();
    public static ArrayList<String> products = new ArrayList<>();
    public static ArrayList<String> proteins = new ArrayList<>();
    public static ArrayList<String> fats = new ArrayList<>();
    public static ArrayList<String> carbohydrates = new ArrayList<>();
    public static ArrayList<String> fas = new ArrayList<>();
    public static ArrayList<String> kl = new ArrayList<>();
    public static ArrayList<String> gr = new ArrayList<>();

    private SwipeDetector swipeDetector;
    private ProductsInfoListAdapter adapter;
    String date;
    String menu;
    Intent intent;
    String datenow = dateFormat.format(datenows);
    String timeNow = timeFormat.format(timeNows);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info_activity);
        Log.e("PRODICTION_INFO", ProductInfoActivity.class.toString());

        db = DataBaseHelper.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);


        textProteins = (TextView) findViewById(R.id.textProteins);
        textFats = (TextView) findViewById(R.id.textFats);
        textCarbohydrates = (TextView) findViewById(R.id.textCarbohydrates);
        textKilos = (TextView) findViewById(R.id.textKilos);
        textGramm = (TextView) findViewById(R.id.textGram);
        textGramm = (TextView) findViewById(R.id.textGram);
        textFA = (TextView) findViewById(R.id.textFA);


        listViewProducts = (ListView) findViewById(R.id.listProducts);

        intent = getIntent();

        if (intent.getStringExtra("From menu") != null) {
            this.date = intent.getStringExtra(DATE);
            this.menu = intent.getStringExtra(MENU);
            Log.e("PrpoductInfoActivity", date + " " + menu);
            //   db.removeFromMenu(date, menu);
        }

        if (intent.getStringExtra(INFO) != null) {
            clearAll();
            this.menu = intent.getStringExtra(MENU);
            this.date = intent.getStringExtra(DATE);

            all = db.getProducts(date, menu);

            for (int i = 0; i < all.size(); i++) {

                String vseharasho = all.get(i);
                String[] product = vseharasho.split("\\|");
                String[] ochenydaje = product[1].split("\\s+");
                products.add(product[0]);
                proteins.add(ochenydaje[1]);
                fats.add(ochenydaje[2]);
                carbohydrates.add(ochenydaje[3]);
                fas.add(ochenydaje[4]);
                kl.add(ochenydaje[5]);
                gr.add(ochenydaje[6]);

            }
            common();

        } else if (intent.getStringExtra("from Add") != null) {
            common();
        } else {
            clearAll();
        }
        listViewProducts.setOnItemClickListener(this);
        swipeDetector = new SwipeDetector();
        listViewProducts.setOnTouchListener(swipeDetector);

        adapter = new ProductsInfoListAdapter(this, R.layout.productsinfo_list_item,
                products, proteins, fats, carbohydrates, fas, kl, gr);
        listViewProducts.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  addProduct();
                ChooseProductDialog dialog = new ChooseProductDialog(date,menu,intent);
                dialog.show(getFragmentManager(),null);
                setAdapter();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                Intent i = getBaseContext().getPackageManager().
                        getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /*public void addProduct() {

        Intent inten = new Intent(this, AddProductActivity.class);
        if (intent.getStringExtra("From menu") != null) {
            inten.putExtra("From menu", "yes");
            inten.putExtra(MENU, menu);
            inten.putExtra(DATE, date);
        }
        inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(inten);
    }*/

    private void setAdapter() {
        adapter = new ProductsInfoListAdapter(this, R.layout.productsinfo_list_item,
                products, proteins, fats, carbohydrates, fas, kl, gr);
        listViewProducts.setOnItemClickListener(this);
        listViewProducts.setAdapter(adapter);
    }

    public void saveToDB(View view) {
        intent = getIntent();
        if (intent.getStringExtra("From menu") != null) {
            String date = intent.getStringExtra(DATE);
            String menu = intent.getStringExtra(MENU);

            db.removeFromMenu(date, menu);
            Log.e("MENU", String.valueOf(menu));
            if (!products.isEmpty()) {
                if (!db.getDates().contains(date))
                    db.insertDate(date);

                db.insertMenuDates(menu, date);
                for (int i = 0; i < products.size(); i++) {
                    db.insertIntoMenu(menu, date, products.get(i) + "|", fats.get(i), proteins.get(i),
                            carbohydrates.get(i), fas.get(i), kl.get(i), gr.get(i));
                }
            }
        } else {
            ArrayList<String> menus = db.getMenues(datenow);
            int size = menus.size();
            if (!products.isEmpty()) {
                if (size == 0) {
                    db.insertDate(datenow);
                    db.insertMenuDates(timeNow, datenow);
                } else {
                    db.insertMenuDates(timeNow, datenow);
                }
                for (int i = 0; i < products.size(); i++) {
                    db.insertIntoMenu(timeNow, datenow, products.get(i) + "|", fats.get(i), proteins.get(i),
                            carbohydrates.get(i), fas.get(i), kl.get(i), gr.get(i));
                }
            }
        }

        finish();

        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(i);

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (swipeDetector.swipeDetected()) {
            if (swipeDetector.getAction() == SwipeDetector.Action.LR) {
                deleteFromList(i);
            }
        } else {
            Intent inten = new Intent(this, AddProductActivity.class);
            if (intent.getStringExtra("From menu") != null) {
                inten.putExtra("From menu", "yes");
                inten.putExtra(MENU, menu);
                inten.putExtra(DATE, date);
            }
            inten.putExtra(ID_PRODUCT, i);
            inten.putExtra(PRODUCTS, products.get(i));
            inten.putExtra(PROTEINS, proteins.get(i));
            inten.putExtra(FATS, fats.get(i));
            inten.putExtra(CARBOHYDRATES, carbohydrates.get(i));
            inten.putExtra(FA, fas.get(i));
            inten.putExtra(KL, kl.get(i));
            inten.putExtra(GR, gr.get(i));
            inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(inten);
        }
    }

    private void deleteFromList(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_question))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        db.removeFromMenu(date, menu, products.get(position) + "|");
                        Log.d("whoooo is this ", db.getProducts(date, menu) + "");
                        if (db.getProducts(date, menu).isEmpty()) {
                            db.removeFromMenu(date, menu);
                        }

                        products.remove(position);
                        proteins.remove(position);
                        carbohydrates.remove(position);
                        fas.remove(position);
                        kl.remove(position);
                        gr.remove(position);

                        adapter = new ProductsInfoListAdapter(getApplicationContext(),
                                R.layout.productsinfo_list_item,
                                products, proteins, fats, carbohydrates, fas, kl, gr);
                        listViewProducts.setAdapter(adapter);
                        common();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void clearAll() {
        products.clear();
        proteins.clear();
        fats.clear();
        carbohydrates.clear();
        fas.clear();
        kl.clear();
        gr.clear();
    }

    public void common() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double prot = 0;
        double fat = 0;
        double car = 0;
        double fass = 0;
        double kll = 0;
        double grr = 0;
        for (int i = 0; i < products.size(); i++) {
            textProteins.setText("" + decimalFormat.format(prot += Double.parseDouble(proteins.get(i))));
            textGramm.setText("" + decimalFormat.format(grr += Double.parseDouble(gr.get(i))));
            textCarbohydrates.setText("" + decimalFormat.format(car += Double.parseDouble(carbohydrates.get(i))));
            textFA.setText("" + decimalFormat.format(fass += Double.parseDouble(fas.get(i))));
            textFats.setText("" + decimalFormat.format(fat += Double.parseDouble(fats.get(i))));
            textKilos.setText("" + decimalFormat.format(kll += Double.parseDouble(kl.get(i))));
        }
    }
}
