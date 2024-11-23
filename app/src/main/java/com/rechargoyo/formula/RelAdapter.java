package com.rechargoyo.formula;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RelAdapter extends RecyclerView.Adapter<RelAdapter.RelViewHolder> {

    private final ArrayList<String> itemList;

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

    public void swapItems(int fromPosition, int toPosition) {
        String fromItem = itemList.get(fromPosition);
        itemList.set(fromPosition, itemList.get(toPosition));
        itemList.set(toPosition, fromItem);
        notifyItemMoved(fromPosition, toPosition);
    }

    static class RelViewHolder extends RecyclerView.ViewHolder {

        private final TextView button;

        public RelViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.relButton);
        }
    }
}