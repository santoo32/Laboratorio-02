package ar.edu.utn.frsf.dam.isi.laboratorio02.REST;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.ProtocolException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ar.edu.utn.frsf.dam.isi.laboratorio02.CustomAdapters.EstadoPedidoReceiver;
import ar.edu.utn.frsf.dam.isi.laboratorio02.MainActivity;
import ar.edu.utn.frsf.dam.isi.laboratorio02.R;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.CategoriaDAO;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class CategoriaActivity extends AppCompatActivity {

    private EditText textoCat;
    private Button btnCrear;
    private Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        textoCat = (EditText) findViewById(R.id.txtNombreCategoria);
        btnCrear = (Button) findViewById(R.id.btnCrearCategoria);

        //lab 4 parte 2 - req05 -- Inicializo la bd
        MyDatabase.getInstance(getApplicationContext());

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.currentThread().sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        final Categoria cat1 = new Categoria(textoCat.getText().toString());
                        //lab 4 parte 1
                        /*
                        CategoriaRest Restrepo = new CategoriaRest();
                        try {
                            Restrepo.crearCategoria(cat1);
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        }*/

                        //lab 4 parte 2 - req05

                        //Crea la categoria en la db local
                        if(MyDatabase.insertOne(cat1)){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CategoriaActivity.this, "La categoria fue creada", Toast.LENGTH_SHORT).show();
                                    textoCat.setText("");
                                    }
                            });
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CategoriaActivity.this, "La categoria ya existe", Toast.LENGTH_SHORT).show();
                                    textoCat.setText("");
                                }
                            });
                        }
                    }
                };
                Thread unHilo = new Thread();
                new Thread(r, ("unHilo")).start();
                finish();
            }
        });
        btnMenu = (Button) findViewById(R.id.btnCategoriaVolver);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoriaActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
