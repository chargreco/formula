package com.rechargoyo.formula;

import static com.rechargoyo.formula.R.drawable;
import static com.rechargoyo.formula.R.id;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
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

/**
 * Actividad contenedora principal de la aplicación.
 * Administra el menú de navegación inferior ({@link BottomNavigationView}), el botón flotante (+),
 * el panel deslizable lateral de notificaciones ({@link DrawerLayout}) y el intercambio de fragmentos.
 *
 * @author Eduardo
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private String user, user2;
    private String text;
    public RequestQueue rq;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private TextView nav_header_title;

    private RecyclerView recyclerView;
    private NotificacionesAdapter adapter;
    private List<Notificacion> notificaciones;
    private TextView noSessionMessage;
    private RequestQueue requestQueue;

    @SuppressLint({"NonConstantResourceId", "WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        requestQueue = Volley.newRequestQueue(this);
        SharedPreferences sp22 = getSharedPreferences("info", Context.MODE_PRIVATE);
        user2 = sp22.getString("user", "");

        recyclerView = findViewById(id.recycler_viewww);
        noSessionMessage = findViewById(id.no_session_message);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificaciones = new ArrayList<>();
        adapter = new NotificacionesAdapter(this, notificaciones);
        recyclerView.setAdapter(adapter);

        rq = Volley.newRequestQueue(this);

        bottomNavigationView = findViewById(id.bottomNavigationView);
        fab = findViewById(id.fab);
        drawerLayout = findViewById(id.drawer_layout);
        NavigationView navigationView = findViewById(id.nav_view);
        Toolbar toolbar = findViewById(id.toolbar);
        toolbar.setTitle("Notificaciones");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                marcarNotificacionesVistas();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {}

            @Override
            public void onDrawerStateChanged(int newState) {}
        });
        toggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(drawable.baseline_notifications_24);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(id.nav_view);
        }

        replaceFragment(new HomeFragment());

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == id.home) {
                replaceFragment(new HomeFragment());
                getAll();
            } else if (itemId == id.formula) {
                SharedPreferences sp = getSharedPreferences("info", Context.MODE_PRIVATE);
                user = sp.getString("user", "");
                if (user.equals("")) {
                    Intent intent = new Intent(this, Login.class);
                    startActivityForResult(intent, 100);
                } else {
                    replaceFragment(new FormulaFragment());
                }
                getAll();
            } else if (itemId == id.perfil) {
                SharedPreferences sp = getSharedPreferences("info", Context.MODE_PRIVATE);
                user = sp.getString("user", "");
                if (user.equals("")) {
                    Intent intent = new Intent(this, Login.class);
                    startActivityForResult(intent, 100);
                } else {
                    replaceFragment(new PerfilFragment());
                }
                getAll();
            }
            return true;
        });

        fab.setOnClickListener(view -> {
            SharedPreferences sp = getSharedPreferences("info", Context.MODE_PRIVATE);
            user = sp.getString("user", "");
            if (user.equals("")) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivityForResult(intent, 100);
            } else {
                bottomNavigationView.setSelectedItemId(id.formula);
                replaceFragment(new FormulaFragment());
            }
            getAll();
        });
    }

    /**
     * Envía una petición GET al servidor para marcar todas las notificaciones del usuario como leídas/vistas.
     */
    private void marcarNotificacionesVistas() {
        String url = Constants.BASE_URL + "marcar_notificaciones_vistas.php?user=" + user2;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {}, error -> {});
        requestQueue.add(stringRequest);
    }

    /**
     * Carga el listado de notificaciones asociadas al usuario desde el servidor.
     *
     * @param user Nombre del usuario autenticado.
     */
    private void loadNotificaciones(String user) {
        user2 = user;
        notificaciones.clear();
        adapter.notifyDataSetChanged();
        String url = Constants.BASE_URL + "get_notificaciones.php?username=" + user;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getString("ans").equals("ok")) {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject notificacionObj = data.getJSONObject(i);
                                String notificacion = notificacionObj.getString("notificacion");
                                int visto = notificacionObj.getInt("visto");
                                Notificacion noti = new Notificacion(notificacion, visto);
                                notificaciones.add(noti);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error al cargar las notificaciones", Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(MainActivity.this, "Error al cargar las notificaciones", Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    /**
     * Obtiene notificaciones detalladas vía método POST.
     *
     * @param user Nombre de usuario.
     */
    private void getNotifications(String user) {
        String url = Constants.BASE_URL + "get_notificaciones.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        text = "";

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestObject,
                response -> {
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
                }, error -> {
            error.printStackTrace();
            Toast.makeText(MainActivity.this, "Error al obtener las notificaciones", Toast.LENGTH_SHORT).show();
        });
        queue.add(jsonObjectRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                Bundle datos = data.getExtras();
                if (datos.getInt("opcion") == 0) {
                    SharedPreferences sp = getSharedPreferences("info", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    String us = datos.getString("user");
                    editor.putString("user", us);
                    editor.putString("pass", datos.getString("pass"));
                    editor.putString("name", datos.getString("name"));
                    editor.putInt("creac", datos.getInt("creac"));
                    editor.apply();
                    replaceFragment(new PerfilFragment());
                } else if (datos.getInt("opcion") == 1) {
                    Intent intent = new Intent(this, Singin.class);
                    startActivityForResult(intent, 200);
                }
            }
        } else if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                Bundle datos = data.getExtras();
                if (datos.getInt("opcion") == 1) {
                    Intent intent = new Intent(this, Login.class);
                    startActivityForResult(intent, 100);
                } else if (datos.getInt("opcion") == 0) {
                    SharedPreferences sp = getSharedPreferences("info", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    String us = datos.getString("user");
                    editor.putString("user", us);
                    editor.putString("pass", datos.getString("pass"));
                    editor.putString("name", datos.getString("name"));
                    editor.putInt("creac", datos.getInt("creac"));
                    editor.apply();
                    replaceFragment(new PerfilFragment());
                }
            }
        }
    }

    /**
     * Reemplaza el fragmento visible actual en el contenedor por uno nuevo.
     *
     * @param fragment Instancia del fragmento a desplegar.
     */
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
        fragmentTransaction.replace(id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAll();
    }

    /**
     * Verifica el estado de conectividad al backend mediante una petición GET a conect.php.
     * En caso de error de red, abre la actividad {@link NoConection}.
     */
    public void getAll() {
        String url = Constants.BASE_URL + "conect.php";
        JsonArrayRequest r = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {},
                error -> {
                    Intent intent = new Intent(this, NoConection.class);
                    startActivity(intent);
                });
        rq.add(r);
    }

    /**
     * Cierra la sesión activa borrando los datos del usuario en SharedPreferences.
     *
     * @param v Vista del botón de cierre de sesión.
     */
    public void logout(View v) {
        bottomNavigationView.setSelectedItemId(id.home);
        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("user");
        editor.remove("pass");
        editor.remove("creac");
        editor.remove("name");
        editor.apply();
        replaceFragment(new HomeFragment());
    }
}
