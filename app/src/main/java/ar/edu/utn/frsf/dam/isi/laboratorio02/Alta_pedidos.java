package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

import static ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido.Estado.EN_PREPARACION;

public class Alta_pedidos extends AppCompatActivity{

    private Pedido unPedido;
    private PedidoRepository repositorioPedido;
    private List<PedidoDetalle> detalle;
    private ProductoRepository repositorioProducto;
    private RadioButton retiro_local;
    private RadioButton envio_domicilio;
    private EditText domicilio;
    private Button agregar_pedido;
    private ListView lista_detalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_pedidos);

        retiro_local = (RadioButton) findViewById(R.id.radioButton);
        envio_domicilio = (RadioButton) findViewById(R.id.radioButton2);
        domicilio = (EditText)findViewById(R.id.editText3);
        envio_domicilio.setChecked(true);
        agregar_pedido = (Button) findViewById(R.id.button5);
        lista_detalle = (ListView)  findViewById(R.id.lista_detalle);
        detalle = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        //Inicializar variables
        unPedido = new Pedido(c.getTime(), EN_PREPARACION);


        //PUNTO E
        ArrayAdapter<PedidoDetalle> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, detalle);
        //PUNTO F
        lista_detalle.setAdapter(adapter);


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

        agregar_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Productos_ofrecidos.class);
                intent.putExtra("NUEVO_PEDIDO", 1);
                startActivityForResult(intent, 777);
            }
        });

        Bundle extras = data.getExtras();
        Integer ID = (Integer) extras.get("idProducto");
        Integer cantidad = (Integer) extras.get("cantidad");
        RepositorioProducto = new ProductoRepository();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        PedidoDetalle d = new PedidoDetalle(cantidad, repositorioProducto.buscarPorId(ID));
        detalle.add(d);
    //punto h
    }
}
