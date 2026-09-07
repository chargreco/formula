package com.rechargoyo.formula;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adaptador para el {@link RecyclerView} del juego de Memorama ({@link Mem}).
 * Gestiona el reverso y anverso de las tarjetas en la cuadrícula de juego.
 *
 * @author Eduardo
 * @version 1.0
 */
public class MemAdapter extends RecyclerView.Adapter<MemAdapter.ViewHolder> {

    /** Contexto de la interfaz de usuario. */
    private Context context;

    /** Lista de tarjetas {@link Card} que componen el juego. */
    private List<Card> cards;

    /** Escuchador para eventos de selección/clic sobre las tarjetas. */
    private OnCardClickListener onCardClickListener;

    /**
     * Construye un adaptador para el tablero de Memorama.
     *
     * @param context             Contexto de la actividad.
     * @param cards               Lista de tarjetas de tipo {@link Card}.
     * @param onCardClickListener Listener para gestionar la interacción de volteo.
     */
    public MemAdapter(Context context, List<Card> cards, OnCardClickListener onCardClickListener) {
        this.context = context;
        this.cards = cards;
        this.onCardClickListener = onCardClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view, onCardClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = cards.get(position);
        if (card.isRevealed()) {
            // Mostrar el texto de la tarjeta si está revelada
            holder.cardText.setText(card.getText());
            holder.cardText.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        } else {
            // Mostrar la cubierta o reverso de la tarjeta
            holder.cardText.setText("");
            holder.cardText.setBackground(ContextCompat.getDrawable(context, R.drawable.app));
        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    /**
     * ViewHolder que mantiene la referencia a la vista de la tarjeta e intercepta clics.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView cardText;
        OnCardClickListener onCardClickListener;

        public ViewHolder(@NonNull View itemView, OnCardClickListener onCardClickListener) {
            super(itemView);
            cardText = itemView.findViewById(R.id.card_text);
            this.onCardClickListener = onCardClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCardClickListener.onCardClick(getAdapterPosition());
        }
    }

    /**
     * Interfaz de callback para notificar cuando una tarjeta es seleccionada por el usuario.
     */
    public interface OnCardClickListener {
        /**
         * Método invocado cuando el usuario toca una tarjeta en la posición especificada.
         *
         * @param position Índice de la tarjeta tocada.
         */
        void onCardClick(int position);
    }
}
