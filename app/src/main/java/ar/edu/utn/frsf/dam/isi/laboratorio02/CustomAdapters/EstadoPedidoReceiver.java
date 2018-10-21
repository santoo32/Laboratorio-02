package ar.edu.utn.frsf.dam.isi.laboratorio02.CustomAdapters;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.Alta_pedidos;
import ar.edu.utn.frsf.dam.isi.laboratorio02.Historial_pedidos;
import ar.edu.utn.frsf.dam.isi.laboratorio02.R;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class EstadoPedidoReceiver extends BroadcastReceiver {

    public  static final String ESTADO_ACEPTADO="frsf.dam.isi.laboratorio02.EVENTO_01_MSG";
    public  static final String ESTADO_EN_PREPARACION="frsf.dam.isi.laboratorio02.EVENTO_02_MSG";
    private PedidoRepository p1;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        int id = extras.getInt("id_pedido");
        p1 = new PedidoRepository();
        Pedido ped;

        if (intent.getAction().equals(ESTADO_ACEPTADO)) {
             ped = p1.buscarPorId(id);
            //Toast.makeText(context,"Pedido para " + ped.getMailContacto() + "ha cambiado de estado a aceptado",Toast.LENGTH_LONG).show();

            Intent i = new Intent(context,Historial_pedidos.class);
            PendingIntent penInt = PendingIntent.getActivity(context,0,i,0);
            //Creo la notificación
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "CANAL01")
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Tu pedido fue aceptado")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("El costo sera "+ ped.total())
                            .addLine("Previsto el envio para "+ped.getFecha()))
                    .setContentIntent(penInt)
                    .setAutoCancel(true);

            //Muestro la notificación
            NotificationManagerCompat notManager = NotificationManagerCompat.from(context);
            notManager.notify(99,mBuilder.build());
        }else{
            if(intent.getAction().equals(ESTADO_EN_PREPARACION)){
                ped = p1.buscarPorId(id);
                Intent i = new Intent(context,Historial_pedidos.class);
                PendingIntent penInt = PendingIntent.getActivity(context,0,i,0);
                //Creo la notificación
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "CANAL01")
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Tu pedido esta en preparacion")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("El pedido "+ped.getId().toString())
                        .addLine("Encargado por: "+ped.getMailContacto()))
                    .setContentIntent(penInt)
                    .setAutoCancel(true);

                //Muestro la notificación
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(98,mBuilder.build());
            }
        }
    }
}
