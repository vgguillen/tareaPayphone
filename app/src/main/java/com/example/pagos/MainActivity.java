package com.example.pagos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    String url = "https://pay.payphonetodoesposible.com/api/Sale";
    EditText telefono, cedula, iva, siniva, valorconiva;
    TextView respuesta;
    Button pago;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        telefono = (EditText) findViewById(R.id.txtTelef);
        cedula = (EditText) findViewById(R.id.txtCedula);
        iva = (EditText) findViewById(R.id.txtIva);
        siniva = (EditText) findViewById(R.id.txtSinIva);
        valorconiva = (EditText) findViewById(R.id.txtValorConIva);
        respuesta = (TextView) findViewById(R.id.idRespuesta);
        pago = (Button) findViewById(R.id.button);

        queue = Volley.newRequestQueue(this);

        pago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObjectRequest();
            }
        });
    }

    public String identificador(){

        String codigo = "";

        for (int i = 0; i < 6 ; i++) {
            Random r = new Random();
            char abc = (char) (r.nextInt(90 - 65 + 1)+65);
            codigo += String.valueOf(abc);
        }

        return codigo;
    }

    public int Suma(){
        int a, b, c;
        a = Integer.valueOf(iva.getText().toString());
        b = Integer.valueOf(siniva.getText().toString());
        c = Integer.valueOf(valorconiva.getText().toString());

        return a+b+c;
    }

    public JSONObject JSON() {
        JSONObject datos = new JSONObject();
        try {
            datos.put("phoneNumber", telefono.getText().toString());
            datos.put("countryCode", "593");
            datos.put("clientUserId", cedula.getText().toString());
            datos.put("reference", "none");
            datos.put("responseUrl", "http://paystoreCZ.com/confirm.php");
            datos.put("amount", Suma());
            datos.put("amountWithTax", valorconiva.getText().toString());
            datos.put("amountWithoutTax", siniva.getText().toString());
            datos.put("tax", iva.getText().toString());
            datos.put("clientTransactionId", identificador());
        }catch (Exception e){

        }

        return datos;
    }
    public void JsonObjectRequest(){
        System.out.println(JSON().toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                JSON(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            respuesta.setText("Tu id de transacción es:" + response.getString("transactionId"));
                            Toast.makeText(MainActivity.this, "Tu id de transacción es:" + response.getString("transactionId"), Toast.LENGTH_LONG);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        respuesta.setText(error.getMessage());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> Cabecera = new HashMap<>();
                Cabecera.put("Content-Type", "application/json");
                Cabecera.put("Accept", "application/json");
                Cabecera.put("Authorization", "Bearer jin1ON_A9btPWqANpoWlcnO7yyzk1h3khYPPEBGog1_1_rkzqXBxDl4QBPWNb4ST687g97atrRBmweIvbVNwdKEde1d5MqeqXf5BFho-syPfI13kMQ8UUhb5wxBO7VybBTXHkcTNAlpuhG05Ux9tDIX1IoJLmB_NacRqjeljZp45RejiuLsKSlD1xD3uyjokUiDyme_NmlHPlRWkUO8MZ-PE5dyTJUBdI5MCdVjoUx57U2U6aurpAOiPAVIecbOnE8K44ltegmeqnJh509laqT0YaL-Aw15qNPq0iM9vTMnynEcPoQ9dHXAtR1tkPb4xmDnvcg");
                return Cabecera;
            }
        };
        queue.add(jsonObjectRequest);
    }
}