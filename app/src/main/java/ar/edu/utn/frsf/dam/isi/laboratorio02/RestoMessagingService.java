package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import ar.edu.utn.frsf.dam.isi.laboratorio02.CustomAdapters.EstadoPedidoReceiver;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class RestoMessagingService extends FirebaseMessagingService {
    private BroadcastReceiver br;
    private IntentFilter filtro;
    private Pedido p;
    private PedidoRepository pedRepo;

    public RestoMessagingService() {
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Extraigos los datos del mensaje remoto
        Map<String,String> remoteData = remoteMessage.getData();
        int id = Integer.parseInt(remoteData.get("ID_PEDIDO"));
        String estado = remoteData.get("NUEVO_ESTADO");

        //Obtengo el pedido por el id
        pedRepo = new PedidoRepository();
        p = pedRepo.buscarPorId(id);
        //Actualizo su estado a LISTO
        p.setEstado(Pedido.Estado.LISTO);

        //Mando un mensaje al BroadcastReceiver
        br = new EstadoPedidoReceiver();
        filtro = new IntentFilter();
        filtro.addAction(EstadoPedidoReceiver.ESTADO_LISTO);
        getApplication().getApplicationContext().registerReceiver(br, filtro);
        final Intent i = new Intent();

        i.putExtra("idPedido",p.getId());
        i.setAction(EstadoPedidoReceiver.ESTADO_LISTO);
        sendBroadcast(i);
    }
    /*@Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }*/
}
