package com.rechargoyo.formula;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.ViewHolder> {
    private List<Notificacion> notificaciones;
    private Context context;

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
        if(!notificacion.isVisto()) {
            holder.notificacionText.setTypeface(null, Typeface.BOLD);
        }
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificacionText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificacionText = itemView.findViewById(R.id.notificacionText);
        }
    }
}
