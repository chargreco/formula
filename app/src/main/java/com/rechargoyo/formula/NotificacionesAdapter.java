package com.rechargoyo.formula;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adaptador para el {@link RecyclerView} que despliega el listado de notificaciones dentro del menú lateral (Drawer).
 * Destaca en negrita las notificaciones que no han sido vistas.
 *
 * @author Eduardo
 * @version 1.0
 */
public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.ViewHolder> {
    
    /** Lista de notificaciones del usuario. */
    private List<Notificacion> notificaciones;

    /** Contexto de la interfaz de usuario. */
    private Context context;

    /**
     * Construye un nuevo adaptador de notificaciones.
     *
     * @param context        Contexto de la actividad o fragmento.
     * @param notificaciones Lista de notificaciones {@link Notificacion}.
     */
    public NotificacionesAdapter(Context context, List<Notificacion> notificaciones) {
        this.context = context;
        this.notificaciones = notificaciones;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notificacion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notificacion notificacion = notificaciones.get(position);
        holder.notificacionText.setText(notificacion.getNotificacion());
        if (!notificacion.isVisto()) {
            // Aplicar formato de texto en negrita para mensajes no leídos
            holder.notificacionText.setTypeface(null, Typeface.BOLD);
        }
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    /**
     * Contenedor de vistas para el ítem de notificación.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificacionText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificacionText = itemView.findViewById(R.id.notificacionText);
        }
    }
}
