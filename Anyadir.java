package com.example.david.trabajofinal;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Anyadir extends AppCompatActivity {
    int USUARIO;

    DatabaseHelper dbHelper;

    ArrayList<String> peliculas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_peliculas);

        dbHelper = new DatabaseHelper(this);
        dbHelper.open();

        peliculas = new ArrayList();

        final Cursor cursor2 = dbHelper.getItems(
                Globals.TABLE_PELICULA,
                new String[] {
                        Globals.TABLE_PELICULA_ID,
                        Globals.TABLE_PELICULA_NOMBRE},
                null,
                null,
                Globals.TABLE_PELICULA_ID);

        while (cursor2.moveToNext())
            peliculas.add(cursor2.getString(1));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, peliculas);

        USUARIO = getIntent().getIntExtra("USUARIO", 0);

        ListView vistaPelicula = (ListView) findViewById(R.id.vistaPelicula);
        vistaPelicula.setAdapter(adapter);

        vistaPelicula.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                dbHelper.insertItem(
                        String.format("INSERT INTO %s (%s, %s) VALUES (%s, %s)",
                                Globals.TABLE_PELICULA_USUARIO_REL,
                                Globals.TABLE_PELICULA_USUARIO_REL_USUARIO_ID,
                                Globals.TABLE_PELICULA_USUARIO_REL_PELICULA_ID,
                                USUARIO,
                                position + 1));
                Toast.makeText(getApplicationContext(), "Película añadida", Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void onStop () {
        super.onStop();
        dbHelper.close();
    }

    protected void onPause () {
        super.onPause();
        dbHelper.close();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);

        super.onBackPressed();
    }
}