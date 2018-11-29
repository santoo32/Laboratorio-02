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

    @Query("SELECT Producto.nombre FROM Producto")
    List<String> getAllNombres();

    @Query("SELECT * FROM Producto WHERE id IN (:productoId)")
    List<Producto> cargarPorId(int[] productoId);

    @Query("SELECT * FROM Producto WHERE Producto.id = :productoId ")
    Producto cargarProductoId(int productoId);

    @Query("SELECT * FROM Producto INNER JOIN Categoria ON Categoria.id = Producto.cat_id WHERE Producto.cat_id = :categoriaId")
    List<Producto> buscarProductosPorIdCategoria(int categoriaId);

    @Query("SELECT * FROM Producto INNER JOIN Categoria ON Categoria.id = Producto.cat_id" +
            " WHERE Producto.nombre = :nombre AND Producto.precio = :precio AND Producto.cat_nombre = :nombreCate")
    Producto buscarProducto(String nombre, Double precio, String nombreCate);

    @Insert
    void insertAll(List<Producto> productos);

    @Insert
    void insertOne(Producto producto);

    @Update
    void update(Producto producto);

    @Delete
    void delete(Producto producto);
}
