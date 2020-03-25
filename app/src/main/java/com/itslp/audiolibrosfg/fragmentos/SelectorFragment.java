package com.itslp.audiolibrosfg.fragmentos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.itslp.audiolibrosfg.AdaptadorLibrosFiltro;
import com.itslp.audiolibrosfg.Aplicacion;
import com.itslp.audiolibrosfg.Libro;
import com.itslp.audiolibrosfg.MainActivity;
import com.itslp.audiolibrosfg.R;

import java.util.Vector;

public class SelectorFragment extends Fragment {
    private Activity actividad;
    private RecyclerView recyclerView;
    private AdaptadorLibrosFiltro adaptador;
    private Vector<Libro> vectorLibros;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.actividad = (Activity) context;
            Aplicacion app = (Aplicacion) actividad.getApplication();
            adaptador = app.getAdaptador();
            vectorLibros = app.getVectorLibros();
        }//if
    }// onAttach

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_selector, container, false);
        recyclerView = (RecyclerView) vista.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(actividad, 2));
        recyclerView.setAdapter(adaptador);
        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(actividad, "Seleccionado el elemento: " +
                        recyclerView.getChildAdapterPosition(v), Toast.LENGTH_SHORT).show();
                ((MainActivity) actividad).mostrarDetalle((int) adaptador.getItemId( recyclerView.getChildAdapterPosition(v)));
            }//onClick
        });//setOnItemClickListener

        // SE AGREGA UN ESCUCHADOR PARA EL CLICK LARGO Y MOSTRAR LAS OPCIONES PARA COMPARTIR BORRAR E INSERTAR
        adaptador.setOnItemLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(final View v) {
                final int id = recyclerView.getChildAdapterPosition(v);
                AlertDialog.Builder menu = new AlertDialog.Builder(actividad);
                CharSequence[] opciones = {"Compartir", "Borrar ", "Insertar"};
                menu.setItems(opciones, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int opcion) {
                        switch (opcion) {
                            case 0: //Compartir
                                Libro libro = vectorLibros.elementAt(id);
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("text/plain");
                                i.putExtra(Intent.EXTRA_SUBJECT, libro.titulo);
                                i.putExtra(Intent.EXTRA_TEXT, libro.urlAudio);
                                startActivity(Intent.createChooser(i, "Compartir"));
                                break;
                            case 1: //Borrar
                                Snackbar.make(v, "¿Estás seguro?", Snackbar.LENGTH_LONG).
                                        setAction("SI", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                //vectorLibros.remove(id);
                                                adaptador.borrar(id);
                                                adaptador.notifyDataSetChanged();
                                            }
                                        }).show(); //setAction
                                break;
                            case 2: //Insertar
                                //vectorLibros.add(vectorLibros.elementAt(id));
                                int posicion = recyclerView.getChildLayoutPosition(v);
                                adaptador.insertar((Libro) adaptador.getItem(posicion));
                                adaptador.notifyDataSetChanged();
                                Snackbar.make(v,"Libro insertado", Snackbar.LENGTH_INDEFINITE).setAction
                                        ("OK", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                    } //onClick
                                                }//View.OnClickListener()
                                        ) .show();
                                break;
                        }//switch
                    }//onClick
                });//menu.setItems
                menu.create().show();
                return true;
            }//onLongClick
        });//adaptador.setOnItemLongClickListener
        //==========================================================================================

        return vista;
    }//onCreateView
}//Clase