package com.rechargoyo.formula;

import android.content.Context;
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

/**
 * Adaptador para el {@link RecyclerView} encargado de mostrar la lista de creaciones creadas por el usuario.
 * Asigna los íconos de tipo de juego y los contadores de interacción.
 *
 * @author Eduardo
 * @version 1.0
 */
public class CreacionesAdapter extends RecyclerView.Adapter<CreacionesAdapter.ViewHolder> {

    private boolean suma;
    private boolean click;

    /** Lista de creaciones del usuario a mostrar en la interfaz. */
    private final List<Creaciones> creacionesList;

    /** Cola de peticiones de Volley para interacciones de red. */
    private RequestQueue requestQueue;

    /**
     * Construye un nuevo adaptador para la lista de creaciones.
     *
     * @param context        Contexto de la aplicación o actividad.
     * @param creacionesList Lista de objetos {@link Creaciones}.
     */
    public CreacionesAdapter(Context context, List<Creaciones> creacionesList) {
        this.creacionesList = creacionesList;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemform, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Creaciones creaciones = creacionesList.get(position);

        suma = false;
        click = false;

        // Limitar la longitud del nombre para evitar desbordamiento visual
        String nombreAux = "";
        if (creaciones.getNombreJuego().length() >= 35) {
            for (int i = 0; i < 35; i++) {
                nombreAux += creaciones.getNombreJuego().charAt(i);
            }
            holder.tvNombre.setText(nombreAux + "...");
        } else {
            holder.tvNombre.setText(creaciones.getNombreJuego());
        }

        // Asignar la imagen correspondiente según el tipo de juego
        switch (creaciones.getTipoJuego()) {
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

        // Configurar el ícono de "me gusta"
        updateLikeIcon(holder.imageLike, creaciones.getDioLike());

        holder.linearLayout.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "ID del juego: " + creaciones.getIdJuego(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return creacionesList.size();
    }

    /**
     * Actualiza el ícono de corazón dependiendo de si el usuario le dio "me gusta".
     *
     * @param imageLike ImageView del ícono de corazón.
     * @param dioLike   1 si dio like, 0 en caso contrario.
     */
    private void updateLikeIcon(ImageView imageLike, int dioLike) {
        if (dioLike == 0) {
            imageLike.setImageResource(R.drawable.baseline_favorite_border_24);
        } else {
            imageLike.setImageResource(R.drawable.baseline_favorite_24);
        }
    }

    /**
     * Actualiza el contador de likes mostrado en la vista tras una interacción del usuario.
     *
     * @param holder ViewHolder de la celda modificada.
     */
    private void updateTextViewLikes(ViewHolder holder) {
        if (click) {
            if (suma) {
                holder.tvLikes.setText(String.valueOf(Integer.valueOf(holder.tvLikes.getText().toString()) + 1));
            } else {
                holder.tvLikes.setText(String.valueOf(Integer.valueOf(holder.tvLikes.getText().toString()) - 1));
            }
        }
    }

    /**
     * ViewHolder que mantiene las referencias a las vistas de cada elemento de la lista.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        ImageView image;
        ImageView imageLike;
        TextView tvTipo;
        TextView tvLikes;
        LinearLayout linearLayout;

        ViewHolder(View view) {
            super(view);
            tvNombre = view.findViewById(R.id.tvnombre);
            image = view.findViewById(R.id.image2);
            tvTipo = view.findViewById(R.id.tvtipo2);
            linearLayout = view.findViewById(R.id.linearlayout2);
        }
    }
}
