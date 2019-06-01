package com.example.alvaro.prac2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MenuLista extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SharedPref shared;
    Boolean trrra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shared = new SharedPref(this);
        switch(shared.loadNightModeState()){
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
        setContentView(R.layout.activity_menu_lista);

        ListView canciones_lista = findViewById(R.id.canciones_lista);

        Bundle b = getIntent().getExtras();
        String string = b.getString("LISTA");

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

        canciones_lista.setAdapter(new CancionesAdapter(this, string, null));

        navigation.getMenu().getItem(1).setChecked(true);
    }

    @Override
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
    public void onRestart()
    {
        /*si recargamos la actividad se reinicia por si cambia el color de la app*/
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        /*menu lateral*/
        int id = menuItem.getItemId();
        Intent intent;
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
