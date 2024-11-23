package com.rechargoyo.formula;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static com.rechargoyo.formula.R.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;

    String user,user2;
    String text;
    public RequestQueue rq;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    private TextView nav_header_title;

    //----------
    private RecyclerView recyclerView;
    private NotificacionesAdapter adapter;
    private List<Notificacion> notificaciones;
    private TextView noSessionMessage;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;

    @SuppressLint({"NonConstantResourceId", "WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        requestQueue = Volley.newRequestQueue(this);
        //nav_header_title = findViewById(R.id.nav_header_title);
        SharedPreferences sp22 = getSharedPreferences("info", Context.MODE_PRIVATE);
        user2 = sp22.getString("user", "");


        recyclerView = findViewById(R.id.recycler_viewww);
        noSessionMessage = findViewById(R.id.no_session_message);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificaciones = new ArrayList<>();
        adapter = new NotificacionesAdapter(this, notificaciones);
        recyclerView.setAdapter(adapter);

//        if (user2.isEmpty()) {
//            nav_header_title.setText("Necesitas iniciar sesión");
//        } else {
//            getNotifications(user2);
//        }

        rq = Volley.newRequestQueue(this);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Notificaciones");
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                marcarNotificacionesVistas();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        toggle.syncState();

        // Cambiar el icono del botón del drawer toggle
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(drawable.baseline_notifications_24); // Usa tu icono personalizado aquí
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Asegurarse de manejar los eventos de clic en el icono de notificaciones
        toolbar.setNavigationOnClickListener(view -> {
            // Aquí puedes manejar el evento clic, por ejemplo abrir el drawer o algo relacionado con notificaciones
            drawerLayout.openDrawer(GravityCompat.START); // O alguna otra lógica
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_view);
        }

        replaceFragment(new HomeFragment());


        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
                getAll();
            } else if(id == R.id.formula){
                SharedPreferences sp = getSharedPreferences("info", Context.MODE_PRIVATE);
                user = sp.getString("user","");
                if(user.equals("")){
                    Intent intent = new Intent(this,Login.class);
                    startActivityForResult(intent,100);
                }else{
                    replaceFragment(new FormulaFragment());
                }
                getAll();
            } else if (id == R.id.perfil) {
                SharedPreferences sp = getSharedPreferences("info", Context.MODE_PRIVATE);
                user = sp.getString("user","");
                if(user.equals("")){
                    Intent intent = new Intent(this,Login.class);
                    startActivityForResult(intent,100);
                }else{
                    replaceFragment(new PerfilFragment());
                }
                getAll();
            }

            return true;
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("info", Context.MODE_PRIVATE);
                user = sp.getString("user","");
                if(user.equals("")){
                    Intent intent = new Intent(MainActivity.this,Login.class);
                    startActivityForResult(intent,100);
                }else{
                    bottomNavigationView.setSelectedItemId(id.formula);
                    replaceFragment(new FormulaFragment());
                }
                getAll();
            }
        });
    }

    private void marcarNotificacionesVistas() {
        String url = "https://prototipoformula.fun/marcar_notificaciones_vistas.php?user=" + user2;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Manejar la respuesta del servidor
                },
                error -> {
                    // Manejar el error
                });

        requestQueue.add(stringRequest);
    }

    private void loadNotificaciones(String user) {

        user2 = user;
        notificaciones.clear();
        adapter.notifyDataSetChanged();
        String url = "https://prototipoformula.fun/get_notificaciones.php?username=" + user;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getString("ans").equals("ok")) {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject notificacionObj = data.getJSONObject(i);
                                String notificacion = notificacionObj.getString("notificacion");
                                int visto = notificacionObj.getInt("visto");
                                Notificacion noti = new Notificacion(notificacion,visto);
                                notificaciones.add(noti);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error al cargar las notificaciones", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            Toast.makeText(MainActivity.this, "Error al cargar las notificaciones", Toast.LENGTH_SHORT).show();
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


    private void getNotifications(String user) {
        String url = "https://prototipoformula.fun/get_notificaciones.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        text = "";

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray notifications = response.getJSONArray("notificaciones");
                            StringBuilder notificationsText = new StringBuilder();
                            for (int i = 0; i < notifications.length(); i++) {
                                notificationsText.append(notifications.getString(i)).append("\n");
                            }
                            text = notificationsText.toString();
                            nav_header_title.setText(text);
                            Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error al obtener las notificaciones", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Error al obtener las notificaciones", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if(resultCode==Activity.RESULT_OK){
                Bundle datos = data.getExtras();
                if(datos.getInt("opcion")==0){

                    SharedPreferences sp = getSharedPreferences("info",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    String us = datos.getString("user");
                    editor.putString("user",us);
                    editor.putString("pass",datos.getString("pass"));
                    editor.putString("name",datos.getString("name"));
                    editor.putInt("creac",datos.getInt("creac"));
                    editor.commit();
                    replaceFragment(new PerfilFragment());

                } else if(datos.getInt("opcion")==1){
                    Intent intent = new Intent(this, Singin.class);
                    startActivityForResult(intent,200);
                } else{
                    Toast.makeText(this, "Error en el getInt", Toast.LENGTH_SHORT).show();
                }
            }
        } else if(requestCode==200){
            if(resultCode==Activity.RESULT_OK){
                Bundle datos = data.getExtras();
                if(datos.getInt("opcion")==1){
                    Intent intent = new Intent(this, Login.class);
                    startActivityForResult(intent,100);
                } else if(datos.getInt("opcion")==0){
                    SharedPreferences sp = getSharedPreferences("info",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    String us = datos.getString("user");
                    editor.putString("user",us);
                    editor.putString("pass",datos.getString("pass"));
                    editor.putString("name",datos.getString("name"));
                    editor.putInt("creac",datos.getInt("creac"));
                    editor.commit();
                    replaceFragment(new PerfilFragment());
                }
            }
        }
    }

    public void replaceFragment(Fragment fragment) {

        SharedPreferences sp2 = getSharedPreferences("info", Context.MODE_PRIVATE);
        String user2 = sp2.getString("user", "");

        if (user2.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noSessionMessage.setText("Necesitas iniciar sesión");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noSessionMessage.setText("");
            loadNotificaciones(user2);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }



    @Override
    protected void onStart() {
        super.onStart();
        getAll();
    }

    public void getAll(){

        String url="https://prototipoformula.fun/conect.php";
        JsonArrayRequest r = new JsonArrayRequest(Request.Method.GET,url,null,
                response -> {
                },
                error -> {
                    Intent intent = new Intent(this,NoConection.class);
                    startActivity(intent);
                });
        rq.add(r);


    }


    public void logout(View v){
        bottomNavigationView.setSelectedItemId(R.id.home);
        SharedPreferences sp = getSharedPreferences("info",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("user");
        editor.remove("pass");
        editor.remove("creac");
        editor.remove("name");
        editor.commit();
        replaceFragment(new HomeFragment());
    }

}