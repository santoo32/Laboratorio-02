package ar.edu.utn.frsf.dam.isi.laboratorio02.CustomAdapters;

import android.content.Context;
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

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.PedidoHolder;
import ar.edu.utn.frsf.dam.isi.laboratorio02.R;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class AdaptadorFilaHistorial extends ArrayAdapter<Pedido> implements View.OnClickListener{
    private Context ctx;
    private List<Pedido> datos;
    public AdaptadorFilaHistorial(Context context,List<Pedido> objects) {
        super(context, 0, objects);
        this.ctx = context;
        this.datos = objects;
    }

    private int lastPosition = -1;

    @Override
    public void onClick(View v) {
        int indice = (int) v.getTag();
        Pedido pedidoSeleccionado = datos.get(indice);
        if( pedidoSeleccionado.getEstado().equals(Pedido.Estado.REALIZADO)|| pedidoSeleccionado.getEstado().equals(Pedido.Estado.ACEPTADO)|| pedidoSeleccionado.getEstado().equals(Pedido.Estado.EN_PREPARACION)){
            pedidoSeleccionado.setEstado(Pedido.Estado.CANCELADO);
            AdaptadorFilaHistorial.this.notifyDataSetChanged();
        }
            return;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Pedido dataModel = getItem(position);

        PedidoHolder viewHolder;

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

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PedidoHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(ctx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.contacto.setText(dataModel.getMailContacto());
        viewHolder.fecha_entrega.setText(dataModel.getFecha().toString());
        viewHolder.imageview.setTag(position);
        //viewHolder.items.setText(dataModel.getMailContacto());
        //viewHolder.pagar = (TextView) convertView.findViewById(R.id.textView15);



        if(dataModel.getRetirar()==true){
            viewHolder.imageview.setImageResource(R.drawable.truck);
        }
        else{
            viewHolder.imageview.setImageResource(R.drawable.knifeandfork);

        }




        switch (dataModel.getEstado()){
            case LISTO: viewHolder.estado.setTextColor(Color.DKGRAY);
            break;
            case ENTREGADO: viewHolder.estado.setTextColor(Color.BLUE);
            break;
            case CANCELADO: case RECHAZADO: viewHolder.estado.setTextColor(Color.RED);
            break;
            case ACEPTADO: viewHolder.estado.setTextColor(Color.GREEN);
            break;
            case EN_PREPARACION: viewHolder.estado.setTextColor(Color.MAGENTA);
            break;
            case REALIZADO: viewHolder.estado.setTextColor(Color.BLUE); break; }


        return convertView;
    }
}
