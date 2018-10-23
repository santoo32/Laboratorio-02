package ar.edu.utn.frsf.dam.isi.laboratorio02.modelo;

import java.util.List;

public class PedidoDetalle {

    private static int ID_DETALLE =1;
    private Integer id;
    private Integer cantidad;
    private Producto producto;
    private Pedido pedido;

    public PedidoDetalle(Integer cantidad, Producto producto) {
        id=ID_DETALLE++;
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
        pedido.agregarDetalle(this);
    }

    @Override
    public String toString() {
        return producto.getNombre() + "( $"+producto.getPrecio()+")"+ cantidad;
    }

    public Double calcularCosto(List<PedidoDetalle> detalle) {

        Double costo = 0.0;

        for(int i = 0; i < detalle.size(); i++){

            costo += detalle.get(i).getProducto().getPrecio() * detalle.get(i).getCantidad();

        }


        return costo;
    }
}
