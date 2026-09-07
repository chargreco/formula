package com.rechargoyo.formula;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Adaptador para el {@link RecyclerView} del juego Relacionar Columnas ({@link Rel}).
 * Muestra los elementos de cada columna y ofrece soporte para reordenar elementos dinámicamente mediante
 * arrastrar y soltar con {@link ItemTouchHelper}.
 *
 * @author Eduardo
 * @version 1.0
 */
public class RelAdapter extends RecyclerView.Adapter<RelAdapter.RelViewHolder> {

    /** Lista de textos de la columna. */
    private final ArrayList<String> itemList;

    /**
     * Construye un nuevo adaptador de columna para el juego de relacionar.
     *
     * @param itemList Lista de cadenas de texto con las opciones de la columna.
     */
    public RelAdapter(ArrayList<String> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rel_item, parent, false);
        return new RelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelViewHolder holder, int position) {
        String item = itemList.get(position);
        holder.button.setText(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * Intercambia la posición de dos elementos de la lista cuando el usuario arrastra un ítem.
     *
     * @param fromPosition Posición original del elemento arrastrado.
     * @param toPosition   Posición destino dentro de la columna.
     */
    public void swapItems(int fromPosition, int toPosition) {
        String fromItem = itemList.get(fromPosition);
        itemList.set(fromPosition, itemList.get(toPosition));
        itemList.set(toPosition, fromItem);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * ViewHolder que administra las vistas de los elementos en la columna.
     */
    static class RelViewHolder extends RecyclerView.ViewHolder {

        private final TextView button;

        public RelViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.relButton);
        }
    }
}
