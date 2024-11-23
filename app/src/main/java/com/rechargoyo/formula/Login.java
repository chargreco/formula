package com.rechargoyo.formula;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity implements InputFilter{

    private EditText etuser,etname,etp1,etp2;
    private Boolean existe;
    private String user,pass,name;
    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user="";
        pass="";

        rq = Volley.newRequestQueue(this);

        etuser = findViewById(R.id.etuser);
        etname = findViewById(R.id.etusuario);
        etp1 = findViewById(R.id.etpass);
        etp2 = findViewById(R.id.etp2);

        etuser.setFilters(new InputFilter[] { new LowerCaseInputFilter(), new LengthFilter(15)});
        etname.setFilters(new InputFilter[]{ new LengthFilter(25)});
        etp1.setFilters(new InputFilter[] { new Login() , new LengthFilter(20)});
        etp2.setFilters(new InputFilter[] { new Login() , new LengthFilter(20)});
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // Verificar cada carácter ingresado
        for (int i = start; i < end; i++) {
            // Si el carácter es un espacio, retornar una cadena vacía para evitar que se ingrese
            if (Character.isWhitespace(source.charAt(i))) {
                return "";
            }
        }
        // Permitir el carácter si no es un espacio
        return null;
    }

    public void crear(View v){
        if(etp1.getText().toString().equals(etp2.getText().toString())){
            existe=false;
            String url="https://prototipoformula.fun/consultar.php?user="+etuser.getText().toString();
            //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
            JsonArrayRequest r = new JsonArrayRequest(Request.Method.GET,url,null,
                    response -> {
                        //Toast.makeText(this, String.valueOf(response.length()), Toast.LENGTH_SHORT).show();
                        if(response.length()==1){
                            existe=true;
                        }
                    },
                    error -> {
                        Toast.makeText(this, "El nombre de usuario ya esta en uso", Toast.LENGTH_SHORT).show();
                    });
            rq.add(r);

            if(!existe){
                url = "https://prototipoformula.fun/insert.php";
                JSONObject param = new JSONObject();
                try {
                    param.put("user",etuser.getText().toString());
                    param.put("name",etname.getText().toString());
                    param.put("pass",etp1.getText().toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                        url,
                        param,
                        response -> {
                            try {
                                if (response.get("ans").toString().equals("ok")){
                                    Toast.makeText(Login.this, "los datos se cargaron correctamente", Toast.LENGTH_SHORT).show();
                                    user = etuser.getText().toString();
                                    pass = etp1.getText().toString();
                                    name = etname.getText().toString();
                                    etname.setText("");
                                    etp1.setText("");
                                    etp2.setText("");
                                    etuser.setText("");
                                    Intent intent = new Intent();
                                    intent.putExtra("opcion",0);
                                    intent.putExtra("user",user);
                                    intent.putExtra("pass",pass);
                                    intent.putExtra("name",name);
                                    intent.putExtra("creac",0);
                                    setResult(Activity.RESULT_OK,intent);
                                    finish();

                                    // Agregar la notificación de bienvenida
                                    String welcomeMessage = "¡Bienvenido a Formula, " + name +
                                            "! Para crear un nuevo juego, ve a la sección con el signo +. Puedes buscar juegos en la sección home.";
                                    createNotification(user, welcomeMessage);
                                }
                                else{
                                    Toast.makeText(Login.this, "Error en el servidor", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }, error -> {
                            Intent intent = new Intent(this,NoConection.class);
                            startActivity(intent);
                        });
                rq.add(request);


                //Aqui es donde requiero el código ChatGPT
            } else{
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Las contraseñas son diferentes", Toast.LENGTH_SHORT).show();
            etp1.setText("");
            etp2.setText("");
        }



    }

    private void createNotification(String username, String message) {
        String url = "https://prototipoformula.fun/create_notification.php";
        JSONObject param = new JSONObject();
        try {
            param.put("user", username);
            param.put("message", message);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                param,
                response -> {
                    try {
                        if (response.get("ans").toString().equals("ok")) {
                            Toast.makeText(Login.this, "Notificación creada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, "Error al crear notificación", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, error -> {
            Toast.makeText(Login.this, "Error de conexión", Toast.LENGTH_SHORT).show();
        });
        rq.add(request);
    }


    public void end(String user, String pass){

    }

    public void iniciar(View v){
        Intent intent = new Intent();
        intent.putExtra("opcion",1);
        intent.putExtra("user","");
        intent.putExtra("pass","");
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}