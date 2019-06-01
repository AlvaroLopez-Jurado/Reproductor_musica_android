package com.example.alvaro.prac2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Listas extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    int[] canciones = new int[]{0};
    ListView lista;
    Boolean opcion;
    SharedPref shared;
    GameDataHelper conn;
    SQLiteDatabase db;
    Boolean trrra;

    public String[] nuevasListas(){
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        shared = new SharedPref(this);
        switch(shared.loadNightModeState()){
            /*para crear el color de la app*/
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
        setContentView(R.layout.activity_listas);

        conn = new GameDataHelper(this);
        db = conn.getReadableDatabase();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
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

        Bundle b = getIntent().getExtras();
        FloatingActionButton button = findViewById(R.id.fab);
        opcion = b.getBoolean("BUTTON");
        final String can = b.getString("CANCION");
        if(opcion) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), AddList.class);
                    int result = 1;
                    startActivityForResult(intent, result);
                }
            });
        }
        else{
            button.hide();
        }

        this.lista = findViewById(R.id.listas_li);
        this.lista.setAdapter(new ListasAdapter(this));

        this.lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                TextView v = (TextView) parent.getItemAtPosition(i);
                Cursor c = db.query(UtilidadesSQL.TABLA_LISTAS, new String[]{UtilidadesSQL.CAMPO_NOMBRE}, UtilidadesSQL.CAMPO_NOMBRE + "=?", new String[]{v.getText().toString()}, null, null, null);
                c.moveToFirst();
                if(!opcion){/*si opcion es falso entonces añadimos la cancion a la lista en la base de datos */
                    Cursor d = db.query(UtilidadesSQL.TABLA_NOMBRE, new String[]{UtilidadesSQL.CAMPO_ID}, UtilidadesSQL.CAMPO_NOMBRE + "=?", new String[]{can}, null, null, null);
                    d.moveToFirst();
                    ContentValues val = new ContentValues();
                    val.put(UtilidadesSQL.CAMPO_ID, d.getInt(0));
                    long insert = db.insert(c.getString(0), null, val);
                    if(insert == -1)
                        MainActivity.setYaAnadida(true);
                    else
                        MainActivity.setAnadirCancion(true);
                    d.close();
                    MainActivity.setIntentoAniadir(true);
                    finish();
                }
                else{/*sino es que seleccionamos la lista para que nos muestre las canciones*/
                    if(GameDataHelper.countTable(db, v.getText().toString()) != 0) {
                        Intent intent = new Intent(view.getContext(), MenuLista.class);
                        intent.putExtra("LISTA", c.getString(0));
                        startActivity(intent);
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Lista vacia", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }
                }
                c.close();
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*esta funcion solo se ejecuta cuando queremos añadir una lista nueva
        * creamos la lista en la base de datos
        * y una nueva tabla para dicha lista*/
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                ContentValues values = new ContentValues();
                GameDataHelper.addList(db, data.getStringExtra("N_LISTA"));
                db.execSQL(UtilidadesSQL.crearNuevaTabla(data.getStringExtra("N_LISTA")));
                ListasAdapter listass = new ListasAdapter(this);
                this.lista.setAdapter(listass);
            }
        }
    }
}
