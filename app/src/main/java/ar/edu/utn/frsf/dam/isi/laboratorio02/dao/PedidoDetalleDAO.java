package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoConDetalles;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

@Dao
public interface PedidoDetalleDAO {

    @Query("SELECT * FROM PedidoDetalle")
    List<PedidoDetalle> getAll();

    @Query("SELECT * FROM PedidoDetalle WHERE PedidoDetalle.id IN (:pedidoDetalleId)")
    List<PedidoDetalle> cargarPorId(int[] pedidoDetalleId);

    @Query("SELECT * FROM PedidoDetalle WHERE PedidoDetalle.id = :pedidoDetalleID ")
    PedidoDetalle cargarPedidoDetalleId(int pedidoDetalleID);


    @Query("SELECT * FROM PedidoDetalle WHERE PedidoDetalle.ped_id = :IdPedido")
    List<PedidoConDetalles> buscarDetalleporIdPedido(int IdPedido);

    @Insert
    void insertAll(List<PedidoDetalle> pedidosDetalles);

    @Insert
    void insertOne(PedidoDetalle pedidosDetalle);

    @Update
    void update(PedidoDetalle pedidosDetalle);

    @Update
    void updateAll(List<PedidoDetalle> pedidosDetalle);

    @Delete
    void delete(PedidoDetalle pedidosDetalle);
}
