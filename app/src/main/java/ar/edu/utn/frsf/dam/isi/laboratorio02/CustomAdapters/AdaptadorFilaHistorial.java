package ar.edu.utn.frsf.dam.isi.laboratorio02.CustomAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ar.edu.utn.frsf.dam.isi.laboratorio02.R;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class AdaptadorFilaHistorial extends BaseAdapter implements ListAdapter {
    private Context context;

    //to store the products
    private final Producto[] imageIDarray;



    // ESTO ES PARA PROBAR ALGO, NO DARLE BOLA
    // ESTO ES PARA PROBAR ALGO, NO DARLE BOLA
    // ESTO ES PARA PROBAR ALGO, NO DARLE BOLA
    // ESTO ES PARA PROBAR ALGO, NO DARLE BOLA
    // ESTO ES PARA PROBAR ALGO, NO DARLE BOLA
    // ESTO ES PARA PROBAR ALGO, NO DARLE BOLA
    // ESTO ES PARA PROBAR ALGO, NO DARLE BOLA
    // ESTO ES PARA PROBAR ALGO, NO DARLE BOLA
    // ESTO ES PARA PROBAR ALGO, NO DARLE BOLA
    // ESTO ES PARA PROBAR ALGO, NO DARLE BOLA



    public AdaptadorFilaHistorial(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
        imageIDarray = new Producto[0];

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_adaptadorfilahistorial, null);
        }



        //Handle buttons and add onClickListeners
        Button menuBtn = view.findViewById(R.id.menu_btn);
        Button deleteBtn = view.findViewById(R.id.delete_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //Chequear en que estado esta el pedido para ver si se puede cancelar


                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //mostrar detalles pedido

                notifyDataSetChanged();
            }
        });
        return view;
    }
}
