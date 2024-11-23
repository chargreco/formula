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

public class MemAdapter extends RecyclerView.Adapter<MemAdapter.ViewHolder> {

    private Context context;
    private List<Card> cards;
    private OnCardClickListener onCardClickListener;

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
            holder.cardText.setText(card.getText());
            holder.cardText.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        } else {
            holder.cardText.setText("");
            holder.cardText.setBackground(ContextCompat.getDrawable(context, R.drawable.app));
        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

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

    public interface OnCardClickListener {
        void onCardClick(int position);
    }
}
