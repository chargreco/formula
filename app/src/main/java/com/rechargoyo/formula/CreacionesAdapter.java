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

public class CreacionesAdapter extends RecyclerView.Adapter<CreacionesAdapter.ViewHolder> {

    private boolean suma;
    private boolean click;

    private final List<Creaciones> creacionesList;
    private RequestQueue requestQueue;

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

        // Configurar el contenido de las vistas


        //Para que no se escriba un nombre tan largo
        String nombreAux = "";
        if (creaciones.getNombreJuego().length()>=35) {
            for (int i = 0; i < 35; i++){
                nombreAux+=creaciones.getNombreJuego().charAt(i);
            }
            holder.tvNombre.setText(nombreAux+"...");
        } else
            holder.tvNombre.setText(creaciones.getNombreJuego());


        // Asignar la imagen según el tipo de juego
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

        // Configurar el ícono de "me gusta" según si dio like
        updateLikeIcon(holder.imageLike, creaciones.getDioLike());

        holder.linearLayout.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "ID del juego: "+creaciones.getIdJuego(), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return creacionesList.size();
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
