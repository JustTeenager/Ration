package com.ration.qcode.application.ProductDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author zeza on 06.04.2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";
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
            + GRAM + " TEXT)";

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
            + GRAM + " TEXT)";


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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + TABLE_ALLPRODUCTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + TABLE_MENU);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + TABLE_ANALIZES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + TABLE_MENUES_DATES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + TABLE_DATE);

        onCreate(sqLiteDatabase);
    }

    public void insertIntoMenu
            (String menu, String date, String product,
             String jiry, String belki,
             String uglevod, String fa, String kl, String gram) {

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
        db.insert(TABLE_MENU, null, values);
        db.close();
    }

    public void insertIntoProduct
            (String product,
             String jiry, String belki,
             String uglevod, String fa, String kl, String gram) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT, product);
        values.put(JIRY, jiry);
        values.put(UGLEVOD, uglevod);
        values.put(BELKI, belki);
        values.put(FA, fa);
        values.put(KL, kl);
        values.put(GRAM, gram);
        db.insertWithOnConflict(TABLE_ALLPRODUCTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.e("values", product);
        db.close();
    }


    public ArrayList<String> getALLProduct(String query) {
        ArrayList<String> all = new ArrayList<>();

        String selectQuery = "SELECT * FROM "
                + TABLE_ALLPRODUCTS + " WHERE product LIKE '%" + query + "%'";
        dbR = this.getReadableDatabase();
        Cursor c = dbR.rawQuery(selectQuery, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            all.add(c.getString(c.getColumnIndex(PRODUCT)) + " "
                    + c.getString(c.getColumnIndex(BELKI)) + " "
                    + c.getString(c.getColumnIndex(JIRY)) + " "
                    + c.getString(c.getColumnIndex(UGLEVOD)) + " "
                    + c.getString(c.getColumnIndex(FA)) + " "
                    + c.getString(c.getColumnIndex(KL)) + " "
                    + c.getString(c.getColumnIndex(GRAM)));
        }
        c.close();
        //  dbR.close();
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
        //  dbR.close();
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
        //  dbR.close();
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
                        + c.getString(c.getColumnIndex(GRAM)));
                Log.e("nu", all.get(c.getPosition()));
            }
            c.close();
            //  dbR.close();
        }
        return all;
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
        Log.e("DATE", date);
        c.close();
        // dbR.close();
        return all;
    }

    public void insertDate(String date) {
        dbW = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE, date);
        Log.e("dateBASE", date);
        dbW.insert(TABLE_DATE, null, values);

    }

    public void insertMenuDates(String menu, String date) {
        dbW = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE, date);
        values.put(ID_MENU, menu);
        dbW.insert(TABLE_MENUES_DATES, null, values);
        //  dbW.close();
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
            }
        }
        //dbW.close();
    }


    public void removeFromMenu(String date, String menu, String product) {
        dbW = this.getWritableDatabase();
        try {
            String removeMenu = "DELETE FROM " + TABLE_MENU + " WHERE TRIM(" + DATE + ") = '" + date.trim() + "' AND "
                    + "(" + ID_MENU + ") = '" + menu + "' AND "
                    + "(" + PRODUCT + ") = '" + product + "'";
            dbW.execSQL(removeMenu);
            Log.e("getMenus", getMenues(date) + "");
            if (getMenues(date).isEmpty()) {

                String removeDate = "DELETE FROM " + TABLE_DATE + " WHERE TRIM(" + DATE + ") = '" + date.trim() + "'";
                dbW.execSQL(removeDate);
            }
        } catch (Exception e) {
        }

    }


    public void insertIntoAnalize(String date, String FA, String notice) {
        dbW = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE, date);
        values.put(this.FA, FA);
        values.put(NOTICE, notice);
        Log.e("dateBASE", date);
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
        // dbR.close();
        return all;
    }


    public static synchronized DataBaseHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DataBaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }
}


