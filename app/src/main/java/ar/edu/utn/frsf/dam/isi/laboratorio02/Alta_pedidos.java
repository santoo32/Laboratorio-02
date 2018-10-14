package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

import static ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido.Estado.EN_PREPARACION;
import static ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido.Estado.REALIZADO;

public class Alta_pedidos extends AppCompatActivity{

    private Pedido unPedido;
    private PedidoRepository repositorioPedido;
    private List<PedidoDetalle> detalle;
    private ProductoRepository repositorioProducto;
    private RadioButton retiro_local;
    private RadioButton envio_domicilio;
    private EditText domicilio;
    private Button agregar_producto;
    private ListView lista_detalle;
    private TextView total;
    private Button hacerPedido;
    private Button volver;
    private  ArrayAdapter<PedidoDetalle> adapter;
    private Button eliminar_prod;
    private Integer ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_pedidos);


        retiro_local = (RadioButton) findViewById(R.id.radioButton);
        envio_domicilio = (RadioButton) findViewById(R.id.radioButton2);
        domicilio = (EditText)findViewById(R.id.editText3);
        envio_domicilio.setChecked(true);
        agregar_producto = (Button) findViewById(R.id.button5);
        lista_detalle = (ListView)  findViewById(R.id.lista_detalle);
        detalle = new ArrayList<>();
        total = (TextView) findViewById(R.id.textView12);
        hacerPedido = (Button) findViewById(R.id.button7);
        volver = (Button) findViewById(R.id.button8);
        repositorioProducto = new ProductoRepository();
        repositorioPedido = new PedidoRepository();
        eliminar_prod = (Button) findViewById(R.id.button6);

        Calendar c = Calendar.getInstance();
        //Inicializar variables
        unPedido = new Pedido(c.getTime(), EN_PREPARACION);
        lista_detalle.setEnabled(true);

        //PUNTO E
        /*if(detalle==null){
            lista_detalle.setEnabled(false);
        }else {
            lista_detalle.setEnabled(true);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, detalle);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lista_detalle.setAdapter(adapter);
        }*/



        envio_domicilio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (envio_domicilio.isChecked()){
                    domicilio.setEnabled(true);
                }
                else{
                    domicilio.setEnabled(false);
                }
            }
        });

        agregar_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Productos_ofrecidos.class);
                intent.putExtra("NUEVO_PEDIDO", 1);
                startActivityForResult(intent, 777);
            }
        });



        lista_detalle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PedidoDetalle eliminar = (PedidoDetalle) adapterView.getItemAtPosition(i);
                ID = eliminar.getId();
                adapter.remove((PedidoDetalle) adapterView.getItemAtPosition(i));
                adapter.notifyDataSetChanged();
                Double costo = calcularCosto(detalle);
                total.setText(" Total Pedido: " + costo.toString());
            }
        });

        eliminar_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Alta_pedidos.this, "Toque el item en la lista que desea eliminar",
                        Toast.LENGTH_LONG).show();
            }
        });
        hacerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //punto i
                validarDatos();//falta implementar
                //punto i.ii
                unPedido.setEstado(REALIZADO);
                //punto i.iii
                unPedido.setDetalle(detalle);
                asignarDatos();//fata implementar
                //punto i.iv
                repositorioPedido.guardarPedido(unPedido);


                //Etapa 3 parte 1
                /*Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.currentThread().sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // buscar pedidos no aceptados y aceptarlos automáticamente
                        List<Pedido> lista = repositorioPedido.getLista();
                        for (Pedido p : lista) {
                            if (p.getEstado().equals(Pedido.Estado.REALIZADO))
                                p.setEstado(Pedido.Estado.ACEPTADO);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Alta_pedidos.this, "Informacion de pedidos actualizada!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                };
                Thread unHilo = new Thread();
                unHilo.start();
                */

                finish();
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void validarDatos() {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       //punto h

        super.onActivityResult(requestCode, resultCode, data);
                Bundle extras = data.getExtras();
                Integer ID = (Integer) extras.get("idProducto");
                Integer cantidad = (Integer) extras.get("cantidad");

                PedidoDetalle d = new PedidoDetalle(cantidad, repositorioProducto.buscarPorId(ID));
                detalle.add(d);

                //item iv)
                Double costo = calcularCosto(detalle);
                total.setText(" Total Pedido: " + costo.toString());
                if(detalle==null){
                    lista_detalle.setEnabled(false);
                 }else {
                    lista_detalle.setEnabled(true);
                    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, detalle);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lista_detalle.setAdapter(adapter);
                    lista_detalle.setItemChecked(0, true );
                }
                //item v)
                adapter.setNotifyOnChange(true);




    }

    private void asignarDatos (){
        //implementar
    }

    private Double calcularCosto(List<PedidoDetalle> detalle) {

        Double costo = 0.0;

        for(int i = 0; i < detalle.size(); i++){

            costo += detalle.get(i).getProducto().getPrecio() * detalle.get(i).getCantidad();

        }


        return costo;
    }
}
