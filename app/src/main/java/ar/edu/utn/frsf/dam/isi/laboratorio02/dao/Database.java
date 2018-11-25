package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.RoomDatabase;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

@android.arch.persistence.room.Database(entities = {Categoria.class,Producto.class,Pedido.class,PedidoDetalle.class},version = 5/*,exportSchema = false*/)
public abstract class Database extends RoomDatabase{
    public abstract CategoriaDAO categoriaDAO();
    public abstract  ProductoDAO productoDAO();
    public abstract PedidoDAO pedidoDAO();
    public abstract PedidoDetalleDAO pedidoDetalleDAO();
}
