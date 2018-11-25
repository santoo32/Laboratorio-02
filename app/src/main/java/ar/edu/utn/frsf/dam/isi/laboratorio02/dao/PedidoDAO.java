package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

@Dao
public interface PedidoDAO {
    @Query("SELECT * FROM Pedido")
    List<Pedido> getAll();

    @Query("SELECT * FROM Pedido WHERE Pedido.id IN (:pedidoId)")
    List<Pedido> cargarPorId(int[] pedidoId);

    @Query("SELECT * FROM Pedido WHERE Pedido.id = :pedidoID ")
    Pedido cargarPedidoId(int pedidoID);

    @Insert
    void insertAll(List<Pedido> pedido);

    @Insert
    void insertOne(Pedido pedido);

    @Update
    void update(Pedido pedido);

    @Update
    void updatePedidos(List<Pedido> pedidos);

    @Delete
    void delete(Pedido pedido);
}
