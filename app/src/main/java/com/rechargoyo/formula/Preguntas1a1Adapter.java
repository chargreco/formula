package com.rechargoyo.formula;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class Preguntas1a1Adapter extends RecyclerView.Adapter<Preguntas1a1Adapter.ViewHolder> {

    private final List<Pregunta1a1> pregunta1a1List;
    private RequestQueue requestQueue;
    private Context context;

    public Preguntas1a1Adapter(Context context, List<Pregunta1a1> pregunta1a1List) {
        this.pregunta1a1List = pregunta1a1List;
        this.requestQueue = Volley.newRequestQueue(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_p1a1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pregunta1a1 p1a1 = pregunta1a1List.get(position);

        holder.numPreg1a1.setText("Pregunta " + (position + 1));
        holder.preg1a1.setText(p1a1.getQust());
        holder.resp1a1.setText(p1a1.getAsw());

        holder.preg1a1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                p1a1.setQust(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        holder.resp1a1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                p1a1.setAsw(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        holder.exitp1a1.setOnClickListener(v -> {
            pregunta1a1List.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, pregunta1a1List.size());
        });
    }

    @Override
    public int getItemCount() {
        return pregunta1a1List.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView numPreg1a1;
        Button exitp1a1;
        EditText preg1a1;
        EditText resp1a1;

        ViewHolder(View view) {
            super(view);
            numPreg1a1 = view.findViewById(R.id.numPreg1a1);
            exitp1a1 = view.findViewById(R.id.exitp1a1);
            preg1a1 = view.findViewById(R.id.preg1a1);
            resp1a1 = view.findViewById(R.id.resp1a1);
        }
    }

    public String post() {
        String jsonform = "[";
        for (int i = 0; i < pregunta1a1List.size(); i++) {
            Pregunta1a1 p1a1 = pregunta1a1List.get(i);
            jsonform+=("{\"preg\":\"")+(p1a1.getQust())+("\",");
            jsonform+=("\"resp\":\"")+(p1a1.getAsw())+("\"}");
            if (i != pregunta1a1List.size() - 1) jsonform+=(",");
        }
        jsonform+="]";
        return jsonform;
    }

    public List<Pregunta1a1> getPregunta1a1() {
        return pregunta1a1List;
    }
}
