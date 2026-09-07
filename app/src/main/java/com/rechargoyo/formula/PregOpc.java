package com.rechargoyo.formula;

import android.content.Context;
import android.content.DialogInterface;
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

/**
 * Actividad encargada de la interfaz de creación para minijuegos con preguntas de opción múltiple
 * (Cuestionario Simple y Tres en Raya).
 *
 * @author Eduardo
 * @version 1.0
 */
public class PregOpc extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rv;
    private List<PreguntaOpc> preguntaOpc;
    private PreguntasOpcAdapter preguntasOpcAdapter;
    private Button add, formulaOpc;
    private EditText nameOpc;
    private int type;
    private String user;
    private RequestQueue requestQueue;

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

        requestQueue = Volley.newRequestQueue(this);

        Bundle datos = getIntent().getExtras();
        if (datos != null) {
            type = datos.getInt("type", 0);
        }

        rv = findViewById(R.id.rvp1a1);
        add = findViewById(R.id.addp1a1);
        nameOpc = findViewById(R.id.name1a1);
        formulaOpc = findViewById(R.id.formula1a1);

        if (type == 0) {
            nameOpc.setHint("Cuestionario Simple");
        } else if (type == 2) {
            nameOpc.setHint("Tres en raya");
        }

        preguntaOpc = new ArrayList<>();
        preguntasOpcAdapter = new PreguntasOpcAdapter(this, preguntaOpc);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(preguntasOpcAdapter);

        preguntaOpc.add(new PreguntaOpc("", "", "", "", ""));
        preguntasOpcAdapter.notifyDataSetChanged();

        add.setOnClickListener(v -> {
            preguntaOpc.add(new PreguntaOpc("", "", "", "", ""));
            preguntasOpcAdapter.notifyDataSetChanged();
        });

        formulaOpc.setOnClickListener(this);
    }

    /**
     * Muestra un cuadro de diálogo de confirmación para subir los datos del juego.
     *
     * @param v Vista del botón de guardado.
     */
    @Override
    public void onClick(View v) {
        SharedPreferences sp = getSharedPreferences("info", Context.MODE_PRIVATE);
        user = sp.getString("user", "");
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("Formula");
        alerta.setMessage("¿Todo listo para guardarlo?");
        alerta.setPositiveButton("Crear", (dialog, which) -> {
            subirDatos();
            finish();
            dialog.dismiss();
        });
        alerta.setNegativeButton("¡Espera!", (dialog, which) -> dialog.dismiss());
        AlertDialog cuadro = alerta.create();
        cuadro.show();
    }

    /**
     * Serializa y sube las preguntas de opción múltiple al servidor backend PHP.
     */
    private void subirDatos() {
        String url = Constants.BASE_URL + "add_form.php";

        JSONObject postData = new JSONObject();
        try {
            postData.put("user", user);
            postData.put("nombre", (nameOpc.getText().toString().isEmpty() ? (type == 0 ? "Cuestionario simple por @" + user : "Tres en raya por @" + user) : nameOpc.getText().toString()));
            postData.put("tipo", type);
            postData.put("preguntas", preguntasOpcAdapter.post());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                    response -> Toast.makeText(PregOpc.this, "Datos subidos correctamente", Toast.LENGTH_SHORT).show(),
                    error -> Toast.makeText(PregOpc.this, "Error al subir los datos", Toast.LENGTH_SHORT).show());

            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
