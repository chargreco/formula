package com.rechargoyo.formula;

/**
 * Modelo de datos que representa una tarjeta dentro del minijuego Memorama ({@link Mem}).
 * Cada tarjeta contiene un texto (pregunta o respuesta) y mantiene el estado de si está revelada.
 *
 * @author Eduardo
 * @version 1.0
 */
public class Card {
    /** Texto mostrado en la tarjeta cuando está revelada (pregunta o respuesta). */
    private String text;

    /** Indica si el texto corresponde a una pregunta (true) o a una respuesta (false). */
    private boolean isQuestion;

    /** Indica si la tarjeta se encuentra volteada boca arriba (revelada). */
    private boolean revealed;

    /**
     * Construye una nueva tarjeta para el juego de Memorama.
     *
     * @param text       Texto visible en la tarjeta (pregunta o respuesta).
     * @param isQuestion true si la tarjeta representa una pregunta; false si representa una respuesta.
     */
    public Card(String text, boolean isQuestion) {
        this.text = text;
        this.isQuestion = isQuestion;
        this.revealed = false;
    }

    /**
     * Obtiene el texto contenido en la tarjeta.
     *
     * @return El texto de la pregunta o respuesta.
     */
    public String getText() {
        return text;
    }

    /**
     * Comprueba si la tarjeta corresponde a una pregunta.
     *
     * @return true si es una pregunta; false si es una respuesta.
     */
    public boolean isQuestion() {
        return isQuestion;
    }

    /**
     * Comprueba si la tarjeta está volteada boca arriba.
     *
     * @return true si está revelada; false si está oculta.
     */
    public boolean isRevealed() {
        return revealed;
    }

    /**
     * Modifica el estado de revelado de la tarjeta.
     *
     * @param revealed true para mostrar la tarjeta boca arriba, false para ocultarla.
     */
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }
}
