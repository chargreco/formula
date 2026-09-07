package com.rechargoyo.formula;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Fragmento que despliega el Perfil del Usuario.
 * Muestra el nombre de usuario, nombre completo, contador de creaciones
 * y el listado de juegos diseñados por el usuario cargados desde la base de datos.
 *
 * @author Eduardo
 * @version 1.0
 */
public class PerfilFragment extends Fragment {

    public ArrayList<String> nombreJuego;
    public RequestQueue rq;

    private TextView proftvuser, proftvname, proftvc;
    public ArrayList<String> tipoJuego;
    public ArrayList<Integer> imagenJuego;
    public ArrayList<Integer> idJuego;
    private RecyclerView rv;
    private String user, name;
    private int id;

    /**
     * Constructor público requerido sin parámetros.
     */
    public PerfilFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tipoJuego = new ArrayList<>();
        imagenJuego = new ArrayList<>();
        idJuego = new ArrayList<>();
        nombreJuego = new ArrayList<>();

        proftvc = view.findViewById(R.id.proftvc);

        SharedPreferences sp = requireContext().getSharedPreferences("info", Context.MODE_PRIVATE);
        rq = Volley.newRequestQueue(requireContext());
        user = sp.getString("user", "");
        name = sp.getString("name", "");
        id = sp.getInt("id", 0);

        String url = Constants.BASE_URL + "consultar.php?user=" + user;
        JsonArrayRequest r1 = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.length() == 1) {
                            JSONObject obj = new JSONObject(response.get(0).toString());
                            proftvc.setText("Creaciones (" + obj.getString("creaciones") + ")");
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show());

        rq.add(r1);

        rv = view.findViewById(R.id.profrecycler);
        proftvuser = view.findViewById(R.id.profuser);
        proftvname = view.findViewById(R.id.profname);

        proftvuser.setText("@" + user);
        proftvname.setText(name);

        url = Constants.BASE_URL + "juegosporuser.php?user=" + user;

        JsonArrayRequest r = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = new JSONObject(response.get(i).toString());
                            nombreJuego.add(obj.getString("nombre"));
                            int tipoo = obj.getInt("tipo");
                            tipoJuego.add((tipoo == 0 ? "Cuestionario simple" : (tipoo == 1 ? "Relacionar columnas" : (tipoo == 2 ? "Tres en raya" : "Memorama"))));
                            imagenJuego.add((tipoo == 0 ? R.drawable.quest : (tipoo == 1 ? R.drawable.rel : (tipoo == 2 ? R.drawable.tictac : R.drawable.mem))));
                            idJuego.add(obj.getInt("id"));
                        }

                        AdaptadorForms adapter = new AdaptadorForms(nombreJuego, tipoJuego, imagenJuego, idJuego);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
                        rv.setLayoutManager(layoutManager);
                        rv.setAdapter(adapter);

                    } catch (JSONException e) {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Intent intent = new Intent(requireContext(), NoConection.class);
                    startActivity(intent);
                });

        rq.add(r);
    }

    /**
     * Adaptador interno para desplegar los juegos creados por el usuario en el perfil.
     */
    public class AdaptadorForms extends RecyclerView.Adapter<AdaptadorForms.AdaptadorFormsHolder> {
        private ArrayList<String> nombreJuego;
        private ArrayList<String> tipoJuego;
        private ArrayList<Integer> imagenJuego;
        private ArrayList<Integer> idJuego;

        public AdaptadorForms(ArrayList<String> nombreJuego, ArrayList<String> tipoJuego, ArrayList<Integer> imagenJuego, ArrayList<Integer> idJuego) {
            this.nombreJuego = nombreJuego;
            this.tipoJuego = tipoJuego;
            this.imagenJuego = imagenJuego;
            this.idJuego = idJuego;
        }

        @NonNull
        @Override
        public AdaptadorFormsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemform, parent, false);
            return new AdaptadorFormsHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorFormsHolder holder, int position) {
            holder.imprimir(position);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), info.class);
                intent.putExtra("idJuego", idJuego.get(position));
                intent.putExtra("nombreJuego", nombreJuego.get(position));
                intent.putExtra("tipoJuego", tipoJuego.get(position));
                intent.putExtra("imagenJuego", imagenJuego.get(position));
                intent.putExtra("creadorJuego", proftvuser.getText().toString());
                intent.putExtra("tipoJuegoInt", (tipoJuego.get(position).equals("Cuestionario simple") ? 0 : (tipoJuego.get(position).equals("Relacionar columnas") ? 1 : (tipoJuego.get(position).equals("Tres en raya") ? 2 : 3))));
                intent.putExtra("dioLike", 0);
                v.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return nombreJuego.size();
        }

        public class AdaptadorFormsHolder extends RecyclerView.ViewHolder {
            ImageView iv1;
            TextView tv1;
            TextView tv2;

            public AdaptadorFormsHolder(@NonNull View itemView) {
                super(itemView);
                iv1 = itemView.findViewById(R.id.image2);
                tv1 = itemView.findViewById(R.id.tvnombre);
                tv2 = itemView.findViewById(R.id.tvtipo2);
            }

            public void imprimir(int position) {
                iv1.setImageResource(imagenJuego.get(position));
                tv1.setText(nombreJuego.get(position));
                tv2.setText(tipoJuego.get(position));
            }
        }
    }
}
