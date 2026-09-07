package com.rechargoyo.formula;

import java.util.ArrayList;

/**
 * Modelo de datos que representa una pregunta de opción múltiple dentro del Cuestionario Simple ({@link Quest}).
 * Mantiene la lista de opciones posibles, la respuesta correcta y la opción seleccionada por el usuario.
 *
 * @author Eduardo
 * @version 1.0
 */
public class Question {
    /** Enunciado o texto de la pregunta. */
    private String questionText;

    /** Lista de opciones de respuesta posibles. */
    private ArrayList<String> answers;

    /** Cadena con el texto exacto de la respuesta correcta. */
    private String correctAnswer;

    /** Índice de la opción seleccionada por el usuario (-1 si ninguna opción está seleccionada). */
    private int selectedOptionIndex = -1;

    /**
     * Construye un nuevo objeto Question.
     *
     * @param questionText Enunciado de la pregunta.
     * @param answers      Lista de opciones de respuesta.
     * @param correctAnswer Respuesta correcta esperada.
     */
    public Question(String questionText, ArrayList<String> answers, String correctAnswer) {
        this.questionText = questionText;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    /**
     * Obtiene el enunciado de la pregunta.
     *
     * @return Enunciado de la pregunta.
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Obtiene la lista de opciones de respuesta.
     *
     * @return Lista de respuestas.
     */
    public ArrayList<String> getAnswers() {
        return answers;
    }

    /**
     * Obtiene el texto de la respuesta correcta.
     *
     * @return Respuesta correcta.
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Establece el índice de la opción seleccionada por el usuario.
     *
     * @param selectedOptionIndex Índice (0 a 3) de la opción marcada.
     */
    public void setSelectedOptionIndex(int selectedOptionIndex) {
        this.selectedOptionIndex = selectedOptionIndex;
    }

    /**
     * Verifica si la opción seleccionada por el usuario coincide con la respuesta correcta.
     *
     * @return true si la respuesta elegida es correcta; false en caso contrario o si no ha seleccionado opción.
     */
    public boolean isAnswerCorrect() {
        if (selectedOptionIndex >= 0 && selectedOptionIndex < answers.size()) {
            String selectedAnswer = answers.get(selectedOptionIndex);
            return selectedAnswer.equals(correctAnswer);
        }
        return false;
    }
}
