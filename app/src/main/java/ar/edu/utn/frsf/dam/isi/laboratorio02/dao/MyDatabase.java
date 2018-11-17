package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class MyDatabase {

    // variable de clase privada que almacena una instancia unica de esta entidad
    private static MyDatabase _INSTANCIA_UNICA=null;
    private Database db;
    private static CategoriaDAO categoriaDAO;
    private static ProductoDAO productoDAO;
    private static List<Producto> LISTA_PRODUCTOS = new ArrayList<>();
    private static List<Categoria> CATEGORIAS_PRODUCTOS = new ArrayList<>();
    private static boolean FLAG_INICIALIZADO = false;

    //Inicializo los productos
    private static void inicializar(){
        int id = 0;
        Random rand = new Random();
        CATEGORIAS_PRODUCTOS.add(new Categoria(/*1,*/"Entrada"));
        CATEGORIAS_PRODUCTOS.add(new Categoria(/*2,*/"Plato Principal"));
        CATEGORIAS_PRODUCTOS.add(new Categoria(/*3,*/"Postre"));
        CATEGORIAS_PRODUCTOS.add(new Categoria(/*4,*/"Bebida"));
        categoriaDAO.insertAll(CATEGORIAS_PRODUCTOS);
        for(Categoria cat: CATEGORIAS_PRODUCTOS){
            for(int i=0;i<25;i++){
                LISTA_PRODUCTOS.add(new Producto(/*id++,*/cat.getNombre()+" 1"+i,"descripcion "+(i*id++)+rand.nextInt(100),rand.nextDouble()*500,cat));
            }
        }
        productoDAO.insertAll(LISTA_PRODUCTOS);
        FLAG_INICIALIZADO=true;
    }

    // metodo static publico que retorna la unica instancia de esta clase
    // si no existe, cosa que ocurre la primera vez que se invoca
    // la crea, y si existe retorna la instancia existente.

    public static MyDatabase getInstance(Context ctx){
        if(_INSTANCIA_UNICA==null){
            _INSTANCIA_UNICA = new MyDatabase(ctx);
        }
        return _INSTANCIA_UNICA;
    }

    // constructor privado para poder implementar SINGLETON
    // al ser privado solo puede ser invocado dentro de esta clase
    // el único lugar donde se invoca es en la linea 16 de esta clase
    // y se invocará UNA Y SOLO UNA VEZ, cuando _INSTANCIA_UNICA sea null
    // luego ya no se invoca nunca más. Nos aseguramos de que haya una
    // sola instancia en toda la aplicacion
    private MyDatabase(Context ctx){
        db = Room.databaseBuilder(ctx,
                Database.class, "dbPedidosCasiYa")
                .fallbackToDestructiveMigration()
                .build();
        if(!FLAG_INICIALIZADO) inicializar();
        categoriaDAO = db.categoriaDAO();
        productoDAO = db.productoDAO();

    }

    public static List<Categoria> getAll() {
        return categoriaDAO.getAll();
    }

    public static List<Categoria> cargarPorId(int[] categoriaIds) {
        return categoriaDAO.cargarPorId(categoriaIds);
    }

    public static void insertAll(List<Categoria> categorias) {
        categoriaDAO.insertAll(categorias);
    }

    public static void insertOne(Categoria categoria) {
        categoriaDAO.insertOne(categoria);
    }

    public static void delete(Categoria categoria) {
        categoriaDAO.delete(categoria);
    }

    public static void update(Categoria categoria) {
        categoriaDAO.update(categoria);
    }

    public static List<Producto> getAllProductos() {
        return productoDAO.getAll();
    }

    public static List<Producto> cargarPorIdProductos(int[] productoIds) {
        return productoDAO.cargarPorId(productoIds);
    }

    public static Producto cargarPorIdProducto(int productoId) {
        return productoDAO.cargarPorId(productoId);
    }

    public static List<Producto> buscarPorCategoria(Categoria categoria) {
        List<Producto> resultado = new ArrayList<>();
        //Busco todos los productos
        List<Producto> todosLosProductos = productoDAO.getAll();
        //Por cada producto me fijo si pertenece a la categoria que se pasa como parametro
        for(Producto p: todosLosProductos){
            if(p.getCategoria().getId().equals(categoria.getId())){
                resultado.add(p);
            }
        }
        //Devuelvo resultado
        return resultado;
    }

    public static void insertAllProductos(List<Producto> productos) {
        productoDAO.insertAll(productos);
    }

    public static void insertOneProduct(Producto producto) {
        productoDAO.insertOne(producto);
    }

    public static void deleteProducto(Producto producto) {
        productoDAO.delete(producto);
    }

    public static void updateProducto(Producto producto) {
        productoDAO.update(producto);
    }
}
