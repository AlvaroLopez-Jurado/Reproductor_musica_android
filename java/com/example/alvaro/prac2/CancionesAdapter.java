package com.example.alvaro.prac2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CancionesAdapter extends BaseAdapter {

    public static LayoutInflater inflater = null;
    Context contexto;
    MediaPlayer mediaPlayer = null;
    Boolean addLista;
    String[] nombres;
    int[] duraciones;
    GameDataHelper conn;
    SQLiteDatabase db;

    public CancionesAdapter(Context contexto, String temp, String album) {
        this.contexto = contexto;
        String[] campos;
        conn = new GameDataHelper(contexto);
        db = conn.getWritableDatabase();
        Cursor cursor = null;

        campos = new String[]{UtilidadesSQL.CAMPO_NOMBRE, UtilidadesSQL.CAMPO_DURACION};

        if(album != null) {
            /*viene del grid y hay que elegir el album*/
            addLista = false;
            ArrayList<Integer> ids = GameDataHelper.getIdAlbum(db, album);
            String[] str = new String[ids.size()];
            for(int i=0; i< ids.size() ; i++)
                str[i] = String.valueOf(ids.get(i));
            nombres = new String[str.length];
            duraciones = new int[str.length];
            for(int i=0; i< str.length ; i++){
                cursor = db.query(UtilidadesSQL.TABLA_NOMBRE, campos, UtilidadesSQL.CAMPO_ID + "=?", new String[]{str[i]}, null, null, null);
                cursor.moveToNext();
                nombres[i] = cursor.getString(0);
                duraciones[i] = cursor.getInt(1);
            }
        }else {
            if (temp == UtilidadesSQL.TABLA_NOMBRE) {
                /*si es la tabla de las canciones es el home fragment por lo tanto sacamos todas las canciones*/

                addLista = true;
                cursor = db.query(temp, campos, null, null, null, null, null);

                nombres = new String[cursor.getCount()];
                duraciones = new int[cursor.getCount()];

                if (cursor != null) {
                    cursor.moveToFirst();

                    for (int i = 0; i < cursor.getCount(); i++) {
                        nombres[i] = cursor.getString(0);
                        duraciones[i] = cursor.getInt(1);
                        cursor.moveToNext();
                    }
                }
                cursor.close();
            } else {/*sino es una lista, por loq ue buscamos las cancionesd e la lista*/
                /*hacemos una consulta en la tabla para saber ls id de las canciones*/
                addLista = false;
                Cursor cursor1 = db.rawQuery("SELECT " + UtilidadesSQL.CAMPO_ID + " FROM " + temp, null);
                if(cursor1.getCount() > 0) {
                    cursor1.moveToFirst();
                    String[] strings = new String[cursor1.getCount()];
                    for (int i = 0; i < cursor1.getCount(); i++) {/*lo gusardamos en strings*/
                        strings[i] = String.valueOf(cursor1.getInt(0));
                        cursor1.moveToNext();
                    }
                    nombres = new String[cursor1.getCount()];
                    duraciones = new int[cursor1.getCount()];
                    /*consuta para saber las canciones*/
                    if (strings.length != 0) {
                        for (int i = 0; i < strings.length; i++) {
                            cursor = db.query(UtilidadesSQL.TABLA_NOMBRE, campos, UtilidadesSQL.CAMPO_ID + "=?", new String[]{strings[i]}, null, null, null);
                            if(cursor.getCount()>0) {
                                cursor.moveToNext();
                                nombres[i] = cursor.getString(0);
                                duraciones[i] = cursor.getInt(1);
                            }
                        }
                        cursor.close();
                    } else
                        cursor = null;
                }
            }
        }
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final View vista = inflater.inflate(R.layout.activity_canciones_adapter, null);
        final TextView nombre = vista.findViewById(R.id.textView);
        TextView duracion = vista.findViewById(R.id.textView2);
        ImageView play = vista.findViewById(R.id.imageView2);
        ImageView addList = vista.findViewById(R.id.imageView);

        nombre.setText(nombres[i]);
        duracion.setText(convertDuration(duraciones[i]));
        nombre.setTag(i);

        if(addLista)/*si es home fragment creamos el boton sino no se crea*/
            addList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(contexto, Listas.class);
                    intent.putExtra("BUTTON", false);
                    intent.putExtra("CANCION", nombre.getText().toString());
                    contexto.startActivity(intent);
                }
            });

        final int pos = i;

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), PlaySong.class);
                intent.putExtra("NOMBRE", nombres[pos]);
                intent.setType("audio/*");
                v.getContext().startActivity(intent);
            }
        });


        if(!addLista)
            addList.setBackgroundColor(Color.rgb(255, 255, 255));
        return vista;
    }

    @Override
    public int getCount() { return nombres.length; }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String convertDuration(long duration) {
        String out = null;
        long hours=0;
        try {
            hours = (duration / 3600000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return out;
        }
        long remaining_minutes = (duration - (hours * 3600000)) / 60000;
        String minutes = String.valueOf(remaining_minutes);
        if (minutes.equals(0)) {
            minutes = "00";
        }
        long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
        String seconds = String.valueOf(remaining_seconds);
        if (seconds.length() < 2) {
            seconds = "00";
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours + ":" + minutes + ":" + seconds;
        } else {
            out = minutes + ":" + seconds;
        }

        return out;

    }
}
