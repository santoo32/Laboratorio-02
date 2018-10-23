package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.CustomAdapters.EstadoPedidoReceiver;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class PrepararPedidoService extends IntentService {
    private BroadcastReceiver br;
    private IntentFilter filtro;
    //private Pedido p1;

    public PrepararPedidoService() {
        super("PrepararPedidoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        br = new EstadoPedidoReceiver();
        filtro = new IntentFilter();
        filtro.addAction(EstadoPedidoReceiver.ESTADO_EN_PREPARACION);
        getApplication().getApplicationContext().registerReceiver(br, filtro);
        final Intent i = new Intent();


        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Creo instancia del repositorio
                PedidoRepository repositorioPedido = new PedidoRepository();
                //Obtengo la lista
                List<Pedido> lista = repositorioPedido.getLista();
                //Cambio el estado
                for (Pedido p : lista) {
                    if (p.getEstado().equals(Pedido.Estado.ACEPTADO)) {
                        p.setEstado(Pedido.Estado.EN_PREPARACION);

                        i.putExtra("idPedido",p.getId());
                        i.setAction(EstadoPedidoReceiver.ESTADO_EN_PREPARACION);
                        sendBroadcast(i);
                    }
                }


            }
        };
        Thread unHi = new Thread();
        new Thread(r , ("unHi")).start();

    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
