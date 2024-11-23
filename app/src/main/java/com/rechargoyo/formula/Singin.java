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

public class Singin extends AppCompatActivity {

    private RequestQueue rq;
    private EditText etuser,etpass;

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

        etuser=findViewById(R.id.etusuario);
        etpass=findViewById(R.id.etpass);

        etuser.setFilters(new InputFilter[] { new LowerCaseInputFilter(), new InputFilter.LengthFilter(15)});
        etpass.setFilters(new InputFilter[] { new Login() , new InputFilter.LengthFilter(20)});

        rq= Volley.newRequestQueue(this);
    }

    public void crear(View v){
        Intent intent = new Intent();
        intent.putExtra("opcion",1);
        intent.putExtra("user","");
        intent.putExtra("pass","");
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    public void iniciar(View v){
        //Toast.makeText(this, "uno", Toast.LENGTH_SHORT).show();
        String url="https://prototipoformula.fun/userpass.php?user="+etuser.getText().toString()+"&pass="+etpass.getText().toString();
        //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        JsonArrayRequest r = new JsonArrayRequest(Request.Method.GET,url,null,
                response -> {

                    //Toast.makeText(this, String.valueOf(response.length()), Toast.LENGTH_SHORT).show();
                    if(response.length()==1){
                        try{

                            JSONObject obj = new JSONObject(response.get(0).toString());
                            Intent intent = new Intent();
                            intent.putExtra("user",obj.getString("user"));
                            intent.putExtra("pass",etpass.getText().toString());
                            intent.putExtra("opcion",0);
                            intent.putExtra("creac",obj.getInt("creaciones"));
                            intent.putExtra("name",obj.getString("nombre"));
                            setResult(Activity.RESULT_OK,intent);
                            finish();
                        }catch(JSONException e){
                        }
                    }else{
                            Toast.makeText(this, "Usuario o contraseña incorrectos, "+response.length(), Toast.LENGTH_SHORT).show();

                    }
                },
                error -> {
                    Intent intent = new Intent(this,NoConection.class);
                    startActivity(intent);
                });
        rq.add(r);

    }
}