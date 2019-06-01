package com.example.alvaro.prac2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HomeFragment extends Fragment {

    public HomeFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*crea el adaptador para el paginador*/
        View vista =  inflater.inflate(R.layout.activity_home_fragment, container, false);
        ListView lista = vista.findViewById(R.id.lista_canciones);
        lista.setAdapter(new CancionesAdapter(this.getContext(), UtilidadesSQL.TABLA_NOMBRE, null));
        return vista;
    }
}

