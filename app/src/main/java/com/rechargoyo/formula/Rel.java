package com.rechargoyo.formula;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Rel extends AppCompatActivity {

    private RecyclerView leftColumnRecyclerView;
    private RecyclerView rightColumnRecyclerView;
    private RelAdapter leftColumnAdapter;
    private RelAdapter rightColumnAdapter;
    private ArrayList<String> questionsList, questionsMap;
    private ArrayList<String> answersList,correctAnswersMap;
    private Button buttonFinish;
    private RequestQueue requestQueue;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rel);

        textView = findViewById(R.id.titleRel);
        textView.setText(getIntent().getStringExtra("nombreJuego"));

        leftColumnRecyclerView = findViewById(R.id.leftColumnRecyclerView);
        rightColumnRecyclerView = findViewById(R.id.rightColumnRecyclerView);
        buttonFinish = findViewById(R.id.buttonFinish);
        requestQueue = Volley.newRequestQueue(this);

        leftColumnRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rightColumnRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        questionsMap = new ArrayList<>();
        correctAnswersMap = new ArrayList<>();

        questionsList = new ArrayList<>();
        answersList = new ArrayList<>();

        leftColumnAdapter = new RelAdapter(questionsList);
        rightColumnAdapter = new RelAdapter(answersList);

        leftColumnRecyclerView.setAdapter(leftColumnAdapter);
        rightColumnRecyclerView.setAdapter(rightColumnAdapter);

        fetchQuestionsAndAnswers();

        ItemTouchHelper itemTouchHelperLeft = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                leftColumnAdapter.swapItems(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // No swipe action
            }
        });
        itemTouchHelperLeft.attachToRecyclerView(leftColumnRecyclerView);

        ItemTouchHelper itemTouchHelperRight = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                rightColumnAdapter.swapItems(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // No swipe action
            }
        });
        itemTouchHelperRight.attachToRecyclerView(rightColumnRecyclerView);

        buttonFinish.setOnClickListener(v -> {
            int correctAnswers = evaluateAnswers();
            showResultDialog(correctAnswers, questionsList.size());
        });

    }
    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    private void saveRecent() {
        Intent intent = getIntent();
        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
        String user = sp.getString("user", "");
        int idJuego = intent.getIntExtra("idJuego", 0);
        // Crear una solicitud POST para enviar los datos al servidor PHP
        String url = "https://prototipoformula.fun/save_recent.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String ans = jsonResponse.getString("ans");
                        if (ans.equals("ok")) {
                            // Éxito al guardar en la base de datos
                            Toast.makeText(Rel.this, "Juego guardado en recientes", Toast.LENGTH_SHORT).show();
                        } else {
                            // Error al guardar en la base de datos
                            Toast.makeText(Rel.this, "Error al guardar en recientes", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Rel.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(Rel.this, "Error de conexión", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user", user);
                params.put("idJuego", String.valueOf(idJuego));
                return params;
            }
        };

        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(request);
    }


    private int evaluateAnswers() {
        int correctCount = 0;
        for (int i = 0; i < questionsList.size(); i++) {
            if (answersList.get(i).equals(getCorrectAnswer(questionsList.get(i)))) {
                correctCount++;
            }
        }
        return correctCount;
    }

    private String getCorrectAnswer(String question) {
        // Aquí obtienes la respuesta correcta a partir de la pregunta utilizando el HashMap correctAnswersMap
        for (int i = 0; i < questionsMap.size(); i++){
            if (questionsMap.get(i).equals(question)){
                return correctAnswersMap.get(i);
            }
        }
        return "";
    }

    private void showResultDialog(int correctAnswers, int totalQuestions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Resultados del Cuestionario");

        String message = "Obtuviste " + correctAnswers + " pregunta(s) correcta(s) de " + totalQuestions + ".";
        if (correctAnswers == totalQuestions) {
            message += "\n¡Felicidades!";
        } else if (correctAnswers >= totalQuestions / 2) {
            message += "\n¡Bien hecho!";
        } else {
            message += "\n¡Puedes hacerlo mejor!";
        }

        builder.setMessage(message);
        builder.setPositiveButton("Reintentar", (dialog, which) -> {
            saveRecent();
            restartActivity();
        });
        builder.setNegativeButton("Salir", (dialog, which) -> {
            saveRecent();
            finish();
        });

        builder.create().show();
    }


    private void fetchQuestionsAndAnswers() {
        Intent intent = getIntent();
        int idJuego = intent.getIntExtra("idJuego", 0);
        String url = "https://prototipoformula.fun/get_form.php?id=" + idJuego;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> parseQuestionsAndAnswers(response),
                error -> Toast.makeText(Rel.this, "Error al obtener datos", Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void parseQuestionsAndAnswers(JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject questionObject = response.getJSONObject(i);
                String question = questionObject.getString("preg");
                String answer = questionObject.getString("resp");

                questionsMap.add(question);
                correctAnswersMap.add(answer);

                questionsList.add(question);
                answersList.add(answer);
            }
            Collections.shuffle(questionsList);
            Collections.shuffle(answersList);
            leftColumnAdapter.notifyDataSetChanged();
            rightColumnAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al parsear datos", Toast.LENGTH_SHORT).show();
        }
    }
}