package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class Alta_pedidos extends AppCompatActivity{

    private Pedido unPedido;
    private PedidoRepository repositorioPedido;
    private ProductoRepository repositorioProducto;
    private RadioButton retiro_local;
    private RadioButton envio_domicilio;
    private EditText domicilio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_pedidos);

        retiro_local = (RadioButton) findViewById(R.id.radioButton);
        envio_domicilio = (RadioButton) findViewById(R.id.radioButton2);
        domicilio = (EditText)findViewById(R.id.editText3);
        envio_domicilio.setChecked(true);

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

    }
}
