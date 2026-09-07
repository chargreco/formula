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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

/**
 * Adaptador para el {@link RecyclerView} del feed de juegos recientes ({@link HomeFragment}).
 * Gestiona el despliegue del título, creador, tipo de juego, fecha formateada y la acción de alternar "me gusta" (like).
 *
 * @author Eduardo
 * @version 1.0
 */
public class RecientesAdapter extends RecyclerView.Adapter<RecientesAdapter.ViewHolder> {

    private boolean suma;
    private boolean click;

    /** Lista de objetos {@link Reciente} recibidos de la API. */
    private final List<Reciente> recientesList;

    /** Cola de peticiones HTTP Volley para ejecutar la acción de like. */
    private RequestQueue requestQueue;

    /**
     * Construye un nuevo adaptador para la lista de juegos recientes.
     *
     * @param context       Contexto de la aplicación.
     * @param recientesList Lista de partidas recientes.
     */
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

        // Truncar el nombre si excede los 35 caracteres para evitar desbordamiento
        String nombreAux = "";
        if (reciente.getNombreJuego().length() >= 35) {
            for (int i = 0; i < 35; i++) {
                nombreAux += reciente.getNombreJuego().charAt(i);
            }
            holder.tvNombre.setText(nombreAux + "...");
        } else {
            holder.tvNombre.setText(reciente.getNombreJuego());
        }

        holder.tvCreador.setText("@" + reciente.getCreadorJuego());
        String formattedDate = DateUtils.formatDate(reciente.getFechaReciente());
        holder.tvDate.setText(formattedDate);

        // Asignar imagen e instructivo según el tipo de minijuego
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

        // Configurar ícono de corazón
        updateLikeIcon(holder.imageLike, reciente.getDioLike());

        // Evento de clic en la tarjeta completa para abrir la actividad info
        holder.linearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), info.class);
            intent.putExtra("idJuego", reciente.getIdJuego());
            intent.putExtra("nombreJuego", reciente.getNombreJuego());
            intent.putExtra("tipoJuego", (reciente.getTipoJuego() == 0 ? "Cuestionario simple" : (reciente.getTipoJuego() == 1 ? "Relacionar columnas" : (reciente.getTipoJuego() == 2 ? "Tres en raya" : "Memorama"))));
            intent.putExtra("imagenJuego", getImageResource(reciente.getTipoJuego()));
            intent.putExtra("creadorJuego", "@" + reciente.getCreadorJuego());
            intent.putExtra("dioLike", reciente.getDioLike());
            intent.putExtra("tipoJuegoInt", reciente.getTipoJuego());
            v.getContext().startActivity(intent);
        });

        // Evento de clic en el ícono de corazón (Like)
        holder.imageLike.setOnClickListener(v -> {
            click = true;
            suma = (reciente.getDioLike() == 0);
            updateTextViewLikes(holder);

            int recienteId = reciente.getIdReciente();
            String user = "user";
            String url = Constants.BASE_URL + "like_toggle.php?user=" + user + "&reciente_id=" + recienteId;

            JsonObjectRequest jsonRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    response -> {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                int newDioLike = response.getInt("newDioLike");
                                reciente.setDioLike(newDioLike);
                                updateLikeIcon(holder.imageLike, newDioLike);
                            }
                        } catch (Exception e) {
                            Log.e("RecientesAdapter", "Error al procesar respuesta de like: " + e.getMessage());
                        }
                    },
                    error -> Log.e("RecientesAdapter", "Error en solicitud Volley: " + error.toString())
            );

            requestQueue.add(jsonRequest);
        });

        // Mostrar el total de likes acumulados
        holder.tvLikes.setText(String.valueOf(reciente.getNumeroLikes()));
    }

    /**
     * Obtiene el recurso de imagen estática (Drawable) asignado al tipo de juego.
     *
     * @param tipoJuego Identificador numérico del tipo de juego (0 a 3).
     * @return ID del recurso Drawable correspondiente.
     */
    private int getImageResource(int tipoJuego) {
        switch (tipoJuego) {
            case 0: return R.drawable.quest;
            case 1: return R.drawable.rel;
            case 2: return R.drawable.tictac;
            case 3: return R.drawable.mem;
            default: return R.drawable.quest;
        }
    }

    @Override
    public int getItemCount() {
        return recientesList.size();
    }

    /**
     * Actualiza la imagen del corazón según si el usuario le dio "me gusta".
     *
     * @param imageLike Vista ImageView del corazón.
     * @param dioLike   1 si le dio like, 0 en caso contrario.
     */
    private void updateLikeIcon(ImageView imageLike, int dioLike) {
        if (dioLike == 0) {
            imageLike.setImageResource(R.drawable.baseline_favorite_border_24);
        } else {
            imageLike.setImageResource(R.drawable.baseline_favorite_24);
        }
    }

    /**
     * Incrementa o decrementa temporalmente el texto visual de likes en la vista mientras responde el servidor.
     *
     * @param holder Contenedor de las vistas del ítem.
     */
    private void updateTextViewLikes(ViewHolder holder) {
        if (click) {
            if (suma) {
                holder.tvLikes.setText(String.valueOf(Integer.parseInt(holder.tvLikes.getText().toString()) + 1));
            } else {
                holder.tvLikes.setText(String.valueOf(Integer.parseInt(holder.tvLikes.getText().toString()) - 1));
            }
        }
    }

    /**
     * ViewHolder que conserva las referencias a las vistas de cada tarjeta de reciente.
     */
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
