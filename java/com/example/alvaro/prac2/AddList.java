package com.example.alvaro.prac2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddList extends AppCompatActivity {

    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.3));

        edit = findViewById(R.id.editText);
        edit.setSelected(true);
        Button boton = findViewById(R.id.button);
        final Intent resultIntent = new Intent();
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit.getText().length() != 0) {
                    resultIntent.putExtra("N_LISTA", edit.getText().toString());
                    setResult(RESULT_OK, resultIntent);
                }else{
                    CharSequence text = "Cambios descartados";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    setResult(Activity.RESULT_CANCELED, resultIntent);
                }
                finish();
            }
        });
    }
}
