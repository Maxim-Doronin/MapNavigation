// Copyright (c) 2017. All rights reserved.
// Author: Maxim Doronin <maximdoronin@yandex-team.ru>

package com.maximdoronin.mapnavigation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class MarkerSaverSql extends SQLiteOpenHelper implements MarkerSaver {
    private static MarkerSaverSql mInstance = null;

    private SQLiteDatabase mDatabase;

    public static MarkerSaver get() {
        return mInstance;
    }

    public static void init(Context context) {
        mInstance = new MarkerSaverSql(context);
    }

    private MarkerSaverSql(Context context) {
        super(context, "ElementsDataBase", null, 2);
        mDatabase = getWritableDatabase();
    }

    @Override
    public void addElement(Marker marker) {
        mDatabase.execSQL("INSERT INTO records VALUES (" +
                "\"" + marker.getPosition().latitude + "\"," +
                "\"" + marker.getPosition().longitude + "\"," +
                "\"" + marker.getTitle() + "\"" + ");");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        mDatabase = sqLiteDatabase;
        mDatabase.execSQL(
                "CREATE TABLE records (ELLAT REAL, ELLONG REAL, ELTITLE TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        mDatabase = db;
    }

    public List<Marker> getElements() {
        List<Marker> result = new ArrayList<>();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM records WHERE 1", new String[]{});
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            result.add(new Marker(new LatLng(cursor.getDouble(0), cursor.getDouble(1)),
                    cursor.getString(2)));
            cursor.moveToNext();
        }
        return result;
    }

    public void deleteAll() {
        List<Marker> markers = getElements();
        for (Marker marker : markers) {
            mDatabase.execSQL("DELETE FROM records WHERE ELLAT=\"" + marker.getPosition().latitude +
                    "\"" + "AND ELLONG=\"" + marker.getPosition().longitude + "\"" + ";");
        }
    }
}
