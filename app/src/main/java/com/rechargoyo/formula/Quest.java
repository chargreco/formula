package com.rechargoyo.formula;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
import java.util.List;
import java.util.Map;

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
        textView.setText(getIntent().getStringExtra("nombreJuego"));

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

    private void saveRecent() {
        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
        String user = sp.getString("user", "");

        // Crear una solicitud POST para enviar los datos al servidor PHP
        String url = "https://prototipoformula.fun/save_recent.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String ans = jsonResponse.getString("ans");
                        if (ans.equals("ok")) {
                            // Éxito al guardar en la base de datos
                            Toast.makeText(Quest.this, "Juego guardado en recientes", Toast.LENGTH_SHORT).show();
                        } else {
                            // Error al guardar en la base de datos
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

        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(request);
    }


    private int evaluateAnswers() {
        int correctCount = 0;
        for (Question question : questionList) {
            if (question.isAnswerCorrect()) {
                correctCount++;
            }
        }
        return correctCount;
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void fetchQuestions(int idJuego) {
        String url = "https://prototipoformula.fun/get_form.php?id=" + idJuego;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> parseQuestions(response),
                error -> Toast.makeText(Quest.this, "Error al obtener datos", Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(jsonArrayRequest);
    }

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
