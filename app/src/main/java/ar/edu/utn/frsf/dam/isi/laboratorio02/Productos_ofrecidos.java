package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public class Productos_ofrecidos extends AppCompatActivity {
    private Spinner spinnerCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_ofrecidos);
        spinnerCat = (Spinner) findViewById(R.id.spinner_cat);



        //adapter lista de categorias
        ProductoRepository prodrepos = new ProductoRepository();
        //aparentemente el
        List<String> nombrescat = new ArrayList<>();
        List<Categoria> categorias = new ArrayList<>();
        categorias = prodrepos.getCategorias();
        //como get categorias me devuelve una lista de categorias les saco los nombres como string
        for(int i=0 ; i<prodrepos.getCategorias().size(); i++){
            nombrescat.add(categorias.get(i).getNombre());
        }
        //inicializo el adapter y se lo paso a el spinner
        ArrayAdapter<String> adaptador_categoria_prod = new ArrayAdapter<>(this , android.R.layout.simple_spinner_item, nombrescat);
        adaptador_categoria_prod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setAdapter(adaptador_categoria_prod);
    }
}
