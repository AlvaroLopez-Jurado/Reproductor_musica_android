package com.example.alvaro.prac2;

public class UtilidadesSQL {

    public static final String TABLA_NOMBRE = "SONGS";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_ALBUM = "album";
    public static final String CAMPO_DURACION = "duracion";
    public static final String CAMPO_PATH = "path";
    public static final String CAMPO_DELETE = "del";



    public static final String TABLA_LISTAS = "LISTAS";

    public static final String NOMBRE_lISTA_DEFECTO = "Favorites";

    public static String CrearTabla(String nombreTabla){
        if(nombreTabla != null)
            return "CREATE TABLE "
                    + nombreTabla + " ("
                    + CAMPO_ID + " INTEGER UNIQUE, "
                    + CAMPO_NOMBRE + " TEXT, "
                    + CAMPO_ALBUM + " TEXT, "
                    + CAMPO_DURACION + " TEXT, "
                    + CAMPO_PATH + " TEXT, "
                    + CAMPO_DELETE + " INTEGER )";
        return null;
    }

    public static String CrearTablaListas(){
        return "CREATE TABLE "
                + TABLA_LISTAS + " ("
                + CAMPO_NOMBRE + " TEXT)";
    }

    public static String crearTablaFav(){
        return "CREATE TABLE "
                + NOMBRE_lISTA_DEFECTO + " ("
                + CAMPO_ID + " INTEGER UNIQUE, "
                + " FOREIGN KEY(" + CAMPO_ID + ") REFERENCES "+ TABLA_NOMBRE + "(" + CAMPO_ID + ") ON DELETE CASCADE)";
    }

    public static String crearNuevaTabla(String nombreTabla){
        return "CREATE TABLE "
                + nombreTabla + " ("
                + CAMPO_ID + " INTEGER UNIQUE, "
                + " FOREIGN KEY(" + CAMPO_ID + ") REFERENCES "+ TABLA_NOMBRE + "(" + CAMPO_ID + ") ON DELETE CASCADE)";
    }

}
