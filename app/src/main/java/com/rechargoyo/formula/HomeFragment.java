package com.rechargoyo.formula;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento de la pantalla de inicio ("Home").
 * Muestra el cuadro de búsqueda para buscar juegos mediante su código hexadecimal
 * y despliega la lista de juegos recientemente jugados por el usuario desde el backend PHP/MySQL.
 *
 * @author Eduardo
 * @version 1.0
 */
public class HomeFragment extends Fragment {

    private RecyclerView rv;
    private RequestQueue rq;
    private TextView tvrecent;
    private String user, pass, name;
    private SearchView searchView;
    private int gameId;

    /**
     * Constructor público requerido.
     */
    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * Consulta el servidor para buscar un juego a partir de su código hexadecimal o identificador.
     *
     * @param query Cadena introducida en el buscador (formato código Hex o número).
     */
    private void searchGameById(String query) {
        try {
            gameId = 0;
            try {
                // Conversión de código hexadecimal inverso a ID decimal de base de datos
                gameId = 1000000 - Integer.parseInt(query, 16);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Error: La cadena no es un número hexadecimal válido.", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = Constants.BASE_URL + "get_game_by_id.php?id=" + gameId;

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    response -> {
                        try {
                            if (response.length() > 0) {
                                String nombreJuego = response.getString("nombreJuego");
                                int tipoJuego = response.getInt("tipoJuego");
                                String creadorJuego = response.getString("creadorJuego");

                                Intent intent = new Intent(requireContext(), info.class);
                                intent.putExtra("nombreJuego", nombreJuego);
                                intent.putExtra("tipoJuegoInt", tipoJuego);
                                intent.putExtra("creadorJuego", creadorJuego);
                                intent.putExtra("idJuego", gameId);
                                startActivity(intent);
                            } else {
                                Toast.makeText(requireContext(), "No se encontró dicho juego", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(requireContext(), "No se encontró dicho juego", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(requireContext(), "Error en la solicitud: " + error.toString(), Toast.LENGTH_SHORT).show()
            );

            rq.add(request);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Por favor ingrese un número válido", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = view.findViewById(R.id.homerecycler);
        rq = Volley.newRequestQueue(requireContext());
        tvrecent = view.findViewById(R.id.tvrecent);
        searchView = view.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchGameById(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Obtener la información del usuario autenticado guardada en SharedPreferences
        SharedPreferences sp = requireContext().getSharedPreferences("info", Context.MODE_PRIVATE);
        user = sp.getString("user", "");
        pass = sp.getString("pass", "");
        name = sp.getString("name", "");

        if (user.equals("")) {
            tvrecent.setText("Inicie sesión para ver los más recientes");
        } else {
            String url = Constants.BASE_URL + "recientes.php?user=" + user;

            List<Reciente> recientesList = new ArrayList<>();
            RecientesAdapter recientesAdapter = new RecientesAdapter(requireContext(), recientesList);

            rv.setLayoutManager(new LinearLayoutManager(requireContext()));
            rv.setAdapter(recientesAdapter);

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    response -> {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                int idReciente = obj.getInt("id_reciente");
                                String nombreJuego = obj.getString("nombre_juego");
                                int tipoJuego = obj.getInt("tipo_juego");
                                String creadorJuego = obj.getString("creador_juego");
                                String fechaReciente = obj.getString("fecha_reciente");
                                int dioLike = obj.getInt("dio_like");
                                int numeroLikes = obj.getInt("numero_likes");
                                int idJuego = obj.getInt("id_juego");

                                recientesList.add(new Reciente(
                                        idReciente,
                                        nombreJuego,
                                        tipoJuego,
                                        creadorJuego,
                                        fechaReciente,
                                        dioLike,
                                        numeroLikes,
                                        idJuego
                                ));
                            }
                            recientesAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e("HomeFragment", "Error analizando JSON: " + e.getMessage());
                        }
                    },
                    error -> {
                        Intent intent = new Intent(requireContext(), NoConection.class);
                        startActivity(intent);
                    }
            );

            rq.add(jsonArrayRequest);
        }
    }
}
