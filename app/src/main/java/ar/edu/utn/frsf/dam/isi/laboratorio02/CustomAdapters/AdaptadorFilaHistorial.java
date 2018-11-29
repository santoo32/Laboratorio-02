package ar.edu.utn.frsf.dam.isi.laboratorio02.CustomAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.Alta_pedidos;
import ar.edu.utn.frsf.dam.isi.laboratorio02.Historial_pedidos;
import ar.edu.utn.frsf.dam.isi.laboratorio02.PedidoHolder;
import ar.edu.utn.frsf.dam.isi.laboratorio02.Productos_ofrecidos;
import ar.edu.utn.frsf.dam.isi.laboratorio02.R;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

public class AdaptadorFilaHistorial extends ArrayAdapter<Pedido> implements View.OnClickListener{
    private Context ctx;
    private List<Pedido> datos;
    private List<PedidoDetalle> detalleDePedidos = new ArrayList<>();

    private int lastPosition = -1;


    public AdaptadorFilaHistorial(Context context,List<Pedido> objects) {
        super(context, 0, objects);
        this.ctx = context;
        this.datos = objects;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final Pedido dataModel = getItem(position);

        MyDatabase.getInstance(ctx);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                detalleDePedidos = MyDatabase.buscarTodosLosDetalleDeUnPedido(dataModel.getId());
            }
        };
        Thread hiloCargarDetalles = new Thread(r);
        hiloCargarDetalles.start();

        try {
            hiloCargarDetalles.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final PedidoHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new PedidoHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.activity_adaptadorfilahistorial, parent, false);
            viewHolder.contacto = (TextView) convertView.findViewById(R.id.textView11);
            viewHolder.fecha_entrega = (TextView) convertView.findViewById(R.id.textView13);
            viewHolder.items = (TextView) convertView.findViewById(R.id.textView14);
            viewHolder.pagar = (TextView) convertView.findViewById(R.id.textView15);
            viewHolder.estado = (TextView) convertView.findViewById(R.id.textView16);
            viewHolder.imageview = (ImageView) convertView.findViewById(R.id.imageView2);
            viewHolder.btnCancelar = (Button) convertView.findViewById(R.id.delete_btn);
            viewHolder.btnDetalles = (Button) convertView.findViewById(R.id.menu_btn);

            viewHolder.btnCancelar.setOnClickListener (new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    System.out.println("v.getTag: "+v.getTag());
                    System.out.println("position: "+position);

                    //int indice = (int) v.getTag();

                    final Pedido pedidoSeleccionado = datos.get(position/*indice*/);
                    //final Pedido pedidoSeleccionado = dataModel;

                    System.out.println(pedidoSeleccionado);
                    System.out.println(pedidoSeleccionado.getId());


                    if( pedidoSeleccionado.getEstado().equals(Pedido.Estado.REALIZADO)|| pedidoSeleccionado.getEstado().equals(Pedido.Estado.ACEPTADO)/*|| pedidoSeleccionado.getEstado().equals(Pedido.Estado.EN_PREPARACION)*/){
                        pedidoSeleccionado.setEstado(Pedido.Estado.CANCELADO);
                        AdaptadorFilaHistorial.this.notifyDataSetChanged();

                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                MyDatabase.updatePedido(pedidoSeleccionado);

                                //Tengo que actualizar todos los pedidos detalles del producto cancelado
                                //List<PedidoDetalle> pedidoDetalle = MyDatabase.getPedidoDetallesDeProducto(pedidoSeleccionado.getId());

                                for(PedidoDetalle pedDet : detalleDePedidos/*pedidoDetalle*/){
                                    pedDet.setPedido(pedidoSeleccionado);
                                }

                                MyDatabase.updatePedidoDetalleAll(detalleDePedidos/*pedidoDetalle*/);
                            }
                        };
                        Thread hiloCancelarPedido = new Thread(r);
                        hiloCancelarPedido.start();

                        try {
                            hiloCancelarPedido.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return;
                }
            });


            viewHolder.btnDetalles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ctx, "No implementado aun", Toast.LENGTH_LONG).show();
                }
            });
            result=convertView;

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (PedidoHolder) convertView.getTag();
            result=convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(ctx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        Double costo = calcularCosto(detalleDePedidos/*dataModel.getDetalle()*/);
        viewHolder.contacto.setText("Contacto: " + dataModel.getMailContacto());
        viewHolder.fecha_entrega.setText("Fecha de entrega: " + dataModel.getFecha().toString());
        viewHolder.imageview.setTag(position);
        viewHolder.items.setText("Items: " + detalleDePedidos.size()/*dataModel.getDetalle().size()*/);
        viewHolder.pagar.setText("Costo: " + costo);



        if(dataModel.getRetirar()==true){
            viewHolder.imageview.setImageResource(R.drawable.truck);
        }
        else{
            viewHolder.imageview.setImageResource(R.drawable.knifeandfork);

        }

        switch (dataModel.getEstado()){
            case LISTO:
                viewHolder.estado.setText("LISTO");
                viewHolder.estado.setTextColor(Color.DKGRAY);
            break;
            case ENTREGADO:
                viewHolder.estado.setText("ENTREGADO");
                viewHolder.estado.setTextColor(Color.BLUE);
            break;
            case CANCELADO: case RECHAZADO:
                viewHolder.estado.setText("CANCELADO/RECHAZADO");
                viewHolder.estado.setTextColor(Color.RED);
            break;
            case ACEPTADO:
                viewHolder.estado.setText("ACEPTADO");
                viewHolder.estado.setTextColor(Color.GREEN);
            break;
            case EN_PREPARACION:
                viewHolder.estado.setText("EN PREPARACION");
                viewHolder.estado.setTextColor(Color.MAGENTA);
            break;
            case REALIZADO:
                viewHolder.estado.setText("REALIZADO");
                viewHolder.estado.setTextColor(Color.BLUE); break; }


        return convertView;
    }

    private Double calcularCosto(List<PedidoDetalle> detalle) {
        Double costo = 0.0;

        for(int i = 0; i < detalle.size(); i++){

            costo += detalle.get(i).getProducto().getPrecio() * detalle.get(i).getCantidad();

        }
        return costo;
    }

    @Override
    public void onClick(View v) {

    }
}
