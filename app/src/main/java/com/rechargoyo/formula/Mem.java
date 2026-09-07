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

/**
 * Actividad que ejecuta la jugabilidad del minijuego "Memorama" (Tipo 3).
 * Distribuye tarjetas de preguntas y respuestas en una cuadrícula desordenada,
 * permitiendo al jugador voltear pares para encontrar sus coincidencias correspondientes.
 *
 * @author Eduardo
 * @version 1.0
 */
public class Mem extends AppCompatActivity {

    private RecyclerView memRecyclerView;
    private Button restartButton;
    private RequestQueue requestQueue;
    private List<Card> cards, quest, answs;
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

    /**
     * Consulta al backend las parejas de preguntas y respuestas para el juego actual.
     */
    private void fetchQuestionsAndAnswers() {
        String url = Constants.BASE_URL + "get_form.php?id=" + getIntent().getIntExtra("idJuego", 0);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                this::parseQuestionsAndAnswers,
                error -> Toast.makeText(Mem.this, "Error al obtener datos", Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Parsea el JSON recibido creando tarjetas individuales para cada pregunta y respuesta, y desordena el tablero.
     *
     * @param response {@link JSONArray} con la lista de parejas.
     */
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

    /**
     * Configura el {@link RecyclerView} en una cuadrícula de 2 columnas para el tablero de juego.
     */
    private void initializeRecyclerView() {
        memRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        selectedCardPositions = new ArrayList<>();
        attempts = 0;
        memAdapter = new MemAdapter(this, cards, this::handleCardClick);
        memRecyclerView.setAdapter(memAdapter);
    }

    /**
     * Maneja la interacción del usuario al tocar una tarjeta en el tablero.
     *
     * @param position Índice de la tarjeta en el adaptador.
     */
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

    /**
     * Verifica si las dos tarjetas seleccionadas forman un par pregunta-respuesta correcto.
     */
    private void checkForMatch() {
        if (selectedCardPositions.size() == 2) {
            int firstPosition = selectedCardPositions.get(0);
            int secondPosition = selectedCardPositions.get(1);

            Card firstCard = cards.get(firstPosition);
            Card secondCard = cards.get(secondPosition);

            boolean equals = false;
            for (int i = 0; i < quest.size(); i++) {
                if (quest.get(i).getText().equals(firstCard.getText()) && answs.get(i).getText().equals(secondCard.getText()) ||
                        quest.get(i).getText().equals(secondCard.getText()) && answs.get(i).getText().equals(firstCard.getText())) {
                    selectedCardPositions.clear();
                    equals = true;
                    break;
                }
            }
            if (!equals) {
                resetSelectedCards();
            }
        }
    }

    /**
     * Vuelve a ocultar boca abajo las dos tarjetas seleccionadas si no coincidieron.
     */
    private void resetSelectedCards() {
        for (int position : selectedCardPositions) {
            cards.get(position).setRevealed(false);
            memAdapter.notifyItemChanged(position);
        }
        selectedCardPositions.clear();
    }

    /**
     * Comprueba si todas las tarjetas del tablero han sido reveladas.
     *
     * @return true si se completó el juego; false en caso contrario.
     */
    private boolean isGameFinished() {
        for (Card card : cards) {
            if (!card.isRevealed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Despliega la ventana final del juego al haber encontrado todas las parejas.
     */
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

    /**
     * Guarda el resultado del juego en la lista de recientes del backend.
     */
    private void saveRecent() {
        Intent intent = getIntent();
        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
        String user = sp.getString("user", "");
        int idJuego = intent.getIntExtra("idJuego", 0);

        String url = Constants.BASE_URL + "save_recent.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String ans = jsonResponse.getString("ans");
                        if (ans.equals("ok")) {
                            Toast.makeText(Mem.this, "Juego guardado en recientes", Toast.LENGTH_SHORT).show();
                        } else {
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

        requestQueue.add(request);
    }

    /**
     * Reinicia el juego recargando las preguntas desde el servidor.
     */
    private void restartGame() {
        fetchQuestionsAndAnswers();
    }
}
