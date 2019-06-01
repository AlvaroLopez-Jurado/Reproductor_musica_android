package com.example.alvaro.prac2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class GameDataHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "gamesdatabase";
    private static final int DBVERSION = 1;
    public static boolean Borrar = true;

    public GameDataHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Borrar = true;
        String a = UtilidadesSQL.CrearTabla(UtilidadesSQL.TABLA_NOMBRE);
        db.execSQL(a);
        a = UtilidadesSQL.CrearTablaListas();
        db.execSQL(a);
        a = UtilidadesSQL.crearTablaFav();
        db.execSQL(a);

        addList(db, UtilidadesSQL.NOMBRE_lISTA_DEFECTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesSQL.TABLA_NOMBRE);
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesSQL.TABLA_LISTAS);
        db.execSQL("DROP TABLE IF EXISTS " + UtilidadesSQL.NOMBRE_lISTA_DEFECTO);

        onCreate(db);
    }

    public static void addSong (SQLiteDatabase db, int id,  String name, String album, String durac, String path, int delete) {
        ContentValues data = new ContentValues();
        data.put(UtilidadesSQL.CAMPO_ID, id);
        data.put(UtilidadesSQL.CAMPO_NOMBRE, name);
        data.put(UtilidadesSQL.CAMPO_ALBUM, album);
        data.put(UtilidadesSQL.CAMPO_DURACION, durac);
        data.put(UtilidadesSQL.CAMPO_PATH, path);
        data.put(UtilidadesSQL.CAMPO_DELETE, delete);
        db.insert(UtilidadesSQL.TABLA_NOMBRE, null, data);
    }

    public static void addList(SQLiteDatabase db, String name){
        ContentValues data = new ContentValues();
        data.put(UtilidadesSQL.CAMPO_NOMBRE, name);
        db.insert(UtilidadesSQL.TABLA_LISTAS, null, data);
    }

    public static int countTable(SQLiteDatabase db, String name){
        return (int) DatabaseUtils.queryNumEntries(db, name);
    }

    public static ArrayList<String> countUniqueTable(SQLiteDatabase db){
        Cursor query = db.rawQuery("SELECT " + UtilidadesSQL.CAMPO_PATH + " FROM " + UtilidadesSQL.TABLA_NOMBRE + " GROUP BY " + UtilidadesSQL.CAMPO_PATH,null);
        ArrayList<String> a = new ArrayList<>();
        while(query.moveToNext()){
            a.add(query.getString(0));
        }
        return a;
    }

    public static ArrayList<String> countUnTable(SQLiteDatabase db, String nombreTable){
        Cursor query = db.rawQuery("SELECT DISTINCT ? FROM " + nombreTable, new String[]{UtilidadesSQL.CAMPO_ALBUM});
        ArrayList<String> a = new ArrayList<>();
        while(query.moveToNext()){
            a.add(query.getString(0));
        }
        return a;
    }

    public static int getMaxId (SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT MAX(" + UtilidadesSQL.CAMPO_ID + ") FROM " + UtilidadesSQL.TABLA_NOMBRE, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static boolean searchForSong(SQLiteDatabase db, String name){
        Cursor c = db.query(UtilidadesSQL.TABLA_NOMBRE, new String[]{UtilidadesSQL.CAMPO_NOMBRE}, UtilidadesSQL.CAMPO_NOMBRE+"=?", new String[]{name}, null, null, null);
        if(c.getCount() > 0)
            return true;
        return false;
    }

    public static ArrayList<Integer> getIdAlbum(SQLiteDatabase db, String album){
        Cursor cursor = db.query(UtilidadesSQL.TABLA_NOMBRE, new String[]{UtilidadesSQL.CAMPO_ID}, UtilidadesSQL.CAMPO_PATH +" =?", new String[]{album}, null,null,null);
        ArrayList<Integer> a = new ArrayList<>();
        while(cursor.moveToNext()){
            a.add(cursor.getInt(0));
        }
        return a;
    }

    public static int getDelete(SQLiteDatabase db){
        Cursor cursor = db.query(UtilidadesSQL.TABLA_NOMBRE, new String[]{UtilidadesSQL.CAMPO_DELETE}, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static void deleteNonFound(SQLiteDatabase db, String notFound){
        if(Borrar) {
            db.execSQL("PRAGMA foreign_keys = ON;");
            Cursor cursor = db.query(UtilidadesSQL.TABLA_NOMBRE, new String[]{UtilidadesSQL.CAMPO_ID}, UtilidadesSQL.CAMPO_DELETE + "=?", new String[]{notFound}, null, null, null);
            while (cursor.moveToNext()) {
                db.delete(UtilidadesSQL.TABLA_NOMBRE, UtilidadesSQL.CAMPO_ID + "=?", new String[]{String.valueOf(cursor.getInt(0))});
            }
        }
    }
}
