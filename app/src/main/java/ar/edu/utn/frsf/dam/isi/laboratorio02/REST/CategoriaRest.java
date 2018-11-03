package ar.edu.utn.frsf.dam.isi.laboratorio02.REST;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public class CategoriaRest {

    public CategoriaRest() {
    }

    public void crearCategoria(Categoria c) throws ProtocolException {

        //Variables de conexión y stream de escritura y lectura
        HttpURLConnection urlConnection = null;
        DataOutputStream printout = null;
        InputStream in = null;

        //Crear el objeto json que representa una categoria
        JSONObject categoriaJson = new JSONObject();
        try {
            categoriaJson.put("nombre", c.getNombre());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Abrir una conexión al servidor para enviar el POST
        URL url = null;
        try {
            url = new URL("http://10.0.2.2:5000/categorias");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        urlConnection.setChunkedStreamingMode(0);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");

        //Obtener el outputStream para escribir el JSON
        try {
            printout = new DataOutputStream(urlConnection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = categoriaJson.toString();
        byte[] jsonData = new byte[0];
        try {
            jsonData = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            printout.write(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            printout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Leer la respuesta
        try {
            in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader isw = new InputStreamReader(in);
        StringBuilder sb = new StringBuilder();
        int data = 0;
        try {
            data = isw.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Analizar el codigo de lar respuesta
        try {
            if (urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201) {
                while (data != -1) {
                    char current = (char) data;
                    sb.append(current);
                    data = isw.read();
                }
                //analizar los datos recibidos
                Log.d("LAB_04", sb.toString());
            } else {
                // lanzar excepcion o mostrar mensaje de error // que no se pudo ejecutar la operacion
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // caputurar todas las excepciones y en el bloque finally // cerrar todos los streams y HTTPUrlCOnnection
        if (printout != null) try {
            printout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (in != null) try {
            in.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        if (urlConnection != null) urlConnection.disconnect();
    }
}
