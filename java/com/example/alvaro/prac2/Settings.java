package com.example.alvaro.prac2;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button cambiarColor;
    TextView lenguage;
    TextView lenguage2;
    SharedPref shared;
    LinearLayout linear;
    Boolean trrra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shared = new SharedPref(this);
        switch (shared.loadNightModeState()) {
            case 0:
                setTheme(R.style.AppTheme);
                break;
            case 1:
                setTheme(R.style.AppTheme2);
                break;
            case 2:
                setTheme(R.style.AppTheme3);
                break;
            case 3:
                setTheme(R.style.AppTheme4);
                break;
            case 4:
                setTheme(R.style.AppTheme5);
                break;
            case 5:
                setTheme(R.style.AppTheme6);
                break;
        }
        trrra = shared.getLenguage();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout Drawerlayout = findViewById(R.id.draw);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, Drawerlayout, toolbar, 0, 0);

        Drawerlayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigation = findViewById(R.id.navigation_drawer_view);
        Menu menu = navigation.getMenu();
        MenuItem uno = menu.findItem(R.id.canciones);
        MenuItem dos = menu.findItem(R.id.listas);
        MenuItem tres = menu.findItem(R.id.action_settings);
        if(trrra) {
            uno.setTitle(R.string._canciones);
            dos.setTitle(R.string._listas);
            tres.setTitle(R.string._action_settings);
        }
        navigation.setNavigationItemSelectedListener(this);

        navigation.getMenu().getItem(2).setChecked(true);

        cambiarColor = findViewById(R.id.button2);
        if(trrra)
            cambiarColor.setText(R.string._dark_mode);
        cambiarColor.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                switch (shared.loadNightModeState()) {
                    case 0:
                        shared.setNightModeState(1);
                        break;
                    case 1:
                        shared.setNightModeState(2);
                        break;
                    case 2:
                        shared.setNightModeState(3);
                        break;
                    case 3:
                        shared.setNightModeState(4);
                        break;
                    case 4:
                        shared.setNightModeState(5);
                        break;
                    case 5:
                        shared.setNightModeState(0);
                        break;
                }
                restartApp();
            }
        });
        lenguage=findViewById(R.id.textViewLenguage);
        if(trrra)
            lenguage.setText(R.string._lenguage);
        lenguage2=findViewById(R.id.textViewLenguage2);
        if(trrra)
            lenguage2.setText(R.string._english);
        linear = findViewById(R.id.linear);

        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shared.getLenguage()){
                    shared.setLenguage(false);
                }
                else{
                    shared.setLenguage(true);
                }
                restartApp();
            }
        });

    }

    public void restartApp(){
        Intent i = new Intent( getApplicationContext(), Settings.class);
        startActivity(i);
        finish();
    }

    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.draw);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent = null;
        if(menuItem.isChecked()){}
        else {
            switch (id) {
                case R.id.canciones:
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.listas:
                    intent = new Intent(this, Listas.class);
                    intent.putExtra("BUTTON", true);
                    startActivity(intent);
                    break;
                case R.id.action_settings:
                    intent = new Intent(this, Settings.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
        DrawerLayout Drawerlayout = findViewById(R.id.draw);
        Drawerlayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
