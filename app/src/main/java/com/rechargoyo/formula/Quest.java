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

/**
 * Actividad que ejecuta la jugabilidad del minijuego "Cuestionario Simple" (Tipo 0).
 * Carga las preguntas del backend, las desordena de manera aleatoria, evalúa las respuestas elegidas
 * por el usuario y guarda la partida en la tabla de recientes.
 *
 * @author Eduardo
 * @version 1.0
 */
public class Quest extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    private ArrayList<Question> questionList;
    private Button buttonFinish;
    private RequestQueue requestQueue;
    private int idJuego;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        textView = findViewById(R.id.titleRel);
        if (getIntent().hasExtra("nombreJuego")) {
            textView.setText(getIntent().getStringExtra("nombreJuego"));
        }

        recyclerView = findViewById(R.id.recyclerView);
        buttonFinish = findViewById(R.id.buttonFinish);
        requestQueue = Volley.newRequestQueue(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        idJuego = intent.getIntExtra("idJuego", 0);

        questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(questionList);
        recyclerView.setAdapter(questionAdapter);

        fetchQuestions(idJuego);

        buttonFinish.setOnClickListener(v -> {
            int correctAnswers = evaluateAnswers();
            showResultDialog(correctAnswers, questionList.size());
        });
    }

    /**
     * Despliega el diálogo emergente con los resultados del cuestionario.
     *
     * @param correctAnswers Cantidad de respuestas acertadas.
     * @param totalQuestions Número total de preguntas del cuestionario.
     */
    private void showResultDialog(int correctAnswers, int totalQuestions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Resultados del Cuestionario");

        String message = "Tuviste " + correctAnswers + " pregunta(s) correcta(s) de " + totalQuestions + ".";
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

    /**
     * Registra la partida completada en la tabla de recientes del servidor backend.
     */
    private void saveRecent() {
        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
        String user = sp.getString("user", "");

        String url = Constants.BASE_URL + "save_recent.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String ans = jsonResponse.getString("ans");
                        if (ans.equals("ok")) {
                            Toast.makeText(Quest.this, "Juego guardado en recientes", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Quest.this, "Error al guardar en recientes", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Quest.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(Quest.this, "Error de conexión", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user", user);
                params.put("idJuego", String.valueOf(idJuego));
                return params;
            }
        };

        requestQueue.add(request);
    }

    /**
     * Evalúa cuántas de las preguntas fueron respondidas correctamente.
     *
     * @return Cantidad de aciertos.
     */
    private int evaluateAnswers() {
        int correctCount = 0;
        for (Question question : questionList) {
            if (question.isAnswerCorrect()) {
                correctCount++;
            }
        }
        return correctCount;
    }

    /**
     * Reinicia la actividad para volver a jugar el cuestionario.
     */
    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    /**
     * Solicita al servidor el conjunto de preguntas pertenecientes al juego.
     *
     * @param idJuego Identificador del juego en el servidor.
     */
    private void fetchQuestions(int idJuego) {
        String url = Constants.BASE_URL + "get_form.php?id=" + idJuego;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                this::parseQuestions,
                error -> Toast.makeText(Quest.this, "Error al obtener datos", Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Parsea la respuesta JSON recibida y llena la lista de preguntas desordenadas.
     *
     * @param response Objeto {@link JSONArray} recibido de la API.
     */
    private void parseQuestions(JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject questionObject = response.getJSONObject(i);
                String questionText = questionObject.getString("preg");
                String correctAnswer = questionObject.getString("respcorrect");
                ArrayList<String> answers = new ArrayList<>();
                answers.add(correctAnswer);
                answers.add(questionObject.getString("resp1"));
                answers.add(questionObject.getString("resp2"));
                answers.add(questionObject.getString("resp3"));
                Collections.shuffle(answers);

                Question question = new Question(questionText, answers, correctAnswer);
                questionList.add(question);
            }
            Collections.shuffle(questionList);
            questionAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al parsear datos", Toast.LENGTH_SHORT).show();
        }
    }
}
