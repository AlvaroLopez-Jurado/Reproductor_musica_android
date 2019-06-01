package com.example.alvaro.prac2;

import android.Manifest;
import android.app.usage.UsageEvents;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final int REQ_PERMISSION = 1543;
    FloatingActionButton m_float_button;
    FrameLayout progressBarParent;
    SharedPref shared;
    NavigationView navigation;
    Boolean trrra;
    static Boolean anadirCancion;
    static Boolean yaAnadida;
    Toast toast;
    static Boolean intentoAniadir;
    int deleteThis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        anadirCancion = false;
        yaAnadida = false;
        intentoAniadir = false;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_PERMISSION);
            finish();
            startActivity(getIntent());
            return;
        }

        /*Llamar a buscar todas las canciones del movil  ¯\_(*.*)_/¯ */
        getAllAudioFromDevice(getApplicationContext());

        setContentView(R.layout.activity_main);

        CategoryAdapter adapter = new CategoryAdapter(getSupportFragmentManager(), this);

        ViewPager pager = findViewById(R.id.viewpager);
        pager.setAdapter(adapter);

        m_float_button = findViewById(R.id.float_button);
        m_float_button.hide();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TabLayout tab = findViewById(R.id.tablayout);
        tab.setupWithViewPager(pager);
        if(trrra) {
            tab.getTabAt(0).setText(R.string._home_tab);
            tab.getTabAt(1).setText(R.string._second_tab);
        }

        DrawerLayout Drawerlayout = findViewById(R.id.draw);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, Drawerlayout, toolbar, 0, 0);

        Drawerlayout.addDrawerListener(toggle);
        toggle.syncState();

        navigation = findViewById(R.id.navigation_drawer_view);
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

        navigation.getMenu().getItem(0).setChecked(true);

        toast = Toast.makeText(getApplicationContext(), "Song added", Toast.LENGTH_LONG);
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
        super.onRestart();
        finish();
        startActivity(getIntent());
        if (anadirCancion) {
            if (trrra)
                toast.setText("Cancion añadida");
        } else if (yaAnadida) {
            if (trrra)
                toast.setText("No se pudo añadir");
            else
                toast.setText("Impossible to add");
        }
        toast.setGravity(Gravity.TOP, 0, 0);
        setAnadirCancion(false);
        setYaAnadida(false);
        if(intentoAniadir)
            toast.show();
        setIntentoAniadir(false);
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

    public void getAllAudioFromDevice(final Context context) {
        final List<String> tempAudioList = new ArrayList<>();
        GameDataHelper conn = new GameDataHelper(getApplicationContext());
        SQLiteDatabase db = conn.getWritableDatabase();
        int id = GameDataHelper.getMaxId(db) + 1;

        try {
            int delete = GameDataHelper.getDelete(db);
            if (delete == 1)
                delete = 0;
            else
                delete = 1;

            deleteThis = delete;
        }catch(Exception e){
            deleteThis = 0;
        }
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA,MediaStore.Audio.AudioColumns.TITLE , MediaStore.Audio.Media.DURATION};
        Cursor c = context.getContentResolver().query(uri,
                projection,
                null,
                null,
                null);

        if (c != null) {
            while (c.moveToNext()) {
                // Create a model object.

                String path = c.getString(0);   // Retrieve path.
                String name = c.getString(1);   // Retrieve name.
                String duration = c.getString(2); //Retrieve duration without convert
                String[] splited = path.split("/");
                String _path = path.substring(0, path.indexOf(splited[splited.length-1] )- 1 );

                boolean respuesta = GameDataHelper.searchForSong(db, name);
                if(!respuesta) {
                    GameDataHelper.addSong(db, id, name, path, duration, _path, deleteThis);
                    id++;
                }
            }
        }
        c.close();
        int delete;
        if(deleteThis == 0)
            delete  = 1;
        else
            delete =0;

        GameDataHelper.deleteNonFound(db, String.valueOf(delete));
        GameDataHelper.Borrar = false;
        db.close();
    }

    public static void setAnadirCancion(Boolean _anadirCancion) {
        anadirCancion = _anadirCancion;
    }

    public static void setYaAnadida(Boolean _yaAnadida) {
        yaAnadida = _yaAnadida;
    }

    public static void setIntentoAniadir(Boolean _intentoAniadir) {
        intentoAniadir = _intentoAniadir;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
