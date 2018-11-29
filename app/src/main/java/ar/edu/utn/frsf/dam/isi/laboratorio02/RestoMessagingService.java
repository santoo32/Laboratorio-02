package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ar.edu.utn.frsf.dam.isi.laboratorio02.CustomAdapters.EstadoPedidoReceiver;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

public class RestoMessagingService extends FirebaseMessagingService {
    private BroadcastReceiver br;
    private IntentFilter filtro;
    private Pedido p;
    //private PedidoRepository pedRepo;
    private List<PedidoDetalle> detalleDePedidos = new ArrayList<>();

    public RestoMessagingService() {
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Extraigos los datos del mensaje remoto
        Object[] remoteData = remoteMessage.getData().values().toArray();
        final int id = Integer.valueOf(remoteData[0].toString());
        //Map<String,String> remoteData = remoteMessage.getData();
        //int id = Integer.valueOf(remoteData.get("ID_PEDIDO"));
        //String estado = remoteData.get("NUEVO_ESTADO");

                //Obtengo el pedido por el id
        //pedRepo = new PedidoRepository();
        //p = pedRepo.buscarPorId(id);

        MyDatabase.getInstance(getApplicationContext());
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                p = MyDatabase.cargarPorIdPedidos(id);
                //Actualizo su estado a LISTO
                p.setEstado(Pedido.Estado.LISTO);
                //Actualizo el pedido en la bd
                MyDatabase.updatePedido(p);
            }
        };
        Thread hiloActualizarPedido = new Thread(r1);
        hiloActualizarPedido.start();

        try {
            hiloActualizarPedido.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                detalleDePedidos = MyDatabase.buscarTodosLosDetalleDeUnPedido(p.getId());

                for(PedidoDetalle pedDet : detalleDePedidos){
                    pedDet.setPedido(p);
                }

                MyDatabase.updatePedidoDetalleAll(detalleDePedidos);
            }
        };
        Thread hiloActualizarDetalles = new Thread(r);
        hiloActualizarDetalles.start();

        //Mando un mensaje al BroadcastReceiver
        br = new EstadoPedidoReceiver();
        filtro = new IntentFilter();
        filtro.addAction(EstadoPedidoReceiver.ESTADO_LISTO);
        getApplication().getApplicationContext().registerReceiver(br, filtro);
        final Intent i = new Intent();

        i.putExtra("idPedido",p.getId());
        i.putExtra("estado", "ESTADO_LISTO");
        i.setAction(EstadoPedidoReceiver.ESTADO_LISTO);
        sendBroadcast(i);

    }
    /*@Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }*/
}
