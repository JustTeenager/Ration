package com.ration.qcode.application.ProductDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ration.qcode.application.utils.Constants;
import com.ration.qcode.application.utils.NetworkService;
import com.ration.qcode.application.utils.SharedPrefManager;
import com.ration.qcode.application.utils.internet.DateMenuResponse;
import com.ration.qcode.application.utils.internet.DeleteFromDateAPI;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author zeza on 06.04.2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";
    private static final String COMPLICATED = "complicated";
    private static final String TABLE_COMPLICATED = "complicateds";
    private static final String COMPLICATED_NAME = "Name";
    private static DataBaseHelper mInstance = null;
    private Context mCtx;
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_DATE = "dates";
    public static final String TABLE_MENUES_DATES = "dates_menus";
    public static final String TABLE_MENU = "menus";
    public static final String TABLE_ANALIZES = "analizes";
    public static final String TABLE_ALLPRODUCTS = "products";
    private final String ID_MENU = "menu";
    private final String DATE = "date";
    private final String PRODUCT = "product";
    private final String JIRY = "jiry";
    private final String BELKI = "belki";
    private final String UGLEVOD = "uglevod";
    private final String FA = "FA";
    private final String KL = "kl";
    private final String GRAM = "gram";
    private final String NOTICE = "notice";
    private SQLiteDatabase dbW;
    private SQLiteDatabase dbR;

    private final String CREATE_TABLE_ALLPRODUCTS = "CREATE TABLE " + TABLE_ALLPRODUCTS + "(" +
            PRODUCT + " TEXT PRIMARY KEY, "
            + JIRY + " TEXT,"
            + UGLEVOD + " TEXT,"
            + BELKI + " TEXT,"
            + FA + " TEXT,"
            + KL + " TEXT,"
            + GRAM + " TEXT,"
            + COMPLICATED + " TEXT)";

    private final String CREATE_TABLE_ANALIZES = "CREATE TABLE " + TABLE_ANALIZES +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DATE + " DATETIME,"
            + FA + " TEXT,"
            + NOTICE + " TEXT)";


    private final String CREATE_TABLE_DATE = "CREATE TABLE " + TABLE_DATE // даты
            + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DATE + " DATETIME)";

    private final String CREATE_TABLE_DATES_MENUS = "CREATE TABLE " + TABLE_MENUES_DATES // меню-даты
            + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ID_MENU + " DATETIME,"
            + DATE + " DATETIME)";

    private final String CREATE_TABLE_MENU = "CREATE TABLE " + TABLE_MENU +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ID_MENU + " DATETIME,"
            + DATE + " DATETIME,"
            + PRODUCT + " TEXT,"
            + JIRY + " TEXT,"
            + UGLEVOD + " TEXT,"
            + BELKI + " TEXT,"
            + FA + " TEXT,"
            + KL + " TEXT,"
            + GRAM + " TEXT,"
            + COMPLICATED + " TEXT)";

    private final String CREATE_TABLE_COMPLICATED_PRODUCT = "CREATE TABLE " + TABLE_COMPLICATED +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COMPLICATED_NAME + " TEXT,"
            + PRODUCT + " TEXT,"
            + JIRY + " TEXT,"
            + UGLEVOD + " TEXT,"
            + BELKI + " TEXT,"
            + FA + " TEXT,"
            + KL + " TEXT,"
            + GRAM + " TEXT,"
            + COMPLICATED + " TEXT)";


    public DataBaseHelper(Context context) {
        super(context, "DATABASE", null, DATABASE_VERSION);
        this.mCtx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ANALIZES);
        sqLiteDatabase.execSQL(CREATE_TABLE_DATES_MENUS);
        sqLiteDatabase.execSQL(CREATE_TABLE_MENU);
        sqLiteDatabase.execSQL(CREATE_TABLE_DATE);
        sqLiteDatabase.execSQL(CREATE_TABLE_ALLPRODUCTS);
        sqLiteDatabase.execSQL(CREATE_TABLE_COMPLICATED_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ALLPRODUCTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ANALIZES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MENUES_DATES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DATE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLICATED);

        onCreate(sqLiteDatabase);
    }

    public void insertIntoMenu
            (String menu, String date, String product,
             String jiry, String belki,
             String uglevod, String fa, String kl, String gram,String complicated) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_MENU, menu);
        values.put(DATE, date);
        values.put(PRODUCT, product);
        values.put(JIRY, jiry);
        values.put(UGLEVOD, uglevod);
        values.put(BELKI, belki);
        values.put(FA, fa);
        values.put(KL, kl);
        values.put(GRAM, gram);
        values.put(COMPLICATED,complicated);
        db.insertWithOnConflict(TABLE_MENU, null, values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<Intent> getFromComplicated(String name){
        ArrayList<Intent> intentArrayList=new ArrayList<>();
           String selectQuery = "SELECT * FROM "
                    + TABLE_COMPLICATED + " WHERE Name = '" + name + "'";
        dbR = this.getReadableDatabase();
        Cursor c = dbR.rawQuery(selectQuery, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Intent intent=new Intent();
            intent.putExtra(Constants.PRODUCTS,c.getString(c.getColumnIndex(PRODUCT)));
            intent.putExtra(Constants.PROTEINS,c.getString(c.getColumnIndex(BELKI)));
            intent.putExtra(Constants.FATS,c.getString(c.getColumnIndex(JIRY)));
            intent.putExtra(Constants.CARBOHYDRATES,c.getString(c.getColumnIndex(UGLEVOD)));
            intent.putExtra(Constants.FA,c.getString(c.getColumnIndex(FA)));
            intent.putExtra(Constants.KL,c.getString(c.getColumnIndex(KL)));
            intent.putExtra(Constants.GR,c.getString(c.getColumnIndex(GRAM)));
            intent.putExtra(Constants.COMPLICATED,c.getString(c.getColumnIndex(COMPLICATED)));
            intentArrayList.add(intent);
        }
        c.close();

        return intentArrayList;
    }

    public boolean getCheckFromComplicated(String name,String product){
        String selectQuery = "SELECT * FROM "
                + TABLE_COMPLICATED + " WHERE Name LIKE '%" + name + "%' AND " +"(" + PRODUCT + ") = '"+product+ "'";
        dbR = this.getReadableDatabase();
        Cursor c = dbR.rawQuery(selectQuery, null);
        boolean bool= c.getCount() > 0;
        c.close();
        dbR.close();
        return bool;
    }

    public boolean getCheckFromDate(String date){
        String selectQuery = "SELECT * FROM "
                + TABLE_DATE + " WHERE ("+DATE+") = '"+date+"'";
        dbR = this.getReadableDatabase();
        Cursor c = dbR.rawQuery(selectQuery, null);
        boolean bool= c.getCount() == 0;
        c.close();
        dbR.close();
        return bool;
    }

    public void insertIntoComplicated
            (String name, String product,
             String jiry, String belki,
             String uglevod, String fa, String kl, String gram,String complicated) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COMPLICATED_NAME, name);
        values.put(PRODUCT, product);
        values.put(JIRY, jiry);
        values.put(UGLEVOD, uglevod);
        values.put(BELKI, belki);
        values.put(FA, fa);
        values.put(KL, kl);
        values.put(GRAM, gram);
        values.put(COMPLICATED,complicated);
        db.insertWithOnConflict(TABLE_COMPLICATED, null, values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void insertIntoProduct
            (String product,
             String jiry, String belki,
             String uglevod, String fa, String kl, String gram,String complicated) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT, product);
        values.put(JIRY, jiry);
        values.put(UGLEVOD, uglevod);
        values.put(BELKI, belki);
        values.put(FA, fa);
        values.put(KL, kl);
        values.put(GRAM, gram);
        values.put(COMPLICATED,complicated);
        db.insertWithOnConflict(TABLE_ALLPRODUCTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }


    public ArrayList<String> getALLProduct(String query) {
        ArrayList<String> all = new ArrayList<>();
        String selectQuery;
        if (query!=null && !query.isEmpty()) {
            selectQuery = "SELECT * FROM "
                    + TABLE_ALLPRODUCTS + " WHERE product LIKE '%" + query + "%'";
        }
        else{
            selectQuery = "SELECT * FROM "
                    + TABLE_ALLPRODUCTS;
        }
        dbR = this.getReadableDatabase();
        Cursor c = dbR.rawQuery(selectQuery, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            all.add(c.getString(c.getColumnIndex(PRODUCT)) + " "
                    + c.getString(c.getColumnIndex(BELKI)) + " "
                    + c.getString(c.getColumnIndex(JIRY)) + " "
                    + c.getString(c.getColumnIndex(UGLEVOD)) + " "
                    + c.getString(c.getColumnIndex(FA)) + " "
                    + c.getString(c.getColumnIndex(KL)) + " "
                    + c.getString(c.getColumnIndex(GRAM))+ " "
                    + c.getString(c.getColumnIndex(COMPLICATED)));
        }
        c.close();
        return all;
    }

    public ArrayList<String> getMenu1screen(String date, String menu) {
        ArrayList<String> all = new ArrayList<>();
        String selectQuery = "SELECT FA, belki,kl FROM "
                + TABLE_MENU +
                " WHERE TRIM(" + DATE + ") = '" + date.trim() + "' AND "
                + "(" + ID_MENU + ") = '" + menu + "'";

        dbR = this.getReadableDatabase();
        Cursor c = dbR.rawQuery(selectQuery, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            all.add(c.getString(c.getColumnIndex(FA)) + " "
                    + c.getString(c.getColumnIndex(BELKI)) + " "
                    + c.getString(c.getColumnIndex(KL)));
        }
        c.close();
        return all;
    }

    public ArrayList<String> getDates() {
        ArrayList<String> all = new ArrayList<>();
        String selectQuery = "SELECT * FROM "
                + TABLE_DATE;
        dbR = this.getReadableDatabase();
        Cursor c = dbR.rawQuery(selectQuery, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            all.add(c.getString(c.getColumnIndex(DATE)));
        }
        c.close();
        Collections.reverse(all);
        return all;
    }

    public ArrayList<String> getDates(String date) {
        ArrayList<String> all = new ArrayList<>();
        String selectQuery = "SELECT * FROM "
                + TABLE_DATE+
                " WHERE TRIM(" + DATE + ") = '" + date.trim() + "'";
        dbR = this.getReadableDatabase();
        Cursor c = dbR.rawQuery(selectQuery, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            all.add(c.getString(c.getColumnIndex(DATE)));
        }
        c.close();
        Collections.reverse(all);
        return all;
    }

    public ArrayList<String> getProducts(String date, String menu) {
        ArrayList<String> all = new ArrayList<>();
        if (date == null || menu == null) {

        } else {
            String selectQuery = "SELECT * FROM "
                    + TABLE_MENU +
                    " WHERE TRIM(" + DATE + ") = '" + date.trim() + "' AND "
                    + "(" + ID_MENU + ") = '" + menu.trim() + "'";
            dbR = this.getReadableDatabase();
            Cursor c = dbR.rawQuery(selectQuery, null);
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                all.add(c.getString(c.getColumnIndex(PRODUCT)) + " "
                        + c.getString(c.getColumnIndex(BELKI)) + " "
                        + c.getString(c.getColumnIndex(JIRY)) + " "
                        + c.getString(c.getColumnIndex(UGLEVOD)) + " "
                        + c.getString(c.getColumnIndex(FA)) + " "
                        + c.getString(c.getColumnIndex(KL)) + " "
                        + c.getString(c.getColumnIndex(GRAM))+" "
                        + c.getString(c.getColumnIndex(COMPLICATED)));
            }
            c.close();
        }
        return all;
    }

    public boolean getCheckGrFromComplicated(String name,String product,String gr){
        String selectQuery = "SELECT * FROM "
                + TABLE_COMPLICATED + " WHERE Name LIKE '%" + name + "%' AND " +"(" + PRODUCT + ") = '"+product+ "'"+" AND " +"("+GRAM+") = '"+gr+ "'";
        dbR = this.getReadableDatabase();
        Cursor c = dbR.rawQuery(selectQuery, null);
        boolean bool= c.getCount() == 0;
        c.close();
        dbR.close();
        return bool;
    }

    public void updateGrams(String name,String product,
                            String jiry, String belki,
                            String uglevod, String fa, String kl, String gram){

        String selectQuery = "UPDATE "
                + TABLE_COMPLICATED + " SET (" + GRAM + ") ='"+gram+
                "', ("+ JIRY + ") ='"+jiry+
                "', ("+ BELKI + ") ='"+belki+
                "', ("+ UGLEVOD + ") ='"+uglevod+
                "', ("+ FA + ") ='"+fa+
                "', ("+ KL + ") ='"+kl+
                "' WHERE "+"("+ COMPLICATED_NAME + ") = '"+name+"'"+
                " AND "+"("+ PRODUCT + ") = '"+product+"'";

        dbW = this.getWritableDatabase();
        dbW.execSQL(selectQuery);
    }

    public ArrayList<String> getMenues(String date) {
        ArrayList<String> all = new ArrayList<>();
        String selectQuery = "SELECT * FROM "
                + TABLE_MENUES_DATES + " WHERE TRIM(date) = '" + date.trim() + "'";
        dbR = this.getReadableDatabase();
        Cursor c = dbR.rawQuery(selectQuery, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            all.add(c.getString(c.getColumnIndex(ID_MENU)));
        }
        c.close();
        return all;
    }

    public ArrayList<DateMenuResponse> getMenuAndDate(String menu, String date) {
        ArrayList<DateMenuResponse> all = new ArrayList<>();
        String selectQuery = "SELECT * FROM "
                + TABLE_MENUES_DATES +
                " WHERE TRIM(" + ID_MENU + ") = '" + menu.trim() + "' AND "
                + "(" + DATE + ") = '" + date.trim() + "'";
        dbR = this.getReadableDatabase();
        Cursor c = dbR.rawQuery(selectQuery, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            DateMenuResponse response = new DateMenuResponse();
            response.setMenu(c.getString(c.getColumnIndex(ID_MENU)));
            response.setDate(c.getString(c.getColumnIndex(DATE)));
            all.add(response);
        }
        c.close();
        return all;
    }

    public ArrayList<DateMenuResponse> getMenu(String menu, String date,String product) {

        ArrayList<DateMenuResponse> all = new ArrayList<>();
        String selectQuery = "SELECT * FROM "
                + TABLE_MENU +
                " WHERE TRIM(" + ID_MENU + ") = '" + menu.trim() +
                "' AND "
                + "(" + DATE + ") = '" + date.trim() + "'"
                + " AND " + "(" + PRODUCT + ") = '" + product.trim() + "'";
        dbR = this.getReadableDatabase();
        Cursor c = dbR.rawQuery(selectQuery, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            DateMenuResponse response = new DateMenuResponse();
            response.setMenu(c.getString(c.getColumnIndex(ID_MENU)));
            response.setDate(c.getString(c.getColumnIndex(DATE)));
            all.add(response);
        }
        c.close();
        return all;
    }

    public void insertDate(String date) {
        dbW = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE, date);
        dbW.insertWithOnConflict(TABLE_DATE, null, values,SQLiteDatabase.CONFLICT_REPLACE);

    }

    public void insertMenuDates(String menu, String date) {
        dbW = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE, date);
        values.put(ID_MENU, menu);
        dbW.insertWithOnConflict(TABLE_MENUES_DATES, null, values,SQLiteDatabase.CONFLICT_REPLACE);
    }


    public void removeFromMenu(String date, String menu) {
        dbW = this.getWritableDatabase();
        if (date == null || menu == null) {

        } else {
            String removeMenu = "DELETE FROM " + TABLE_MENU + " WHERE TRIM(" + DATE + ") = '" + date.trim() + "' AND "
                    + "(" + ID_MENU + ") = '" + menu + "'";
            String removeMenuDates = "DELETE FROM " + TABLE_MENUES_DATES + " WHERE TRIM(" + DATE + ") = '" + date.trim() + "' AND "
                    + "(" + ID_MENU + ") = '" + menu + "'";
            dbW.execSQL(removeMenu);
            dbW.execSQL(removeMenuDates);
            if (getMenues(date).isEmpty()) {
                String removeDate = "DELETE FROM " + TABLE_DATE + " WHERE TRIM(" + DATE + ") = '" + date.trim() + "'";
                dbW.execSQL(removeDate);
                //removeDateFromHosting(date);
            }
        }
    }

    public void deleteFromMenu(String date, String menu,Context context) {
        dbW = this.getWritableDatabase();
        if (date == null || menu == null) {

        } else {
            String removeMenu = "DELETE FROM " + TABLE_MENU + " WHERE TRIM(" + DATE + ") = '" + date.trim() + "' AND "
                    + "(" + ID_MENU + ") = '" + menu + "'";
            String removeMenuDates = "DELETE FROM " + TABLE_MENUES_DATES + " WHERE TRIM(" + DATE + ") = '" + date.trim() + "' AND "
                    + "(" + ID_MENU + ") = '" + menu + "'";
            dbW.execSQL(removeMenu);
            dbW.execSQL(removeMenuDates);
            if (getMenues(date).isEmpty()) {
                String removeDate = "DELETE FROM " + TABLE_DATE + " WHERE TRIM(" + DATE + ") = '" + date.trim() + "'";
                dbW.execSQL(removeDate);
                removeDateFromHosting(date,context);
            }
        }
    }

    private void removeDateFromHosting(String date,Context context){

        DeleteFromDateAPI deleteFromDateAPI= NetworkService.getInstance(SharedPrefManager.getManager(context).getUrl()).getApi(DeleteFromDateAPI.class);
        Call<String> call=deleteFromDateAPI.removeDate(date);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    public void removeFromMenu(String date, String menu, String product) {
        dbW = this.getWritableDatabase();
        try {
            String removeMenu = "DELETE FROM " + TABLE_MENU + " WHERE TRIM(" + DATE + ") = '" + date.trim() + "' AND "
                    + "(" + ID_MENU + ") = '" + menu + "' AND "
                    + "(" + PRODUCT + ") = '" + product + "'";
            dbW.execSQL(removeMenu);
            if (getMenues(date).isEmpty()) {
                String removeDate = "DELETE FROM " + TABLE_DATE + " WHERE TRIM(" + DATE + ") = '" + date.trim() + "'";
                dbW.execSQL(removeDate);
                //removeDateFromHosting(date);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteFromMenu(String date, String menu, String product,Context context) {
        dbW = this.getWritableDatabase();
        try {
            String removeMenu = "DELETE FROM " + TABLE_MENU + " WHERE TRIM(" + DATE + ") = '" + date.trim() + "' AND "
                    + "(" + ID_MENU + ") = '" + menu + "' AND "
                    + "(" + PRODUCT + ") = '" + product + "'";
            dbW.execSQL(removeMenu);
            if (getMenues(date).isEmpty()) {
                String removeDate = "DELETE FROM " + TABLE_DATE + " WHERE TRIM(" + DATE + ") = '" + date.trim() + "'";
                dbW.execSQL(removeDate);
                removeDateFromHosting(date,context);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void changeLocalDBAfterDeletingInRecView(String name,String product) {
        dbW=this.getWritableDatabase();
        String removeCompl = "DELETE FROM " + TABLE_COMPLICATED + " WHERE (" + COMPLICATED_NAME + ") = '" + name + "' AND "
                + "(" + PRODUCT + ") = '" + product + "'";
        dbW.execSQL(removeCompl);
    }

    public void updateProductComplicated(String name,
    String jiry, String belki,
    String uglevod, String fa, String kl){

        dbW = this.getWritableDatabase();

        String updateProduct = "UPDATE " + TABLE_ALLPRODUCTS + " SET " +
                "("+ JIRY + ") ='"+jiry+
                "', ("+ BELKI + ") ='"+belki+
                "', ("+ UGLEVOD + ") ='"+uglevod+
                "', ("+ FA + ") ='"+fa+
                "', ("+ KL + ") ='"+kl+"'"+
                " WHERE (" + PRODUCT + ") = '" + name + "'";

        dbW.execSQL(updateProduct);
    }



    public void insertIntoAnalize(String date, String FA, String notice) {
        dbW = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE, date);
        values.put(this.FA, FA);
        values.put(NOTICE, notice);
        dbW.insert(TABLE_ANALIZES, null, values);

    }

    public void deleteAnalize(String date) {
        dbW = this.getWritableDatabase();
        String removeANAL = "DELETE FROM " + TABLE_ANALIZES + " WHERE TRIM(" + DATE + ") = '" + date.trim() + "'";
        dbW.execSQL(removeANAL);
    }

    public ArrayList<String> getAnalizes() {
        ArrayList<String> all = new ArrayList<>();
        String selectQuery = "SELECT * FROM "
                + TABLE_ANALIZES;
        dbR = this.getReadableDatabase();
        Cursor c = dbR.rawQuery(selectQuery, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            all.add(c.getString(c.getColumnIndex(FA)) + " "
                    + c.getString(c.getColumnIndex(DATE)) + " "
                    + c.getString(c.getColumnIndex(NOTICE)));
        }
        c.close();
        return all;
    }



    public static synchronized DataBaseHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DataBaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public void removeComplicatedProduct(String productName) {
            this.dbW=getWritableDatabase();
            String query="DELETE FROM "+TABLE_COMPLICATED+" WHERE "+"("+COMPLICATED_NAME+") = '"+productName+"'" ;
            dbW.execSQL(query);
    }

    public void removeProduct(String productName) {
        this.dbW=getWritableDatabase();
        String query="DELETE FROM "+TABLE_ALLPRODUCTS+" WHERE "+"("+PRODUCT+")='"+productName+"'" ;
        dbW.execSQL(query);
    }

    public void removeAllBeforeUpdate(){
        this.dbW=getWritableDatabase();
        String queryCompl="DELETE FROM "+TABLE_COMPLICATED;
        String queryDate="DELETE FROM "+TABLE_DATE;
        String queryMenuDate="DELETE FROM "+TABLE_MENUES_DATES;
        String queryComplProduct="DELETE FROM "+TABLE_ALLPRODUCTS+" WHERE "+"("+COMPLICATED+")='1'";
        String queryMenu="DELETE FROM "+TABLE_MENU;

        dbW.execSQL(queryDate);
        dbW.execSQL(queryCompl);
        dbW.execSQL(queryMenuDate);
        dbW.execSQL(queryComplProduct);
        dbW.execSQL(queryMenu);
    }
}


