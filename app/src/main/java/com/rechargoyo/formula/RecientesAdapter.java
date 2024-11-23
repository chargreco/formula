package com.rechargoyo.formula;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class RecientesAdapter extends RecyclerView.Adapter<RecientesAdapter.ViewHolder> {

    private boolean suma;
    private boolean click;

    private final List<Reciente> recientesList;
    private RequestQueue requestQueue;

    public RecientesAdapter(Context context, List<Reciente> recientesList) {
        this.recientesList = recientesList;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recentsform, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reciente reciente = recientesList.get(position);
        suma = false;
        click = false;
        // Configurar el contenido de las vistas
        //Para que no se escriba un nombre tan largo
        String nombreAux = "";
        if (reciente.getNombreJuego().length()>=35) {
            for (int i = 0; i < 35; i++){
                nombreAux+=reciente.getNombreJuego().charAt(i);
            }
            holder.tvNombre.setText(nombreAux+"...");
        } else
            holder.tvNombre.setText(reciente.getNombreJuego());
        holder.tvCreador.setText("@"+reciente.getCreadorJuego());
        String formattedDate = DateUtils.formatDate(reciente.getFechaReciente());
        holder.tvDate.setText(formattedDate);
        // Asignar la imagen según el tipo de juego
        switch (reciente.getTipoJuego()) {
            case 0:
                holder.image.setImageResource(R.drawable.quest);
                holder.tvTipo.setText("Cuestionario Simple");
                break;
            case 1:
                holder.image.setImageResource(R.drawable.rel);
                holder.tvTipo.setText("Relacionar columnas");
                break;
            case 2:
                holder.image.setImageResource(R.drawable.tictac);
                holder.tvTipo.setText("Tres en raya");
                break;
            case 3:
                holder.image.setImageResource(R.drawable.mem);
                holder.tvTipo.setText("Memorama");
                break;
        }

        // Configurar el ícono de "me gusta" según si dio like
        updateLikeIcon(holder.imageLike, reciente.getDioLike());

        holder.linearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), info.class);

            // Pasar datos al Intent
            intent.putExtra("idJuego", reciente.getIdJuego());
            intent.putExtra("nombreJuego", reciente.getNombreJuego());
            intent.putExtra("tipoJuego", (reciente.getTipoJuego() == 0 ? "Cuestionario simple" : (reciente.getTipoJuego() == 1 ? "Relacionar columnas" : (reciente.getTipoJuego() == 2 ? "Tres en raya" : "Memorama"))));
            intent.putExtra("imagenJuego", getImageResource(reciente.getTipoJuego()));
            intent.putExtra("creadorJuego", "@"+reciente.getCreadorJuego());
            intent.putExtra("dioLike", reciente.getDioLike());
            intent.putExtra("tipoJuegoInt", reciente.getTipoJuego());

            // Iniciar la actividad
            v.getContext().startActivity(intent);
        });

        holder.imageLike.setOnClickListener(v -> {
            click = true;
            if(reciente.getDioLike() == 0){ suma = true; }
            else { suma = false; }
            updateTextViewLikes(holder);
            int recienteId = reciente.getIdReciente(); // Asumiendo que tienes el ID del reciente
            String user = "user"; // Cambiar por el valor adecuado
            String url = "https://prototipoformula.fun/like_toggle.php?user=" + user + "&reciente_id=" + recienteId;

            JsonObjectRequest jsonRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    response -> {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                int newDioLike = response.getInt("newDioLike");

                                // Actualizar el valor de diolike en el objeto Reciente
                                reciente.setDioLike(newDioLike);

                                /*if(newDioLike == 0){
                                    numLikes.setText(Integer.parseInt(numLikes.getText().toString())-1);
                                } else{
                                    numLikes.setText(Integer.parseInt(numLikes.getText().toString())+1);
                                }

                                 */
                                // Actualizar el ícono de "me gusta"
                                updateLikeIcon(holder.imageLike, newDioLike);
                            }
                        } catch (Exception e) {
                            Log.e("RecientesAdapter", "Error processing response: " + e.getMessage());
                        }
                    },
                    error -> {
                        Log.e("RecientesAdapter", "Error in Volley request: " + error.toString());
                    }
            );

            requestQueue.add(jsonRequest);
        });

        // Configurar el número de "likes"
        holder.tvLikes.setText(String.valueOf(reciente.getNumeroLikes()));
    }

    private int getImageResource(int tipoJuego) {
        switch (tipoJuego) {
            case 0: return R.drawable.quest;
            case 1: return R.drawable.rel;
            case 2: return R.drawable.tictac;
            case 3: return R.drawable.mem;
            default: return R.drawable.quest; // Default image
        }
    }

    @Override
    public int getItemCount() {
        return recientesList.size();
    }

    private void updateLikeIcon(ImageView imageLike, int dioLike) {
        if (dioLike == 0) {
            imageLike.setImageResource(R.drawable.baseline_favorite_border_24);
        } else {
            imageLike.setImageResource(R.drawable.baseline_favorite_24);
        }
    }

    private void updateTextViewLikes(ViewHolder holder){
        if(click){
            if(suma){
                holder.tvLikes.setText(String.valueOf(Integer.valueOf(holder.tvLikes.getText().toString())+1));
            }else{
                holder.tvLikes.setText(String.valueOf(Integer.valueOf(holder.tvLikes.getText().toString())-1));
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        TextView tvCreador;
        ImageView image;
        ImageView imageLike;
        TextView tvTipo;
        TextView tvLikes;
        TextView tvDate;
        LinearLayout linearLayout;

        ViewHolder(View view) {
            super(view);
            tvNombre = view.findViewById(R.id.tvnombre);
            tvCreador = view.findViewById(R.id.tvcreador);
            image = view.findViewById(R.id.image2);
            imageLike = view.findViewById(R.id.imageLike2);
            tvLikes = view.findViewById(R.id.tvlikes2);
            tvTipo = view.findViewById(R.id.tvtipo2);
            tvDate = view.findViewById(R.id.tvdate);
            linearLayout = view.findViewById(R.id.linearlayout1);
        }
    }
}
