package com.rechargoyo.formula;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Preg1a1 extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rv;
    private List<Pregunta1a1> pregunta1a1;
    private Preguntas1a1Adapter preguntas1a1Adapter;
    private Button add, formula1a1;
    private RequestQueue rq;
    private EditText name1a1;
    private int type;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_preg1a1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle datos = getIntent().getExtras();
        type = datos.getInt("type",0);
        rv = findViewById(R.id.rvp1a1);
        add = findViewById(R.id.addp1a1);
        name1a1 = findViewById(R.id.name1a1);
        formula1a1 = findViewById(R.id.formula1a1);
        rq = Volley.newRequestQueue(this);

        if(type==1){
            name1a1.setHint("Relación de columnas");
        }else if(type==3){
            name1a1.setHint("Memorama");
        }

        pregunta1a1 = new ArrayList<>();

        preguntas1a1Adapter = new Preguntas1a1Adapter(this,pregunta1a1);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(preguntas1a1Adapter);

        pregunta1a1.add(new Pregunta1a1("",""));

        preguntas1a1Adapter.notifyDataSetChanged();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pregunta1a1.add(new Pregunta1a1("",""));
                preguntas1a1Adapter.notifyDataSetChanged();
            }
        });

        formula1a1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(name1a1.getText().toString().isEmpty())
            Toast.makeText(this, "No se ingresó nombre. Se utilizará un nombre default.", Toast.LENGTH_SHORT).show();
        SharedPreferences sp = getSharedPreferences("info", Context.MODE_PRIVATE);
        user = sp.getString("user", "");
        Toast.makeText(this, preguntas1a1Adapter.post(), Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("Formula");
        alerta.setMessage("¿Todo listo para guardarlo?");
        alerta.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Subir la información a la base de datos
                subirDatos();
                finish();
                dialog.dismiss();
            }
        });
        alerta.setNegativeButton("¡Espera!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog cuadro = alerta.create();
        cuadro.show();
    }

    private void subirDatos() {
        String url = "https://prototipoformula.fun/add_form.php";

        JSONObject postData = new JSONObject();
        try {
            postData.put("user", user);
            postData.put("nombre", (name1a1.getText().toString().isEmpty()?(type==1?"Relación de columnas por @"+user:"Memorama por @"+user):name1a1.getText().toString()));
            postData.put("tipo", type);
            postData.put("preguntas", preguntas1a1Adapter.post());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String ans = response.getString("ans");
                            if (ans.equals("ok")) {
                                Toast.makeText(Preg1a1.this, "Para ver el código de su formulario, entre a perfil y de click al mismo", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Preg1a1.this, "Error al crear el juego", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Preg1a1.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Preg1a1.this, "Error en la solicitud: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        rq.add(jsonObjectRequest);
    }
}