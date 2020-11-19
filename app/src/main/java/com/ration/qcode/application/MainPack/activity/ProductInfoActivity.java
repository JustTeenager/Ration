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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ration.qcode.application.MainPack.adapter.ProductsInfoListAdapter;
import com.ration.qcode.application.MainPack.dialog.ChooseProductDialog;
import com.ration.qcode.application.ProductDataBase.DataBaseHelper;
import com.ration.qcode.application.R;
import com.ration.qcode.application.utils.Constants;
import com.ration.qcode.application.utils.NetworkService;
import com.ration.qcode.application.utils.SwipeDetector;
import com.ration.qcode.application.utils.internet.AddProductResponse;
import com.ration.qcode.application.utils.internet.RemoveFromMenu;
import com.ration.qcode.application.utils.internet.RemoveProductFromMenu;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ration.qcode.application.utils.Constants.CARBOHYDRATES;
import static com.ration.qcode.application.utils.Constants.DATE;
import static com.ration.qcode.application.utils.Constants.FA;
import static com.ration.qcode.application.utils.Constants.FATS;
import static com.ration.qcode.application.utils.Constants.GR;
import static com.ration.qcode.application.utils.Constants.ID_PRODUCT;
import static com.ration.qcode.application.utils.Constants.INFO;
import static com.ration.qcode.application.utils.Constants.KL;
import static com.ration.qcode.application.utils.Constants.MAIN_URL_CONST;
import static com.ration.qcode.application.utils.Constants.MENU;
import static com.ration.qcode.application.utils.Constants.PRODUCTS;
import static com.ration.qcode.application.utils.Constants.PROTEINS;


/**
 * Created by deepdev on 10.04.17.
 */

public class ProductInfoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView textProteins, textFats, textCarbohydrates, textKilos, textGramm, textFA;
    private ListView listViewProducts;
    private Retrofit mRetrofit;

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
    public static ArrayList<String> isComplicated = new ArrayList<>();

    private SwipeDetector swipeDetector;
    private ProductsInfoListAdapter adapter;
    String date;
    String menu;
    Intent intent;
    String dateNow = dateFormat.format(datenows);
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

        Gson gson= new GsonBuilder().setLenient().create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(MAIN_URL_CONST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        textProteins = (TextView) findViewById(R.id.textProteins);
        textFats = (TextView) findViewById(R.id.textFats);
        textCarbohydrates = (TextView) findViewById(R.id.textCarbohydrates);
        textKilos = (TextView) findViewById(R.id.textKilos);
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
                isComplicated.add(ochenydaje[7]);
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

    private void setAdapter() {
        adapter = new ProductsInfoListAdapter(this, R.layout.productsinfo_list_item,
                products, proteins, fats, carbohydrates, fas, kl, gr);
        listViewProducts.setOnItemClickListener(this);
        listViewProducts.setAdapter(adapter);
    }

    private void removeFromHostingMenu(String date,String menu) {

        Log.d("Vseharasho","запустилиУдаление");
        RemoveFromMenu removeFromMenu=mRetrofit.create(RemoveFromMenu.class);
        Call<String> call=removeFromMenu.removeFromMenu(menu,date);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Vseharasho","запустили onResponse");
                if (response.isSuccessful()) {
                    Log.d("Vseharasho","ochenydaje");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Vseharasho","пососали");
                Log.e("Vseharasho", String.valueOf(t));
            }
        });
    }

    private void removeFromHostingMenu(String date,String menu,String product) {
        Log.d("Vseharasho2","запустилиУдаление");

        RemoveProductFromMenu removeProductFromMenu=mRetrofit.create(RemoveProductFromMenu.class);
        Call<String> call=removeProductFromMenu.removeProductFromMenu(menu,date,product);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Vseharasho2","запустили onResponse");
                if (response.isSuccessful()) {
                    Log.d("Vseharasho2","ochenydaje");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Vseharasho2","пососали");
                Log.e("Vseharasho2", String.valueOf(t));
            }
        });
    }


    public void saveToDB(View view) {
        intent = getIntent();
        if (intent.getStringExtra("From menu") != null) {
            String date = intent.getStringExtra(DATE);
            String menu = intent.getStringExtra(MENU);

            db.removeFromMenu(date, menu);
            removeFromHostingMenu(date,menu);
            Log.e("MENU", String.valueOf(menu));
            if (!products.isEmpty()) {
                if (!db.getDates().contains(date)) {
                    db.insertDate(date);
                    insertInHostingIntoDate(date);
                }

                db.insertMenuDates(menu, date);
                insertInHostingIntoDateMenu(menu,date);
                for (int i = 0; i < products.size(); i++) {
                    db.insertIntoMenu(menu, date, products.get(i) + "|", fats.get(i), proteins.get(i),
                            carbohydrates.get(i), fas.get(i), kl.get(i), gr.get(i), isComplicated.get(i));
                    insertInHostingIntoMenu(menu, date, products.get(i) + "|", fats.get(i), proteins.get(i),
                            carbohydrates.get(i), fas.get(i), kl.get(i), gr.get(i),isComplicated.get(i));
                }
            }
        } else {
            ArrayList<String> menus = db.getMenues(dateNow);
            int size = menus.size();
            if (!products.isEmpty()) {
                if (size == 0) {
                    db.insertDate(dateNow);
                    insertInHostingIntoDate(dateNow);
                    db.insertMenuDates(timeNow, dateNow);
                    insertInHostingIntoDateMenu(timeNow,dateNow);
                } else {
                    db.insertMenuDates(timeNow, dateNow);
                    insertInHostingIntoDateMenu(timeNow,dateNow);
                }
                for (int i = 0; i < products.size(); i++) {
                    db.insertIntoMenu(timeNow, dateNow, products.get(i) + "|", fats.get(i), proteins.get(i),
                            carbohydrates.get(i), fas.get(i), kl.get(i), gr.get(i), isComplicated.get(i));
                    insertInHostingIntoMenu(timeNow, dateNow, products.get(i) + "|", fats.get(i), proteins.get(i),
                            carbohydrates.get(i), fas.get(i), kl.get(i), gr.get(i), isComplicated.get(i));
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

    private void insertInHostingIntoMenu(String menu, String date, String product, String fats, String proteins, String carbohydrates,
                                         String fas, String kl, String gr,String complicated) {
        NetworkService.getInstance(Constants.MAIN_URL_CONST)
                .getMenuJSONApi()
                .insertProduct(menu,date,product,fats, proteins,carbohydrates,fas,kl,gr,complicated)
                .enqueue(new Callback<AddProductResponse>() {
                    @Override
                    public void onResponse(Call<AddProductResponse> call, Response<AddProductResponse> response) {

                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals("ok")) {
                                Log.d("Response","Меню добавлено");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AddProductResponse> call, Throwable t) {

                    }
                });
    }

    private void insertInHostingIntoDate(String date){
        Log.d("Response","Зашли в добавление даты" );
        NetworkService.getInstance(Constants.MAIN_URL_CONST)
                .getDateJSONApi()
                .insertDate(date)
                .enqueue(new Callback<AddProductResponse>() {
                    @Override
                    public void onResponse(Call<AddProductResponse> call, Response<AddProductResponse> response) {
                        Log.d("Response", "зашли в добавление даты");
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals("ok")) {
                                Log.d("Response","Дата добавлено");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AddProductResponse> call, Throwable t) {

                    }
                });
    }

    private void insertInHostingIntoDateMenu(String menu, String date){
        NetworkService.getInstance(Constants.MAIN_URL_CONST)
                .getMenuDateJSONApi()
                .insertDateMenu(menu,date)
                .enqueue(new Callback<AddProductResponse>() {
                    @Override
                    public void onResponse(Call<AddProductResponse> call, Response<AddProductResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals("ok")) {
                                Log.d("Response","Меню добавлено");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AddProductResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (swipeDetector.swipeDetected()) {
            if (swipeDetector.getAction() == SwipeDetector.Action.LR) {
                deleteFromList(i);
            }
        } else {

            //TODO РАЗДЕЛЕНИЕ ХУЙНИ
            Intent inten;
            if (isComplicated.get(i).equals("1")){
                inten=new Intent(this,SearchComplicatedProductActivity.class);
                inten.putExtra("COMPL","true");
            }
            else inten = new Intent(this, AddProductActivity.class);



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
                        removeFromHostingMenu(date,menu,products.get(position)+"|");
                        Log.d("whoooo is this ", db.getProducts(date, menu) + "");
                        if (db.getProducts(date, menu).isEmpty()) {
                            db.removeFromMenu(date, menu);
                            removeFromHostingMenu(date,menu);
                        }

                        products.remove(position);
                        proteins.remove(position);
                        carbohydrates.remove(position);
                        fas.remove(position);
                        kl.remove(position);
                        gr.remove(position);
                        isComplicated.remove(position);

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
        isComplicated.clear();
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
