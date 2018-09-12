package ar.edu.utn.frsf.dam.isi.laboratorio02.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {

    public enum Estado { REALIZADO, ACEPTADO,EN_PREPARACION,EN_ENVIO,ENTREGADO,CANCELADO}

    private Integer id;
    private Date fecha;
    private List<PedidoDetalle> detalle;
    private Estado estado;
    private String direccionEnvio;
    private String mailContacto;
    private Boolean retirar;

    public Pedido() {
    }

    public Pedido(Date fecha, List<PedidoDetalle> detalle, Estado estado, String direccionEnvio, String mailContacto, Boolean retirar) {
        this.fecha = fecha;
        this.detalle = detalle;
        this.estado = estado;
        this.direccionEnvio = direccionEnvio;
        this.mailContacto = mailContacto;
        this.retirar = retirar;
    }

    public Pedido(Date fecha, Estado estado) {
        this.fecha = fecha;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<PedidoDetalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<PedidoDetalle> detalle) {
        this.detalle = detalle;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void agregarDetalle(PedidoDetalle det){
        if(this.detalle == null) this.detalle = new ArrayList<>();
        this.detalle.add(det);
    }

    public void quitarDetalle(PedidoDetalle det){
        if(this.detalle != null) this.detalle.remove(det);
    }

}
