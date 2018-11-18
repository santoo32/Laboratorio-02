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
    private static List<Producto> LISTA_PRODUCTOS;
    private static List<Categoria> CATEGORIAS_PRODUCTOS;
    private static boolean FLAG_INICIALIZADO = false;
    private static List<Producto> resultado;

    //Inicializo los productos
    private static void inicializar(){
        int id = 0;
        Random rand = new Random();
        CATEGORIAS_PRODUCTOS.add(new Categoria(1,"Entrada"));
        CATEGORIAS_PRODUCTOS.add(new Categoria(2,"Plato Principal"));
        CATEGORIAS_PRODUCTOS.add(new Categoria(3,"Postre"));
        CATEGORIAS_PRODUCTOS.add(new Categoria(4,"Bebida"));

        for(Categoria cat: CATEGORIAS_PRODUCTOS){
            for(int i=0;i<5;i++){
                LISTA_PRODUCTOS.add(new Producto(id++,cat.getNombre()+" 1"+i,"descripcion "+(i*id++)+rand.nextInt(100),rand.nextDouble()*500,cat));
            }
        }

        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                categoriaDAO.insertAll(CATEGORIAS_PRODUCTOS);
            }
        };
        Thread cargarCategoria = new Thread(r1);
        cargarCategoria.start();

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                productoDAO.insertAll(LISTA_PRODUCTOS);
            }
        };
        Thread cargarProducto = new Thread(r2);
        cargarProducto.start();
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

        CATEGORIAS_PRODUCTOS = new ArrayList<>();
        LISTA_PRODUCTOS = new ArrayList<>();
        categoriaDAO = db.categoriaDAO();
        productoDAO = db.productoDAO();

        /*if(!FLAG_INICIALIZADO){
            Runnable r1 = new Runnable() {
                @Override
                public void run() {
                    List <Categoria> categoria = categoriaDAO.getAll();
                    for(Categoria c : categoria){
                        categoriaDAO.delete(c);
                    }
                }
            };
            Thread borrarCategoria = new Thread(r1);
            borrarCategoria.start();

            Runnable r2 = new Runnable() {
                @Override
                public void run() {
                    List <Producto> producto = productoDAO.getAll();
                    for(Producto p : producto){
                        productoDAO.delete(p);
                    }
                }
            };
            Thread borrarProducto = new Thread(r2);
            borrarProducto.start();

            inicializar();
        }*/


    }

    public static List<Categoria> getAll() {
        return categoriaDAO.getAll();
    }

    public static Categoria getCategoria(final String nombreCategoria) {
        return categoriaDAO.getCategoria(nombreCategoria);
    }

    public static List<Categoria> cargarPorId(int[] categoriaIds) {
        return categoriaDAO.cargarPorId(categoriaIds);
    }

    public static void insertAll(List<Categoria> categorias) {
        categoriaDAO.insertAll(categorias);
    }

    public static boolean insertOne(Categoria categoria) {
        //Verifico que la categoria que quiero agregar no se encuentre en la bd
        Boolean b = false;
        List<String> todoNombresCategorias = categoriaDAO.getAllNombres();
        if (!todoNombresCategorias.contains(categoria.getNombre())) {
            categoriaDAO.insertOne(categoria);
            b = true;
        }else{
            b = false;
        }
        return b;
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

    public static Producto cargarPorIdProducto(int productoId, int categoriaId) {
        return productoDAO.cargarPorId(productoId, categoriaId);
    }

    public static List<Producto> buscarPorCategoria(final Categoria categoria) {


        Runnable r = new Runnable() {
            @Override
            public void run() {
                /*
                //Busco todos los productos
                List<Producto> todosLosProductos = productoDAO.getAll();
                //Por cada producto me fijo si pertenece a la categoria que se pasa como parametro

                for(Producto p: todosLosProductos){
                    if(p.getCategoria().getNombre().equals(categoria.getNombre())){
                        resultado.add(p);
                    }
                }*/

                resultado = productoDAO.buscarProductosPorIdCategoria(categoria.getId());
            }
        };
        Thread hiloTodosProductos = new Thread(r);
        hiloTodosProductos.start();

        //Devuelvo resultado
        return resultado;
    }

    public static void insertAllProductos(List<Producto> productos) {
        productoDAO.insertAll(productos);
    }

    public static boolean insertOneProduct(Producto producto) {
        //Verifico que la categoria que quiero agregar no se encuentre en la bd
        Boolean b = false;
        List<String> todoNombresProductos = productoDAO.getAllNombres();
        if (!todoNombresProductos.contains(producto.getNombre())) {
            productoDAO.insertOne(producto);
            b = true;
        }else{
            b = false;
        }
        return b;

    }

    public static void deleteProducto(Producto producto) {
        productoDAO.delete(producto);
    }

    public static void updateProducto(Producto producto) {
        productoDAO.update(producto);
    }
}
