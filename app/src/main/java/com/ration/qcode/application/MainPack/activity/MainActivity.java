package com.ration.qcode.application.MainPack.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ration.qcode.application.MainPack.dialog.AddProductDialog;
import com.ration.qcode.application.MainPack.dialog.ChangeURLDialog;
import com.ration.qcode.application.MainPack.fragment.AnalyzesListFragment;

import com.ration.qcode.application.MainPack.fragment.MainListFragment;
import com.ration.qcode.application.ProductDataBase.DataBaseHelper;
import com.ration.qcode.application.R;
import com.ration.qcode.application.utils.AdapterUpdatable;
import com.ration.qcode.application.utils.NetworkService;
import com.ration.qcode.application.utils.SharedPrefManager;
import com.ration.qcode.application.utils.internet.ComplicatedResponse;
import com.ration.qcode.application.utils.internet.DateMenuResponse;
import com.ration.qcode.application.utils.internet.DateResponse;
import com.ration.qcode.application.utils.internet.IGetAllComplicatedAPI;
import com.ration.qcode.application.utils.internet.IGetAllDataAPI;
import com.ration.qcode.application.utils.internet.IGetAllDateAPI;
import com.ration.qcode.application.utils.internet.IGetAllMenuAPI;
import com.ration.qcode.application.utils.internet.IGetAllMenuDateAPI;
import com.ration.qcode.application.utils.internet.MenuResponse;
import com.ration.qcode.application.utils.internet.TasksResponse;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ration.qcode.application.utils.Constants.DAY;
import static com.ration.qcode.application.utils.Constants.HOUR;
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
    //private Retrofit retrofit;

    private AdapterUpdatable mUpdatable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(MAIN_URL_CONST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();*/
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.wait));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataBaseHelper = DataBaseHelper.getInstance(getApplicationContext());

        preferences = getPreferences(MODE_PRIVATE);
        setFragment(MainListFragment.class);
        if(!preferences.getString("DOWNLOAD", "").equals("false")) {
            SharedPreferences.Editor ed = preferences.edit();
            ed.putString("DOWNLOAD", "false");
            ed.commit();

            SharedPrefManager.getManager(this).setUrl("https://sh1604917.a.had.su");


            new Async().execute();
            //update();
        }

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
            mUpdatable= (AdapterUpdatable) fragment;
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Class FragmentClass = null;

        if (id == R.id.nav_main) {
            FragmentClass = MainListFragment.class;
        } else if (id == R.id.nav_analize) {
            FragmentClass = AnalyzesListFragment.class;
        } else if (id == R.id.nav_update) {
            //update();
            DataBaseHelper.getInstance(this).removeAllBeforeUpdate();
            new Async().execute();

        } else if (id == R.id.nav_insert) {
            callDialogInsertProduct();
        } else if (id==R.id.nav_change_link){
            callDialogChangeURL();
        }

        try {
            setFragment(FragmentClass);
        } catch (NullPointerException e) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callDialogChangeURL() {
        ChangeURLDialog dialog=new ChangeURLDialog();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(),null);
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
        getAllTasksApi();
        getAllMenuApi();
        getComplicatedMenu();
        getAllDateApi();
        getAllDateMenuApi();
    }

    private void getAllTasksApi() {
        IGetAllDataAPI allTasksApi=NetworkService.getInstance(SharedPrefManager.getManager(this).getUrl()).getApi(IGetAllDataAPI.class);
        Call<List<TasksResponse>> call = allTasksApi.getAllTasks();
        call.enqueue(new Callback<List<TasksResponse>>() {
            @Override
            public void onResponse(Call<List<TasksResponse>> call, Response<List<TasksResponse>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        TasksResponse list = response.body().get(i);
                        dataBaseHelper.insertIntoProduct(list.getName() + "|", list.getBelok().replace(",", "."),
                                list.getJiry().replace(",", "."), list.getUglevod().replace(",", "."),
                                list.getFa().replace(",", "."), list.getKkal().replace(",", "."), list.getGram(),list.getComplicated());
                    }
                }
                mUpdatable.updateAdapter();
                //progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<TasksResponse>> call, Throwable t) {
            }
        });
    }

    private void getAllMenuApi() {
        IGetAllMenuAPI allMenuAPI=NetworkService.getInstance(SharedPrefManager.getManager(this).getUrl()).getApi(IGetAllMenuAPI.class);
        Call<List<MenuResponse>> menuResponses = allMenuAPI.getAllMenu();
        menuResponses.enqueue(new Callback<List<MenuResponse>>() {
            @Override
            public void onResponse(Call<List<MenuResponse>> call, Response<List<MenuResponse>> response) {
                if (response.isSuccessful()){
                    for (int i = 0; i < response.body().size(); i++) {
                        MenuResponse menuResponse = response.body().get(i);
                        if (dataBaseHelper.getMenu(menuResponse.getMenu(),menuResponse.getDate(),menuResponse.getProduct()).isEmpty()) {
                            dataBaseHelper.insertIntoMenu(menuResponse.getMenu(), menuResponse.getDate(), menuResponse.getProduct(), menuResponse.getJiry(),
                                    menuResponse.getBelki(), menuResponse.getUglevod(), menuResponse.getFa(), menuResponse.getKl(), menuResponse.getGram(),menuResponse.getComplicated());
                        }
                    }
                    mUpdatable.updateAdapter();
                }
                //progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<MenuResponse>> call, Throwable t) {}
        });
    }

    private void getComplicatedMenu(){
        IGetAllComplicatedAPI complicatedAPI=NetworkService.getInstance(SharedPrefManager.getManager(this).getUrl()).getApi(IGetAllComplicatedAPI.class);
        Call<List<ComplicatedResponse>> call=complicatedAPI.queryComplicated();
        call.enqueue(new Callback<List<ComplicatedResponse>>() {
            @Override
            public void onResponse(Call<List<ComplicatedResponse>> call, Response<List<ComplicatedResponse>> response) {
                if (response.isSuccessful()){
                    for (int i = 0; i < response.body().size(); i++) {
                        ComplicatedResponse complicatedResponse = response.body().get(i);
                            dataBaseHelper.insertIntoComplicated
                                    (complicatedResponse.getName(), complicatedResponse.getProduct(), complicatedResponse.getJiry(),
                                    complicatedResponse.getBelki(), complicatedResponse.getUglevod(), complicatedResponse.getFa(),
                                            complicatedResponse.getKl(), complicatedResponse.getGram(),complicatedResponse.getComplicated());
                    }
                    mUpdatable.updateAdapter();
                }
                //progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<ComplicatedResponse>> call, Throwable t) {
            }
        });



    }

    private void getAllDateMenuApi() {
        IGetAllMenuDateAPI menuDateAPI=NetworkService.getInstance(SharedPrefManager.getManager(this).getUrl()).getApi(IGetAllMenuDateAPI.class);
        Call<List<DateMenuResponse>> call = menuDateAPI.getAllMenuDate();
        call.enqueue(new Callback<List<DateMenuResponse>>() {
            @Override
            public void onResponse(Call<List<DateMenuResponse>> call, Response<List<DateMenuResponse>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        DateMenuResponse list = response.body().get(i);
                        if (dataBaseHelper.getMenuAndDate(list.getMenu(),list.getDate()).isEmpty()) {
                            dataBaseHelper.insertMenuDates(list.getMenu(), list.getDate());
                        }
                    }
                    mUpdatable.updateAdapter();
                }
                //progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<DateMenuResponse>> call, Throwable t) {
            }
        });
    }

    private void getAllDateApi() {
        IGetAllDateAPI getAllDateAPI=NetworkService.getInstance(SharedPrefManager.getManager(this).getUrl()).getApi(IGetAllDateAPI.class);
        Call<List<DateResponse>> call = getAllDateAPI.getAllDate();
       call.enqueue(new Callback<List<DateResponse>>() {
           @Override
           public void onResponse(Call<List<DateResponse>> call, Response<List<DateResponse>> response) {
               if (response.isSuccessful()) {
                   for (int i = 0; i < response.body().size(); i++) {
                       DateResponse list = response.body().get(i);
                       if (dataBaseHelper.getDates(list.getDate()).isEmpty()) {
                           dataBaseHelper.insertDate(list.getDate());
                       }

                   }
                   mUpdatable.updateAdapter();
               }
           }

           @Override
           public void onFailure(Call<List<DateResponse>> call, Throwable t) {
           }
       });
    }


    public class Async extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            update();
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean rez) {
            super.onPostExecute(rez);
            mUpdatable.updateAdapter();
            progressDialog.dismiss();
            //if (rez) progressDialog.dismiss();
        }
    }
}
