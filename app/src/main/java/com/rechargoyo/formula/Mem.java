package com.rechargoyo.formula;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class Mem extends AppCompatActivity {

    private RecyclerView memRecyclerView;
    private Button restartButton;
    private RequestQueue requestQueue;
    private List<Card> cards,quest, answs;
    private List<Integer> selectedCardPositions;
    private MemAdapter memAdapter;
    private int attempts;
    private Handler handler;
    private TextView memInstructions;
    private boolean isProcessing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem);

        quest = new ArrayList<>();
        answs = new ArrayList<>();
        memInstructions = findViewById(R.id.mem_instructions);
        memRecyclerView = findViewById(R.id.mem_recycler_view);
        restartButton = findViewById(R.id.mem_restart_button);

        requestQueue = Volley.newRequestQueue(this);
        handler = new Handler();
        isProcessing = false;

        fetchQuestionsAndAnswers();

        restartButton.setOnClickListener(v -> restartGame());
    }

    private void fetchQuestionsAndAnswers() {
        String url = "https://prototipoformula.fun/get_form.php?id=" + getIntent().getIntExtra("idJuego", 0);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                this::parseQuestionsAndAnswers,
                error -> Toast.makeText(Mem.this, "Error al obtener datos", Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void parseQuestionsAndAnswers(JSONArray response) {
        cards = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject cardObject = response.getJSONObject(i);
                String question = cardObject.getString("preg");
                String answer = cardObject.getString("resp");
                cards.add(new Card(question, true));
                quest.add(new Card(question, true));
                answs.add(new Card(answer, false));
                cards.add(new Card(answer, false));
            }
            Collections.shuffle(cards);
            initializeRecyclerView();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al parsear datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeRecyclerView() {
        memRecyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 tarjetas por línea
        selectedCardPositions = new ArrayList<>();
        attempts = 0;
        memAdapter = new MemAdapter(this, cards, this::handleCardClick);
        memRecyclerView.setAdapter(memAdapter);
    }

    private void handleCardClick(int position) {
        if (isProcessing || selectedCardPositions.contains(position) || cards.get(position).isRevealed()) return;

        selectedCardPositions.add(position);
        cards.get(position).setRevealed(true);
        memAdapter.notifyItemChanged(position);

        if (selectedCardPositions.size() == 2) {
            attempts++;
            isProcessing = true;
            handler.postDelayed(() -> {
                checkForMatch();
                isProcessing = false;
                if (isGameFinished()) {
                    showGameOverDialog();
                }
            }, 1000);
        }
    }

    private void checkForMatch() {
        if (selectedCardPositions.size() == 2) {
            int firstPosition = selectedCardPositions.get(0);
            int secondPosition = selectedCardPositions.get(1);

            Card firstCard = cards.get(firstPosition);
            Card secondCard = cards.get(secondPosition);

            boolean equals = false;
            for (int i = 0; i < quest.size(); i++) {
                if(quest.get(i).getText().equals(firstCard.getText()) && answs.get(i).getText().equals(secondCard.getText()) || quest.get(i).getText().equals(secondCard.getText()) && answs.get(i).getText().equals(firstCard.getText())){
                    selectedCardPositions.clear();
                    equals = true;
                }
            }
            if(!equals){
                resetSelectedCards();
            }

//            if (firstCard.isQuestion() != secondCard.isQuestion() && firstCard.getText().equals(secondCard.getText())) {
//                selectedCardPositions.clear();
//            } else {
//                resetSelectedCards();
//            }
        }
    }

    private void resetSelectedCards() {
        for (int position : selectedCardPositions) {
            cards.get(position).setRevealed(false);
            memAdapter.notifyItemChanged(position);
        }
        selectedCardPositions.clear();
    }

    private boolean isGameFinished() {
        for (Card card : cards) {
            if (!card.isRevealed()) {
                return false;
            }
        }
        return true;
    }

    private void showGameOverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Juego Terminado!");
        builder.setMessage("Lo lograste en " + attempts + " intentos");
        builder.setPositiveButton("Reiniciar", (dialog, which) -> {
            saveRecent();
            restartGame();
        });
        builder.setNegativeButton("Salir", (dialog, which) -> {
            saveRecent();
            finish();
        });
        builder.show();
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
                            Toast.makeText(Mem.this, "Juego guardado en recientes", Toast.LENGTH_SHORT).show();
                        } else {
                            // Error al guardar en la base de datos
                            Toast.makeText(Mem.this, "Error al guardar en recientes", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Mem.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(Mem.this, "Error de conexión", Toast.LENGTH_SHORT).show()) {
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

    private void restartGame() {
        fetchQuestionsAndAnswers();
    }
}
