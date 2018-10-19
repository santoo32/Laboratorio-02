package ar.edu.utn.frsf.dam.isi.laboratorio02.CustomAdapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.Alta_pedidos;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class EstadoPedidoReceiver extends BroadcastReceiver {

    public  static final String ESTADO_ACEPTADO="frsf.dam.isi.laboratorio02.EVENTO_01_MSG";
    private PedidoRepository p1;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Bundle extras = intent.getExtras();
        int id = extras.getInt("id_pedido");

        if (intent.getAction().equals(ESTADO_ACEPTADO)) {
            p1 = new PedidoRepository();
            Pedido ped = p1.buscarPorId(id);
            Toast.makeText(context,"Pedido para " + ped.getMailContacto() + "ha cambiado de estado a aceptado",Toast.LENGTH_LONG).show();

        }
    }
}
