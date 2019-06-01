package com.example.alvaro.prac2;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    public static LayoutInflater inflater = null;
    Context context;
    GameDataHelper conn;
    SQLiteDatabase db;
    ArrayList<String> lista;

    public GridAdapter(Context context) {
        this.context = context;
        conn = new GameDataHelper(context);
        db = conn.getReadableDatabase();
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View vista = inflater.inflate(R.layout.activity_grid_element, null);

        ImageView imagenCoche = vista.findViewById(R.id.imagen_carpeta);
        TextView nombreCoche = vista.findViewById(R.id.nombre_carpeta);

        String[] array = lista.get(position).split("/");

        imagenCoche.setImageResource(R.drawable.baseline_folder_black_48);
        nombreCoche.setText(array[array.length - 1]);

        vista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MenuGrid.class);
                intent.putExtra("ALBUM", lista.get(position));
                v.getContext().startActivity(intent);
            }
        });

        return vista;
    }


    @Override
    public int getCount() {
        lista = GameDataHelper.countUniqueTable(db);
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
