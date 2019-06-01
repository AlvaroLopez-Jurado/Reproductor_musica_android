package com.example.alvaro.prac2;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListasAdapter extends BaseAdapter {

    public static LayoutInflater inflater = null;
    Context contexto;
    String[] nombre;
    int[] aa;
    Cursor cursor;
    GameDataHelper conn;
    SQLiteDatabase db;
    TextView _nombre;

    public ListasAdapter(Context contexto) {
        this.contexto = contexto;

        conn = new GameDataHelper(contexto);
        db = conn.getReadableDatabase();
        String[] campos = {UtilidadesSQL.CAMPO_NOMBRE};
        cursor = db.query(UtilidadesSQL.TABLA_LISTAS, campos, null, null, null, null, null);

        this.nombre = new String[cursor.getCount()];
        this.aa = new int[cursor.getCount()];
        int i=0;

        while(cursor.moveToNext()) {
            this.nombre[i] = cursor.getString(0);
            this.aa[i] = GameDataHelper.countTable(db, this.nombre[i]);
            i++;
        }
        cursor.close();

        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final View vista = inflater.inflate(R.layout.activity_listas_adapter, null);
        _nombre = vista.findViewById(R.id.textView4);
        TextView _numero = vista.findViewById(R.id.textView5);

        _nombre.setText(this.nombre[i]);
        _numero.setText(String.valueOf(this.aa[i]) + " canciones");
        _nombre.setTag(this.nombre[i]);

        return vista;
    }

    @Override
    public int getCount() {
        return this.nombre.length;
    }

    @Override
    public Object getItem(int i) {
        return _nombre;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
