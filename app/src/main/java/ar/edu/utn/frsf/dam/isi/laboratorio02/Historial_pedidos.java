package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.CustomAdapters.AdaptadorFilaHistorial;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

import static ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository.LISTA_PEDIDOS;

public class Historial_pedidos extends AppCompatActivity {

    private Button btnnuevo;
    private Button btnmenu;
    private ListView viewpedido;
    //private PedidoRepository repositorioPedido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedidos);


        btnnuevo = (Button) findViewById(R.id.button_nuevo);
        btnmenu = (Button) findViewById(R.id.button_menu);
        viewpedido = (ListView) findViewById(R.id.ListView1);
        //repositorioPedido = new PedidoRepository();
        viewpedido.setEnabled(true);




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

        //---------Adapter lista de pedidos-------------------------------------------------------------------

        /*if(repositorioPedido.getLista()==null){
            viewpedido.setEnabled(false);
        }else{
            viewpedido.setEnabled(true);
            AdaptadorFilaHistorial customadapter = new AdaptadorFilaHistorial(this, repositorioPedido.getLista());
            //ArrayAdapter<Pedido> adaptador_pedidos_prod = new ArrayAdapter<>(this , android.R.layout.simple_spinner_item, repositorioPedido.getLista());
            //adaptador_pedidos_prod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            customadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            viewpedido.setAdapter(customadapter);

        }*/
        Runnable r = new Runnable() {
            @Override
            public void run() {
                MyDatabase.getInstance(getApplicationContext());
                final List<Pedido> pedidos = MyDatabase.getAllPedidos();

                if(pedidos == null || pedidos.size()==0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewpedido.setEnabled(false);
                            Toast.makeText(Historial_pedidos.this, "No se han realizado pedidos", Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewpedido.setEnabled(true);
                            AdaptadorFilaHistorial customadapter = new AdaptadorFilaHistorial(getApplicationContext(), pedidos);
                            customadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            viewpedido.setAdapter(customadapter);
                        }
                    });
                }
            }
        };
        Thread hiloHistorial = new Thread(r);
        hiloHistorial.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, ConfiguracionActivity.class);
            startService(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
