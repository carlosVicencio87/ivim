package com.ivim.ivim;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Conexion extends SQLiteOpenHelper {

    public Conexion(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Sentencias.TABLA_CATALOGO);
        db.execSQL(Sentencias.TABLA_BASCULAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Sentencias.DROP_CATALOGO);
        db.execSQL(Sentencias.DROP_BASCULAS);
        onCreate(db);
    }
}
