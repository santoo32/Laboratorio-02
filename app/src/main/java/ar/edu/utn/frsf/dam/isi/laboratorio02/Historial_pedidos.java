package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.CustomAdapters.AdaptadorFilaHistorial;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

import static ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository.LISTA_PEDIDOS;

public class Historial_pedidos extends AppCompatActivity {

    private Button btnnuevo;
    private Button btnmenu;
    private ListView viewpedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedidos);

        btnnuevo = (Button) findViewById(R.id.button_nuevo);
        btnmenu = (Button) findViewById(R.id.button_menu);
        viewpedido = (ListView) findViewById(R.id.ListView1);



        //----------Intents de los botones-------------

        btnnuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), Alta_pedidos.class );
                startActivity(i);
            }
        });
        btnmenu = (Button) findViewById(R.id.button_menu);
        btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //Punto 6
        AdaptadorFilaHistorial customadapter = new AdaptadorFilaHistorial(this, LISTA_PEDIDOS);



        //-------------Listener ListView------------------------
        /*viewpedido.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

                Toast.makeText(getApplicationContext(), "No implementado aun", Toast.LENGTH_LONG).show();

                Log.v("long clicked","pos: " + pos);

                return true;
            }
        });*/

    }
}
