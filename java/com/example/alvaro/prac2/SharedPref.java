package com.example.alvaro.prac2;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences shared;

    public SharedPref(Context context) {
        shared = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    public void setNightModeState(Integer state) {
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt("Color", state);
        editor.commit();
    }

    public int loadNightModeState() {
        int state = shared.getInt("Color", 0);
        return state;
    }

    public void setLenguage(Boolean idioma) {
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("Idioma", idioma);
        editor.commit();
    }

    public boolean getLenguage() {
        boolean state = shared.getBoolean("Idioma", false);
        return state;
    }
}
