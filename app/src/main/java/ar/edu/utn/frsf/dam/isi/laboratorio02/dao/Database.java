package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.RoomDatabase;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
                                                                                //Valor de versión no se a que se refiere
@android.arch.persistence.room.Database(entities = {Categoria.class/*,Producto.class,Pedido.class*/},version = 2)
public abstract class Database extends RoomDatabase{
    public abstract CategoriaDAO categoriaDAO();

}
