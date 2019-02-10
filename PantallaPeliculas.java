package com.example.david.trabajofinal;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PantallaPeliculas extends AppCompatActivity {

    int USUARIO;

    DatabaseHelper dbHelper;

    ArrayList<String> usuarioPeliculas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_peliculas);

        dbHelper = new DatabaseHelper(this);
        dbHelper.open();

        usuarioPeliculas = new ArrayList();

        USUARIO = getIntent().getIntExtra("USUARIO", 0);

        Cursor cursor = dbHelper.getItems(
                Globals.TABLE_PELICULA_USUARIO_REL + ", " + Globals.TABLE_PELICULA,
                new String[] {Globals.TABLE_PELICULA + "." + Globals.TABLE_PELICULA_NOMBRE},
                Globals.TABLE_PELICULA_USUARIO_REL_PELICULA_ID + " = " +
                        Globals.TABLE_PELICULA + "." +
                        Globals.TABLE_PELICULA_ID + " AND usuario_id = ?",
                new String[] {String.valueOf(USUARIO)},
                Globals.TABLE_PELICULA_USUARIO_REL + "." +
                        Globals.TABLE_PELICULA_USUARIO_REL_ID);

        if (cursor.getCount() != 0)
            while (cursor.moveToNext())
                usuarioPeliculas.add(cursor.getString(0));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usuarioPeliculas);

        ListView vistaPelicula = (ListView) findViewById(R.id.vistaPelicula);
        vistaPelicula.setAdapter(adapter);

        dbHelper.close();
    }

    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ejemplo_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.anyadir:
                Intent intent = new Intent(this, Anyadir.class);

                Bundle bundle = new Bundle();
                bundle.putInt("USUARIO", USUARIO);

                intent.putExtras(bundle);
                startActivityForResult(intent, 0);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK){
            finish();
            startActivity(getIntent());
        }
    }

}