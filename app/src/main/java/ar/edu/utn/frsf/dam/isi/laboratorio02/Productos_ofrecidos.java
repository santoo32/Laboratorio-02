package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class Productos_ofrecidos extends AppCompatActivity {
    private Spinner spinnerCat;
    private ListView listprod;
    private Button agregar;
    private EditText cantidad_pedir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_ofrecidos);
        spinnerCat = (Spinner) findViewById(R.id.spinner_cat);
        agregar = (Button) findViewById(R.id.btnProdAddPedido);
        spinnerCat.setSelection(0);
        listprod = (ListView) findViewById(R.id.listprod);
        cantidad_pedir = (EditText) findViewById(R.id.cantidad_pedir);
        final ProductoRepository prodrepos = new ProductoRepository();

        agregar.setEnabled(false);
        cantidad_pedir.setEnabled(false);

        //--------Analizando si se reciben datos en el Intent---------------------
        Intent intent = getIntent();
        if(intent.getStringExtra("NUEVO_PEDIDO") == "1"){
            agregar.setEnabled(true);
            cantidad_pedir.setEnabled(true);
        }


        //---------Adapter lista de categorias-------------------------------------------------------------------
        List<Categoria> categorias = new ArrayList<>();
        categorias = prodrepos.getCategorias();
        ArrayAdapter<Categoria> adaptador_categoria_prod = new ArrayAdapter<>(this , android.R.layout.simple_spinner_item, categorias);
        adaptador_categoria_prod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setAdapter(adaptador_categoria_prod);
        //--------Adapter producto por categoria---------------------------------------------------
        spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Categoria cat = prodrepos.getCategorias().get(position);
                ArrayAdapter<Producto> adaptador_prod = new ArrayAdapter<Producto>(getApplicationContext(), android.R.layout.simple_spinner_item, prodrepos.buscarPorCategoria(cat));
                adaptador_prod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                listprod.setAdapter(adaptador_prod);
                listprod.setItemChecked(0, true );

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("cantidad", cantidad_pedir.getText());

                //i.putExtra("idProducto", listprod.getSelectedItem().getClass());
            }
        });

    }
}
