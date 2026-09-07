package com.rechargoyo.formula;

/**
 * Modelo de datos que representa una pregunta de opción múltiple (con 3 respuestas incorrectas y 1 correcta)
 * utilizada durante la creación de Cuestionarios Simples ({@link PregOpc}) y Tres en Raya ({@link Tictac}).
 *
 * @author Eduardo
 * @version 1.0
 */
public class PreguntaOpc {

    /** Texto o enunciado de la pregunta. */
    private String pregunta;

    /** Primera respuesta incorrecta. */
    private String respuesta1;

    /** Segunda respuesta incorrecta. */
    private String respuesta2;

    /** Tercera respuesta incorrecta. */
    private String respuesta3;

    /** Respuesta correcta esperada. */
    private String respuestaCorrecta;

    /**
     * Construye una nueva pregunta de opción múltiple.
     *
     * @param pregunta          Enunciado de la pregunta.
     * @param respuesta1        Opción incorrecta 1.
     * @param respuesta2        Opción incorrecta 2.
     * @param respuesta3        Opción incorrecta 3.
     * @param respuestaCorrecta Opción correcta esperada.
     */
    public PreguntaOpc(String pregunta, String respuesta1, String respuesta2, String respuesta3, String respuestaCorrecta) {
        this.pregunta = pregunta;
        this.respuesta1 = respuesta1;
        this.respuesta2 = respuesta2;
        this.respuesta3 = respuesta3;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    /**
     * Obtiene el enunciado de la pregunta.
     *
     * @return Texto de la pregunta.
     */
    public String getPregunta() {
        return pregunta;
    }

    /**
     * Establece el enunciado de la pregunta.
     *
     * @param pregunta Texto de la pregunta.
     */
    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    /**
     * Obtiene la primera respuesta incorrecta.
     *
     * @return Respuesta 1.
     */
    public String getRespuesta1() {
        return respuesta1;
    }

    /**
     * Establece la primera respuesta incorrecta.
     *
     * @param respuesta1 Respuesta 1.
     */
    public void setRespuesta1(String respuesta1) {
        this.respuesta1 = respuesta1;
    }

    /**
     * Obtiene la segunda respuesta incorrecta.
     *
     * @return Respuesta 2.
     */
    public String getRespuesta2() {
        return respuesta2;
    }

    /**
     * Establece la segunda respuesta incorrecta.
     *
     * @param respuesta2 Respuesta 2.
     */
    public void setRespuesta2(String respuesta2) {
        this.respuesta2 = respuesta2;
    }

    /**
     * Obtiene la tercera respuesta incorrecta.
     *
     * @return Respuesta 3.
     */
    public String getRespuesta3() {
        return respuesta3;
    }

    /**
     * Establece la tercera respuesta incorrecta.
     *
     * @param respuesta3 Respuesta 3.
     */
    public void setRespuesta3(String respuesta3) {
        this.respuesta3 = respuesta3;
    }

    /**
     * Obtiene la respuesta correcta.
     *
     * @return Respuesta correcta.
     */
    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    /**
     * Establece la respuesta correcta.
     *
     * @param respuestaCorrecta Respuesta correcta.
     */
    public void setRespuestaCorrecta(String respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }
}
