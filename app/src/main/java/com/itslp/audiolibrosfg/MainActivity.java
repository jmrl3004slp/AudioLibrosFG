package com.itslp.audiolibrosfg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.itslp.audiolibrosfg.fragmentos.DetalleFragment;

public class MainActivity extends AppCompatActivity {
    private AdaptadorLibrosFiltro adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        adaptador = ((Aplicacion) getApplicationContext()).getAdaptador();
    }

    public void mostrarDetalle(int id) {
        DetalleFragment detalleFragment = (DetalleFragment) getSupportFragmentManager().findFragmentById(R.id.detalle_fragment);
        if (detalleFragment != null) {
            detalleFragment.ponInfoLibro(id);
        }

        SharedPreferences pref = getSharedPreferences("itslp.moviles.sistemas.audiolibrosfg_internal", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("ultimo", id);
        editor.commit();
    }//mostrarDetalle

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_buscar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String query) {
                        adaptador.setBusqueda(query);
                        adaptador.notifyDataSetChanged();
                        return false;
                    }
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                }//.OnQueryTextListener()
        );//setOnQueryTextListener

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override public boolean onMenuItemActionCollapse(MenuItem item) {
                adaptador.setBusqueda("");
                adaptador.notifyDataSetChanged();
                return true; // Para permitir cierre
            }
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true; // Para permitir expansión
            }

        });//setOnActionExpandListener

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_preferencias) {
            Toast.makeText(this, "Preferencias", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.menu_ultimo) {
            irUltimoVisitado();
            return true;
        } else if (id == R.id.menu_buscar) {
            return true;
        } else if (id == R.id.menu_acerca) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Mensaje de Acerca De");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.create().show();
            return true;
        }//else


        return super.onOptionsItemSelected(item);
    }

    public void irUltimoVisitado() {
        SharedPreferences pref = getSharedPreferences("itslp.moviles.sistemas.audiolibrosfg_internal", MODE_PRIVATE);
        int id = pref.getInt("ultimo", -1);
        if (id >= 0) {
            mostrarDetalle(id);
        } else {
            Toast.makeText(this,"Sin última vista",Toast.LENGTH_LONG).show();
        }//else
    }//irUltimoVisitado
}
