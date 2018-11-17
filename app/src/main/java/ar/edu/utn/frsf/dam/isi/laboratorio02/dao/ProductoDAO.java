package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

@Dao
public interface ProductoDAO {
    @Query("SELECT * FROM Producto")
    List<Producto> getAll();

    @Query("SELECT * FROM Producto WHERE id IN (:productoId)")
    List<Producto> cargarPorId(int[] productoId);

    @Query("SELECT * FROM Producto WHERE id IN (:productoId)")
    Producto cargarPorId(int productoId);

    /*@Query("SELECT * FROM Producto WHERE cat_id (:categoriaId)")
    List<Producto> buscarProductosPorIdCategoria(int categoriaId);*/

    @Insert
    void insertAll(List<Producto> productos);

    @Insert
    void insertOne(Producto producto);

    @Update
    void update(Producto producto);

    @Delete
    void delete(Producto producto);
}
