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

import ar.edu.utn.frsf.dam.isi.laboratorio02.REST.RestClient;
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
                    Double precio = new Double(precioProducto.getText().toString());

                    Producto p = new Producto(nombreProducto.getText().toString(), descProducto.getText().toString(), precio, (Categoria) comboCategorias.getSelectedItem());
                    ProductoRetrofit clienteRest = RestClient.getInstance().getRetrofit().create(ProductoRetrofit.class);
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
                    });
                }
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idProductoBuscar.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Debe completar el ID a buscar", Toast.LENGTH_LONG).show();
                } else {
                    ProductoRetrofit clienteRest = RestClient.getInstance().getRetrofit().create(ProductoRetrofit.class);
                    Integer idabuscar = new Integer(idProductoBuscar.getText().toString());
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
                    });
                }
            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
