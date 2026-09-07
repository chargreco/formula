package com.rechargoyo.formula;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Actividad encargada del inicio de sesión (autenticación) del usuario.
 * Valida las credenciales ingresadas mediante el servicio PHP {@code userpass.php}.
 *
 * @author Eduardo
 * @version 1.0
 */
public class Singin extends AppCompatActivity {

    private RequestQueue rq;
    private EditText etuser, etpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_singin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etuser = findViewById(R.id.etusuario);
        etpass = findViewById(R.id.etpass);

        // Filtros de entrada para nombre de usuario y contraseña
        etuser.setFilters(new InputFilter[]{new LowerCaseInputFilter(), new InputFilter.LengthFilter(15)});
        etpass.setFilters(new InputFilter[]{new Login(), new InputFilter.LengthFilter(20)});

        rq = Volley.newRequestQueue(this);
    }

    /**
     * Navega hacia la pantalla de registro si el usuario no tiene cuenta.
     *
     * @param v Vista (botón) presionado.
     */
    public void crear(View v) {
        Intent intent = new Intent();
        intent.putExtra("opcion", 1);
        intent.putExtra("user", "");
        intent.putExtra("pass", "");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * Valida el nombre de usuario y contraseña con el backend.
     *
     * @param v Vista del botón de inicio de sesión.
     */
    public void iniciar(View v) {
        String url = Constants.BASE_URL + "userpass.php?user=" + etuser.getText().toString() + "&pass=" + etpass.getText().toString();

        JsonArrayRequest r = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    if (response.length() == 1) {
                        try {
                            JSONObject obj = new JSONObject(response.get(0).toString());
                            Intent intent = new Intent();
                            intent.putExtra("user", obj.getString("user"));
                            intent.putExtra("pass", etpass.getText().toString());
                            intent.putExtra("opcion", 0);
                            intent.putExtra("creac", obj.getInt("creaciones"));
                            intent.putExtra("name", obj.getString("nombre"));
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Intent intent = new Intent(this, NoConection.class);
                    startActivity(intent);
                });
        rq.add(r);
    }
}
