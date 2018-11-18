package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.REST.RestClient;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRetrofit;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GestionProductoActivity extends AppCompatActivity {

    private Button btnMenu;
    private Button btnGuardar;
    private Spinner comboCategorias;
    private EditText nombreProducto;
    private EditText descProducto;
    private EditText precioProducto;
    private ToggleButton opcionNuevoBusqueda;
    private EditText idProductoBuscar;
    private Button btnBuscar;
    private Button btnBorrar;
    private Boolean flagActualizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_producto);
        final ProductoRepository prodrepos = new ProductoRepository();


        flagActualizacion = false;
        opcionNuevoBusqueda = (ToggleButton) findViewById(R.id.abmProductoAltaNuevo);
        idProductoBuscar = (EditText) findViewById(R.id.abmProductoIdBuscar);
        nombreProducto = (EditText) findViewById(R.id.abmProductoNombre);
        descProducto = (EditText) findViewById(R.id.abmProductoDescripcion);
        precioProducto = (EditText) findViewById(R.id.abmProductoPrecio);
        comboCategorias = (Spinner) findViewById(R.id.abmProductoCategoria);
        btnMenu = (Button) findViewById(R.id.btnAbmProductoVolver);
        btnGuardar = (Button) findViewById(R.id.btnAbmProductoCrear);
        btnBuscar = (Button) findViewById(R.id.btnAbmProductoBuscar);
        btnBorrar = (Button) findViewById(R.id.btnAbmProductoBorrar);
        opcionNuevoBusqueda.setChecked(false);
        btnBuscar.setEnabled(false);
        btnBorrar.setEnabled(false);
        idProductoBuscar.setEnabled(false);
        comboCategorias.setSelection(0);

        nombreProducto.setText("");
        descProducto.setText("");
        precioProducto.setText("");
        idProductoBuscar.setText("");

        Runnable r = new Runnable() {
            @Override
            public void run() {
                MyDatabase.getInstance(getApplicationContext());
                final List<Categoria> cats = MyDatabase.getAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<Categoria> categoriasAdapter = new ArrayAdapter<Categoria>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, cats);
                        comboCategorias.setAdapter(categoriasAdapter);
                        comboCategorias.setSelection(0);
                    }
                });
            }
        };
        Thread hiloCagarAdaptadorCategoria = new Thread(r);
        hiloCagarAdaptadorCategoria.start();



        opcionNuevoBusqueda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flagActualizacion = isChecked;
                btnBuscar.setEnabled(isChecked);
                btnBorrar.setEnabled(isChecked);
                idProductoBuscar.setEnabled(isChecked);

            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nombreProducto.getText().toString().equals("") || descProducto.getText().toString().equals("") || precioProducto.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Debe completar todos los campos", Toast.LENGTH_LONG).show();
                } else {
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Double precio = new Double(precioProducto.getText().toString());
                            //Si no es una actaualización creo un producto nuevo
                            if(!flagActualizacion){

                                Producto p = new Producto(nombreProducto.getText().toString(), descProducto.getText().toString(), precio, (Categoria) comboCategorias.getSelectedItem());
                                //Req 06 Parte 2 taller 4
                                MyDatabase.getInstance(getApplicationContext());
                                if(MyDatabase.insertOneProduct(p)){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(GestionProductoActivity.this,"El producto fue creado",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(GestionProductoActivity.this,"El producto ya existe",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }else{

                                //Obtengo el producto
                                Producto prodActualizar = MyDatabase.cargarPorIdProducto(Integer.parseInt(idProductoBuscar.getText().toString()), ((Categoria) comboCategorias.getItemAtPosition(0)).getId());
                                //Actualizo los datos
                                prodActualizar.setNombre(nombreProducto.getText().toString());
                                prodActualizar.setDescripcion(descProducto.getText().toString());
                                prodActualizar.setPrecio(precio);
                                prodActualizar.setCategoria((Categoria)comboCategorias.getSelectedItem());
                                //Guardo el producto actualizado
                                MyDatabase.updateProducto(prodActualizar);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GestionProductoActivity.this,"El producto fue actualizado",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    };
                    Thread hiloGuardarProducto = new Thread(r);
                    hiloGuardarProducto.start();

                    /*ProductoRetrofit clienteRest = RestClient.getInstance().getRetrofit().create(ProductoRetrofit.class);
                    Call<Producto> altaCall = clienteRest.crearProducto(p);

                    System.out.println(p);

                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> resp) {
                            //Producto onresponse = resp.body();
                            //hay que chequear si el producto ya existe para borrar el viejo y poner el actualizado
                            //adaptador_prod.notifyDataSetChanged(onresponse);
                            Toast.makeText(getApplicationContext(), "Producto creado", Toast.LENGTH_LONG).show();
                            nombreProducto.setText("");
                            descProducto.setText("");
                            precioProducto.setText("");

                        }

                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Algo salio mal, intente nuevamente mas tarde", Toast.LENGTH_LONG).show();
                        }
                    });*/
                }
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idProductoBuscar.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Debe completar el ID a buscar", Toast.LENGTH_LONG).show();
                } else {
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Integer idABuscar = new Integer(idProductoBuscar.getText().toString());
                            Categoria categoriaSeleccionada = (Categoria) comboCategorias.getItemAtPosition(0);
                            MyDatabase.getInstance(getApplicationContext());
                            //No devuelve nada!!
                            final Producto prodBuscado = MyDatabase.cargarPorIdProducto(idABuscar, categoriaSeleccionada.getId());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    nombreProducto.setText(prodBuscado.getNombre());
                                    descProducto.setText(prodBuscado.getDescripcion());
                                    precioProducto.setText(String.valueOf(prodBuscado.getPrecio()));
                                    comboCategorias.setSelection(prodBuscado.getCategoria().getId()-1);
                                }
                            });
                        }
                    };
                    Thread hiloBuscar = new Thread(r);
                    hiloBuscar.start();

                    /*ProductoRetrofit clienteRest = RestClient.getInstance().getRetrofit().create(ProductoRetrofit.class);
                    Call<Producto> altaCall = clienteRest.buscarProductoPorId(idabuscar);

                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> resp) {
                            Producto onresponse = resp.body();
                            nombreProducto.setText(onresponse.getNombre());
                            descProducto.setText(onresponse.getDescripcion());
                            precioProducto.setText(onresponse.getPrecio().toString());

                        }

                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Algo salio mal, intente nuevamente mas tarde", Toast.LENGTH_LONG).show();
                        }
                    });*/
                }
            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nombreProducto.getText().toString().equals("") || descProducto.getText().toString().equals("") || precioProducto.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Debe buscar un producto primero", Toast.LENGTH_LONG).show();
                } else {
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            //Busco el producto
                            Integer idABuscar = new Integer(idProductoBuscar.getText().toString());
                            MyDatabase.getInstance(getApplicationContext());
                            Producto prodBorrar = MyDatabase.cargarPorIdProducto(idABuscar, ((Categoria) comboCategorias.getItemAtPosition(0)).getId());
                            //Lo borro
                            MyDatabase.deleteProducto(prodBorrar);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Seteo los campos del layout
                                    nombreProducto.setText(null);
                                    descProducto.setText(null);
                                    precioProducto.setText(null);
                                    comboCategorias.setSelection(-1);
                                    idProductoBuscar.setText(null);

                                    Toast.makeText(GestionProductoActivity.this,"El producto fue eliminado", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    };
                    Thread hiloBorrar = new Thread(r);
                    hiloBorrar.start();
                    /*ProductoRetrofit clienteRest = RestClient.getInstance().getRetrofit().create(ProductoRetrofit.class);
                    Call<Producto> altaCall = clienteRest.borrar(idabuscar);
                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call, Response<Producto> resp) {
                            Producto onresponse = resp.body();
                            nombreProducto.setText("");
                            descProducto.setText("");
                            precioProducto.setText("");
                            Toast.makeText(getApplicationContext(), "Producto Borrado", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Algo salio mal, intente nuevamente mas tarde", Toast.LENGTH_LONG).show();
                        }
                    });*/
                }
            }
        });
    }
}
