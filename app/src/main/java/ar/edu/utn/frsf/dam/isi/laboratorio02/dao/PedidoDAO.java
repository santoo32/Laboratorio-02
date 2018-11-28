package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Relation;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoConDetalles;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

@Dao
public interface PedidoDAO {
    @Query("SELECT * FROM Pedido")
    List<Pedido> getAll();

    @Query("SELECT * FROM Pedido WHERE Pedido.id IN (:pedidoId)")
    List<Pedido> cargarPorId(int[] pedidoId);

    @Query("SELECT * FROM Pedido WHERE Pedido.id = :pedidoID ")
    Pedido cargarPedidoId(int pedidoID);

    /*
    @Query("SELECT * FROM Pedido WHERE id = :detalleIdPedido")
    List<PedidoConDetalles> buscarDetalleporIdPedido(int detalleIdPedido);
    */


    @Insert
    void insertAll(List<Pedido> pedido);

    @Insert
    long insertOne(Pedido pedido);

    @Update
    void update(Pedido pedido);

    @Update
    void updatePedidos(List<Pedido> pedidos);

    @Delete
    void delete(Pedido pedido);
}
