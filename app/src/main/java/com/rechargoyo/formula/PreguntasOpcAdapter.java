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

public class PreguntasOpcAdapter extends RecyclerView.Adapter<PreguntasOpcAdapter.ViewHolder> {

    private final List<PreguntaOpc> preguntaOpcList;
    private RequestQueue requestQueue;
    private int pos = -1;

    public PreguntasOpcAdapter(Context context, List<PreguntaOpc> preguntaOpcList) {
        this.preguntaOpcList = preguntaOpcList;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_popc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int positionn = position;
        PreguntaOpc pOpc = preguntaOpcList.get(position);
        holder.numPregOpc.setText("Pregunta " + (position + 1));
        holder.pregOpc.setText(pOpc.getPregunta());
        holder.resp1Opc.setText(pOpc.getRespuesta1());
        holder.resp2Opc.setText(pOpc.getRespuesta2());
        holder.resp3Opc.setText(pOpc.getRespuesta3());
        holder.respCorrectOpc.setText(pOpc.getRespuestaCorrecta());

        holder.exitOpc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preguntaOpcList.remove(positionn);
                notifyDataSetChanged();
            }
        });

        holder.pregOpc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                pOpc.setPregunta(s.toString());
            }
        });

        holder.resp1Opc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                pOpc.setRespuesta1(s.toString());
            }
        });

        holder.resp2Opc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                pOpc.setRespuesta2(s.toString());
            }
        });

        holder.resp3Opc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                pOpc.setRespuesta3(s.toString());
            }
        });

        holder.respCorrectOpc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                pOpc.setRespuestaCorrecta(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return preguntaOpcList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView numPregOpc;
        Button exitOpc;
        EditText pregOpc;
        EditText resp1Opc;
        EditText resp2Opc;
        EditText resp3Opc;
        EditText respCorrectOpc;

        ViewHolder(View view) {
            super(view);
            numPregOpc = view.findViewById(R.id.numPregOpc);
            exitOpc = view.findViewById(R.id.exitpopc);
            pregOpc = view.findViewById(R.id.pregopc);
            resp1Opc = view.findViewById(R.id.resp1opc);
            resp2Opc = view.findViewById(R.id.resp2opc);
            resp3Opc = view.findViewById(R.id.resp3opc);
            respCorrectOpc = view.findViewById(R.id.respcorrectopc);
        }
    }

    public String post() {
        StringBuilder jsonform = new StringBuilder("[");
        for (int i = 0; i < preguntaOpcList.size(); i++) {
            PreguntaOpc p = preguntaOpcList.get(i);
            jsonform.append("{\"preg\":\"").append(p.getPregunta()).append("\",");
            jsonform.append("\"resp1\":\"").append(p.getRespuesta1()).append("\",");
            jsonform.append("\"resp2\":\"").append(p.getRespuesta2()).append("\",");
            jsonform.append("\"resp3\":\"").append(p.getRespuesta3()).append("\",");
            jsonform.append("\"respcorrect\":\"").append(p.getRespuestaCorrecta()).append("\"}");
            if (i != preguntaOpcList.size() - 1) jsonform.append(",");
        }
        jsonform.append("]");
        return jsonform.toString();
    }

    public List<PreguntaOpc> getPreguntaOpc() {
        return preguntaOpcList;
    }
}
