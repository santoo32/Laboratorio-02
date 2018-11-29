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
import android.util.Log;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.Alta_pedidos;
import ar.edu.utn.frsf.dam.isi.laboratorio02.Historial_pedidos;
import ar.edu.utn.frsf.dam.isi.laboratorio02.R;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class EstadoPedidoReceiver extends BroadcastReceiver {

    public  static final String ESTADO_ACEPTADO = "frsf.dam.isi.laboratorio02.EVENTO_01_MSG";
    public  static final String ESTADO_EN_PREPARACION = "frsf.dam.isi.laboratorio02.EVENTO_02_MSG";
    public  static final String ESTADO_LISTO = "frsf.dam.isi.laboratorio02.EVENTO_03_MSG";
    //private PedidoRepository p1;
    private Pedido ped;

    @Override
    public void onReceive(final Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        final Integer id = extras.getInt("idPedido");
        //p1 = new PedidoRepository();
        //Pedido ped;
        MyDatabase.getInstance(context);

        if (intent.getAction().equals(ESTADO_ACEPTADO)) {
            //ped = p1.buscarPorId(id);
            Runnable aceptado = new Runnable() {
                @Override
                public void run() {
                    ped = MyDatabase.cargarPorIdPedidos(id);

                    Log.i("id para la busqueda ", id.toString());

                    if(ped != null){
                        Log.i("id producto: ",ped.getId().toString());
                        Log.i("Fecha",ped.getFecha().toString());
                    }else{
                        Log.i("No devuelve el pedido","-----------------");
                    }

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
                }
            };
            Thread hiloAceptar = new Thread(aceptado);
            hiloAceptar.start();
            //Toast.makeText(context,"Pedido para " + ped.getMailContacto() + "ha cambiado de estado a aceptado",Toast.LENGTH_LONG).show();
        }

        if(intent.getAction().equals(ESTADO_EN_PREPARACION)){
            //ped = p1.buscarPorId(id);
            Runnable enPreparacion = new Runnable() {
                @Override
                public void run() {
                    ped = MyDatabase.cargarPorIdPedidos(id);

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
            };
            Thread hiloEnPreparacion = new Thread(enPreparacion);
            hiloEnPreparacion.start();
        }

        if(intent.getAction().equals(ESTADO_LISTO)){
            //ped = p1.buscarPorId(id);
            Runnable listo = new Runnable() {
                @Override
                public void run() {
                    ped = MyDatabase.cargarPorIdPedidos(id);

                    Intent i = new Intent(context,Historial_pedidos.class);
                    PendingIntent penInt = PendingIntent.getActivity(context,0,i,0);
                    //Creo la notificación
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "CANAL01")
                            .setSmallIcon(R.drawable.logo)
                            .setContentTitle("Tu pedido esta listo")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setStyle(new NotificationCompat.InboxStyle()
                                    .addLine("El costo sera "+ ped.total()))
                            .setContentIntent(penInt)
                            .setAutoCancel(true);

                    //Muestro la notificación
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    notificationManager.notify(97,mBuilder.build());
                }
            };
            Thread hiloListo = new Thread(listo);
            hiloListo.start();
        }
    }
}
