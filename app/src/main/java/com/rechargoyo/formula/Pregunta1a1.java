package com.rechargoyo.formula;

/**
 * Modelo de datos que representa un par de pregunta y respuesta (1 a 1)
 * utilizado en los minijuegos de Relacionar Columnas ({@link Rel}) y Memorama ({@link Mem}).
 *
 * @author Eduardo
 * @version 1.0
 */
public class Pregunta1a1 {

    /** Número correlativo de la pregunta dentro del formulario. */
    private int numPregunta;

    /** Texto del enunciado de la pregunta o concepto inicial. */
    private String qust;

    /** Texto de la respuesta correspondiente o concepto emparejado. */
    private String asw;

    /**
     * Constructor por defecto para inicialización vacía.
     */
    public Pregunta1a1() {
    }

    /**
     * Construye una nueva instancia de Pregunta1a1 con valores iniciales.
     *
     * @param qust Texto de la pregunta o concepto.
     * @param asw  Texto de la respuesta o concepto emparejado.
     */
    public Pregunta1a1(String qust, String asw) {
        this.qust = qust;
        this.asw = asw;
    }

    /**
     * Establece el enunciado de la pregunta.
     *
     * @param qust Texto de la pregunta.
     */
    public void setQust(String qust) {
        this.qust = qust;
    }

    /**
     * Obtiene el enunciado de la pregunta.
     *
     * @return Enunciado de la pregunta.
     */
    public String getQust() {
        return this.qust;
    }

    /**
     * Establece el texto de la respuesta.
     *
     * @param asw Texto de la respuesta.
     */
    public void setAsw(String asw) {
        this.asw = asw;
    }

    /**
     * Obtiene el texto de la respuesta.
     *
     * @return Respuesta asociada.
     */
    public String getAsw() {
        return this.asw;
    }

    /**
     * Obtiene el número correlativo de la pregunta.
     *
     * @return Número de pregunta.
     */
    public int getNumPregunta() {
        return numPregunta;
    }

    /**
     * Establece el número correlativo de la pregunta.
     *
     * @param numPregunta Número correlativo.
     */
    public void setNumPregunta(int numPregunta) {
        this.numPregunta = numPregunta;
    }
}
