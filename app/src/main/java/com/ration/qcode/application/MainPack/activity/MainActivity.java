package com.ration.qcode.application.MainPack.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ration.qcode.application.MainPack.dialog.AddProductDialog;
import com.ration.qcode.application.MainPack.fragment.AnalyzesListFragment;
import com.ration.qcode.application.MainPack.fragment.FeedbackFragment;
import com.ration.qcode.application.MainPack.fragment.MainListFragment;
import com.ration.qcode.application.ProductDataBase.DataBaseHelper;
import com.ration.qcode.application.R;
import com.ration.qcode.application.utils.Constants;
import com.ration.qcode.application.utils.internet.DateMenuResponse;
import com.ration.qcode.application.utils.internet.DateResponse;
import com.ration.qcode.application.utils.internet.IGetAllDataAPI;
import com.ration.qcode.application.utils.internet.IGetAllDateAPI;
import com.ration.qcode.application.utils.internet.IGetAllMenuAPI;
import com.ration.qcode.application.utils.internet.IGetAllMenuDateAPI;
import com.ration.qcode.application.utils.internet.IGetPrice;
import com.ration.qcode.application.utils.internet.MenuResponse;
import com.ration.qcode.application.utils.internet.PriceResponse;
import com.ration.qcode.application.utils.internet.TasksResponse;
import com.yandex.money.api.methods.payment.params.P2pTransferParams;
import com.yandex.money.api.methods.payment.params.PaymentParams;
import com.yandex.money.api.methods.payment.params.PhoneParams;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.yandex.money.android.PaymentActivity;

import static com.ration.qcode.application.utils.Constants.DAY;
import static com.ration.qcode.application.utils.Constants.HOUR;
import static com.ration.qcode.application.utils.Constants.MAIN_URL_CONST;
import static com.ration.qcode.application.utils.Constants.MINUTE;
import static com.ration.qcode.application.utils.Constants.MONTH;
import static com.ration.qcode.application.utils.Constants.YEAR;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DataBaseHelper dataBaseHelper;
    private ProgressDialog progressDialog;
    private static final int REQUEST_CODE = 1;
    private static final String CLIENT_ID = "F4C33E5FDE2D3BB4892D4FEBCAA10A8ED53525BAF383F4A2F76BBE8587CED803";
    private static final String HOST = "https://money.yandex.ru";
    private SharedPreferences preferences;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("MAINACTIVITY", MainActivity.class.toString());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataBaseHelper = DataBaseHelper.getInstance(getApplicationContext());

        preferences = getPreferences(MODE_PRIVATE);
        if(!preferences.getString("DOWNLOAD", "").equals("false")) {
            SharedPreferences.Editor ed = preferences.edit();
            ed.putString("DOWNLOAD", "false");
            ed.commit();
            //update();
            new UpdateLocalDbTasks().execute();
        }
        else setFragment(MainListFragment.class);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getInt(YEAR, 0) == 0) {
            setDateOfPay();
        }
    }

    private void setFragment(Class fragmentClass) {
        Fragment fragment = null;
        try {

            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.FlContent, fragment).commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Class FragmentClass = null;

        if (id == R.id.nav_main) {
            FragmentClass = MainListFragment.class;
        } else if (id == R.id.nav_analize) {
            FragmentClass = AnalyzesListFragment.class;
        } else if (id == R.id.nav_support) {
            FragmentClass = FeedbackFragment.class;
        } else if (id == R.id.nav_update) {
            update();
        } else if (id == R.id.nav_insert) {
            callDialogInsertProduct();
        } else if (id == R.id.nav_bill) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MAIN_URL_CONST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IGetPrice priceTasksApi = retrofit.create(IGetPrice.class);
            Call<PriceResponse> call = priceTasksApi.getPrice();
            call.enqueue(new Callback<PriceResponse>() {
                @Override
                public void onResponse(Call<PriceResponse> call, Response<PriceResponse> response) {
                    if (response.isSuccessful()) {
                        double price = response.body().getPrice();

                        PaymentParams phoneParams = PhoneParams.newInstance(getString(R.string.account), new BigDecimal(price));
                        Intent intent = PaymentActivity.getBuilder(MainActivity.this)
                                .setPaymentParams(phoneParams)
                                .setClientId(CLIENT_ID)
                                .setHost(HOST)
                                .build();
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }

                @Override
                public void onFailure(Call<PriceResponse> call, Throwable t) {

                }
            });
        } else {
        }

        try {
            setFragment(FragmentClass);
        } catch (NullPointerException e) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            setDateOfPay();
        }
    }

    private void setDateOfPay() {
        Calendar alarmStartTime = Calendar.getInstance();
        alarmStartTime.add(Calendar.DAY_OF_MONTH, 30);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().clear().apply();
        prefs.edit().putInt(YEAR, (alarmStartTime.getTime().getYear() + 1900)).apply();
        prefs.edit().putInt(MONTH, alarmStartTime.getTime().getMonth()).apply();
        prefs.edit().putInt(DAY, alarmStartTime.getTime().getDate()).apply();
        prefs.edit().putInt(HOUR, alarmStartTime.getTime().getHours()).apply();
        prefs.edit().putInt(MINUTE, alarmStartTime.getTime().getMinutes()).apply();
    }

    private void callDialogInsertProduct(){

        AddProductDialog dialog = new AddProductDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    public void update() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.wait));
        progressDialog.show();

        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(MAIN_URL_CONST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        //getAllTasksApi();
        //getAllMenuApi();
        //getAllDateApi();
        //getAllDateMenuApi();
        progressDialog.dismiss();
    }

    //TODO: надо выледить 2 метода enqueue в отдельный поток и потом их синхронизировать
    private void getAllTasksApi() {
        IGetAllDataAPI allTasksApi = retrofit.create(IGetAllDataAPI.class);
        Call<List<TasksResponse>> call = allTasksApi.getAllTasks();
        call.enqueue(new Callback<List<TasksResponse>>() {
            @Override
            public void onResponse(Call<List<TasksResponse>> call, Response<List<TasksResponse>> response) {
                Log.d("Tut", "Zashlo1");
                if (response.isSuccessful()) {
                    Log.d("Tut", "" + response.body().size());
                    for (int i = 0; i < response.body().size(); i++) {
                        TasksResponse list = response.body().get(i);
                        dataBaseHelper.insertIntoProduct(list.getName() + "|", list.getBelok().replace(",", "."),
                                list.getJiry().replace(",", "."), list.getUglevod().replace(",", "."),
                                list.getFa().replace(",", "."), list.getKkal().replace(",", "."), "100");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TasksResponse>> call, Throwable t) {
                Log.e("Tut", "Zashlo4");
                Log.e("Tut", String.valueOf(t));
            }
        });
    }

    //TODO: тут сделать добавление меню во внутреннюю БД из хостинга
    private void getAllMenuApi() {
        IGetAllMenuAPI allMenuAPI = retrofit.create(IGetAllMenuAPI.class);
        Call<List<MenuResponse>> menuResponses = allMenuAPI.getAllMenu();
        menuResponses.enqueue(new Callback<List<MenuResponse>>() {
            @Override
            public void onResponse(Call<List<MenuResponse>> call, Response<List<MenuResponse>> response) {
                Log.d("Tut", "Зашли в onResponse");
                if (response.isSuccessful()){
                    Log.d("Tut", String.valueOf(response.body().size()));
                    Log.d("Tut","Запрос удался, начинаем считывание данных");
                    for (int i = 0; i < response.body().size(); i++) {
                        MenuResponse menuResponse = response.body().get(i);
                        Log.d("Data_menu"+i,menuResponse.getMenu());
                        Log.d("Data_menu"+i,menuResponse.getDate());
                        Log.d("Data_menu"+i,menuResponse.getProduct());
                        Log.d("Data_menu"+i,menuResponse.getJiry());
                        Log.d("Data_menu"+i,menuResponse.getBelki());
                        Log.d("Data_menu"+i,menuResponse.getUglevod());
                        Log.d("Data_menu"+i,menuResponse.getFa());
                        Log.d("Data_menu"+i,menuResponse.getKl());
                        Log.d("Data_menu"+i,menuResponse.getGram());
                        if (dataBaseHelper.getMenuAndDate(menuResponse.getMenu(),menuResponse.getDate()).isEmpty()) {
                            dataBaseHelper.insertIntoMenu(menuResponse.getMenu(), menuResponse.getDate(), menuResponse.getProduct(), menuResponse.getJiry(),
                                    menuResponse.getBelki(), menuResponse.getUglevod(), menuResponse.getFa(), menuResponse.getKl(), menuResponse.getGram());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MenuResponse>> call, Throwable t) {
                Log.e("Tut","Ошибка при заходе в onResponse");
                Log.e("Tut", String.valueOf(t));
            }
        });
    }

    private void getAllDateMenuApi() {
        IGetAllMenuDateAPI menuDateAPI = retrofit.create(IGetAllMenuDateAPI.class);
        Call<List<DateMenuResponse>> call = menuDateAPI.getAllMenuDate();
        call.enqueue(new Callback<List<DateMenuResponse>>() {
            @Override
            public void onResponse(Call<List<DateMenuResponse>> call, Response<List<DateMenuResponse>> response) {
                Log.d("Tut", "Зашли в onResponse в getAllDateMenuApi()");
                if (response.isSuccessful()) {
                    Log.d("Tut", "Зашли в onResponse в getAllDateMenuApi() и прошли onSuccessful()"  );
                    for (int i = 0; i < response.body().size(); i++) {
                        DateMenuResponse list = response.body().get(i);
                        Log.d("Data_date_menu", list.getDate());
                        Log.d("Data_date_menu", list.getMenu());
                        if (dataBaseHelper.getMenuAndDate(list.getMenu(),list.getDate()).isEmpty()) {
                            dataBaseHelper.insertMenuDates(list.getMenu(), list.getDate());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DateMenuResponse>> call, Throwable t) {
                Log.e("Tut","Ошибка при заходе в onResponse в getAllDateMenuApi()");
                Log.e("Tut", String.valueOf(t));
            }
        });
    }

    private void getAllDateApi() {
       IGetAllDateAPI getAllDateAPI = retrofit.create(IGetAllDateAPI.class);
       Call<List<DateResponse>> call = getAllDateAPI.getAllDate();
       call.enqueue(new Callback<List<DateResponse>>() {
           @Override
           public void onResponse(Call<List<DateResponse>> call, Response<List<DateResponse>> response) {
               Log.d("Tut", "Зашли в onResponse в getAllDateApi()");
               if (response.isSuccessful()) {
                   Log.d("Tut", "Зашли в onResponse в getAllDateApi() и прошли onSuccessful()"  );
                   for (int i = 0; i < response.body().size(); i++) {
                       DateResponse list = response.body().get(i);
                       Log.d("Data_date", list.getDate());
                       if (dataBaseHelper.getDates(list.getDate()).isEmpty()) {
                           dataBaseHelper.insertDate(list.getDate());
                       }
                   }
               }
           }

           @Override
           public void onFailure(Call<List<DateResponse>> call, Throwable t) {
               Log.e("Tut","Ошибка при заходе в onResponse в getAllDateApi()");
               Log.e("Tut", String.valueOf(t));
           }
       });
    }

    private class UpdateLocalDbTasks extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            getAllTasksApi();
            getAllMenuApi();
            getAllDateApi();
            getAllDateMenuApi();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            setFragment(MainListFragment.class);
        }
    }
}
