package com.rechargoyo.formula;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
 * Actividad que ejecuta la jugabilidad del minijuego "Tres en Raya Educativo" (Tipo 2).
 * Permite partidas entre 2 jugadores por turnos donde cada jugador debe responder correctamente
 * una pregunta de opción múltiple antes de habilitar el marcado de una casilla en la cuadrícula (X u O).
 *
 * @author Eduardo
 * @version 1.0
 */
public class Tictac extends AppCompatActivity {

    private GridLayout gridLayout;
    private TextView questionText;
    private RadioGroup answerGroup;
    private Button buttonSubmit;
    private List<Question2> questionList;
    private int currentQuestionIndex;
    private int playerTurn;
    private int[] playerScores;
    private int[] playerAttempts;
    private TextView[] buttons;
    private boolean[] buttonStates;
    private TextView tictac_instructions;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tictac);

        tictac_instructions = findViewById(R.id.tictac_instructions);
        gridLayout = findViewById(R.id.tictac_grid);
        questionText = findViewById(R.id.question_text);
        answerGroup = findViewById(R.id.answer_group);
        buttonSubmit = findViewById(R.id.button_submit);

        requestQueue = Volley.newRequestQueue(this);

        buttons = new TextView[9];
        buttonStates = new boolean[9];
        for (int i = 0; i < 9; i++) {
            buttons[i] = gridLayout.findViewById(getResources().getIdentifier("button" + (i + 1), "id", getPackageName()));
            int finalI = i;
            buttons[i].setOnClickListener(v -> handleGridButtonClick(finalI));
            buttonStates[i] = false;
        }

        playerTurn = 0; // Turno inicial: Jugador 1 (X)
        playerScores = new int[2];
        playerAttempts = new int[2];

        fetchQuestions();

        buttonSubmit.setOnClickListener(v -> handleAnswerSubmission());
        enableGridButtons(false);
        showInstructionsDialog();
        tictac_instructions.setText((playerTurn == 0 ? "Turno del jugador 1 (X)" : "Turno del jugador 2 (O)"));
    }

    /**
     * Muestra las instrucciones de juego iniciales al usuario.
     */
    private void showInstructionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instrucciones del juego");
        builder.setMessage("Este es un juego de tres en raya con preguntas de opción múltiple. " +
                "Cada jugador debe responder una pregunta correctamente para poder marcar una casilla. " +
                "El primer jugador en completar tres símbolos en una fila, columna o diagonal gana. " +
                "Si nadie gana y las casillas están llenas, el juego termina en empate.");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    /**
     * Solicita el conjunto de preguntas pertenecientes al juego desde la API PHP.
     */
    private void fetchQuestions() {
        String url = Constants.BASE_URL + "get_form.php?id=" + getIntent().getIntExtra("idJuego", 0);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                this::parseQuestions,
                error -> Toast.makeText(Tictac.this, "Error al obtener datos", Toast.LENGTH_SHORT).show()
        );
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Parsea el JSON con las preguntas y respuestas recibidas del servidor.
     *
     * @param response Objeto {@link JSONArray} con los datos.
     */
    private void parseQuestions(JSONArray response) {
        questionList = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject questionObject = response.getJSONObject(i);
                String qText = questionObject.getString("preg");
                String correctAnswer = questionObject.getString("respcorrect");
                ArrayList<String> answers = new ArrayList<>();
                answers.add(correctAnswer);
                answers.add(questionObject.getString("resp1"));
                answers.add(questionObject.getString("resp2"));
                answers.add(questionObject.getString("resp3"));
                Collections.shuffle(answers);

                questionList.add(new Question2(qText, answers, correctAnswer));
            }
            Collections.shuffle(questionList);
            showNextQuestion();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al parsear datos", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Muestra la siguiente pregunta en pantalla para el jugador en turno.
     */
    private void showNextQuestion() {
        if (currentQuestionIndex >= questionList.size()) {
            currentQuestionIndex = 0;
        }
        Question2 currentQuestion = questionList.get(currentQuestionIndex);
        questionText.setText(currentQuestion.getQuestionText());
        answerGroup.removeAllViews();
        for (String answer : currentQuestion.getAnswers()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(answer);
            answerGroup.addView(radioButton);
        }
    }

    /**
     * Evalúa la respuesta elegida por el usuario para otorgar o denegar el turno en el tablero.
     */
    private void handleAnswerSubmission() {
        int selectedId = answerGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Selecciona una respuesta", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedButton = findViewById(selectedId);
        String selectedAnswer = selectedButton.getText().toString();
        Question2 currentQuestion = questionList.get(currentQuestionIndex);

        if (currentQuestion.getCorrectAnswer().equals(selectedAnswer)) {
            Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show();
            enableGridButtons(true);
            buttonSubmit.setEnabled(false);
        } else {
            Toast.makeText(this, "Respuesta incorrecta. Cambio de turno", Toast.LENGTH_SHORT).show();
            playerAttempts[playerTurn]++;
            currentQuestionIndex++;
            switchPlayerTurn();
            showNextQuestion();
        }
    }

    /**
     * Procesa la selección de una casilla en la cuadrícula de Tres en Raya.
     *
     * @param index Índice (0 a 8) de la casilla seleccionada.
     */
    private void handleGridButtonClick(int index) {
        if (buttonStates[index]) return;
        answerGroup.check(-1);
        buttonSubmit.setEnabled(true);
        buttonStates[index] = true;
        buttons[index].setText(playerTurn == 0 ? "X" : "O");
        playerScores[playerTurn]++;
        playerAttempts[playerTurn]++;
        currentQuestionIndex++;
        enableGridButtons(false);

        if (checkForWin()) {
            showGameOverDialog(playerTurn == 0 ? "¡Jugador 1 (X) gana!" : "¡Jugador 2 (O) gana!");
        } else if (isBoardFull()) {
            showGameOverDialog("Empate");
        } else {
            switchPlayerTurn();
            showNextQuestion();
        }
    }

    /**
     * Habilita o deshabilita los botones del tablero según la fase del turno.
     *
     * @param enable true para habilitar selección de casilla; false para bloquear.
     */
    private void enableGridButtons(boolean enable) {
        int colorX = getResources().getColor(R.color.X);
        int colorO = getResources().getColor(R.color.O);

        for (int i = 0; i < 9; i++) {
            buttons[i].setEnabled(enable && !buttonStates[i]);
            if (buttons[i].getText().toString().equals("X")) {
                buttons[i].setTextColor(colorX);
            } else if (buttons[i].getText().toString().equals("O")) {
                buttons[i].setTextColor(colorO);
            }
        }
    }

    /**
     * Alterna el turno entre el Jugador 1 (X) y Jugador 2 (O).
     */
    private void switchPlayerTurn() {
        playerTurn = (playerTurn + 1) % 2;
        tictac_instructions.setText((playerTurn == 0 ? "Turno del jugador 1 (X)" : "Turno del jugador 2 (O)"));
    }

    /**
     * Comprueba si alguno de los jugadores ha completado una línea ganadora de tres en raya.
     *
     * @return true si hay un ganador; false si no.
     */
    private boolean checkForWin() {
        return (buttons[0].getText() == buttons[1].getText() && buttons[1].getText() == buttons[2].getText() && !buttons[0].getText().toString().isEmpty()) ||
                (buttons[3].getText() == buttons[4].getText() && buttons[4].getText() == buttons[5].getText() && !buttons[3].getText().toString().isEmpty()) ||
                (buttons[6].getText() == buttons[7].getText() && buttons[7].getText() == buttons[8].getText() && !buttons[6].getText().toString().isEmpty()) ||
                (buttons[0].getText() == buttons[3].getText() && buttons[3].getText() == buttons[6].getText() && !buttons[0].getText().toString().isEmpty()) ||
                (buttons[1].getText() == buttons[4].getText() && buttons[4].getText() == buttons[7].getText() && !buttons[1].getText().toString().isEmpty()) ||
                (buttons[2].getText() == buttons[5].getText() && buttons[5].getText() == buttons[8].getText() && !buttons[2].getText().toString().isEmpty()) ||
                (buttons[0].getText() == buttons[4].getText() && buttons[4].getText() == buttons[8].getText() && !buttons[0].getText().toString().isEmpty()) ||
                (buttons[2].getText() == buttons[4].getText() && buttons[4].getText() == buttons[6].getText() && !buttons[2].getText().toString().isEmpty());
    }

    /**
     * Comprueba si todas las casillas del tablero han sido ocupadas.
     *
     * @return true si el tablero está lleno; false en caso contrario.
     */
    private boolean isBoardFull() {
        for (boolean state : buttonStates) {
            if (!state) return false;
        }
        return true;
    }

    /**
     * Despliega la ventana final del juego con el resumen de estadísticas de aciertos por jugador.
     *
     * @param message Texto del resultado final (Victoria o Empate).
     */
    private void showGameOverDialog(String message) {
        String player1Stats = "Jugador 1 (X): " + playerScores[0] + " aciertos / " + playerAttempts[0] + " preguntas hechas.";
        String player2Stats = "Jugador 2 (O): " + playerScores[1] + " aciertos / " + playerAttempts[1] + " preguntas hechas.";

        String finalMessage = message + "\n\n" + player1Stats + "\n" + player2Stats;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fin del juego");
        builder.setMessage(finalMessage);
        builder.setPositiveButton("Reiniciar", (dialog, which) -> {
            saveRecent();
            restartActivity();
        });
        builder.setNegativeButton("Salir", (dialog, which) -> {
            saveRecent();
            finish();
        });
        builder.show();
    }

    /**
     * Reinicia la actividad actual.
     */
    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    /**
     * Guarda la partida recién jugada en el historial del servidor.
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
                            Toast.makeText(Tictac.this, "Juego guardado en recientes", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Tictac.this, "Error al guardar en recientes", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Tictac.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(Tictac.this, "Error de conexión", Toast.LENGTH_SHORT).show()) {
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
}

/**
 * Clase auxiliar interna que representa una pregunta de opción múltiple dentro del juego Tres en Raya.
 */
class Question2 {
    private String questionText;
    private List<String> answers;
    private String correctAnswer;

    public Question2(String questionText, List<String> answers, String correctAnswer) {
        this.questionText = questionText;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
