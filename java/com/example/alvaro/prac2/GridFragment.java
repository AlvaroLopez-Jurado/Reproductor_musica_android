package com.example.alvaro.prac2;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GridFragment extends Fragment {

    public GridFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.activity_grid, container, false);
        GridView grid = vista.findViewById(R.id.grid);
        grid.setAdapter(new GridAdapter(this.getContext()));

        return vista;
    }
}
