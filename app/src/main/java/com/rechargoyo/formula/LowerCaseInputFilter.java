package com.rechargoyo.formula;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

/**
 * Filtro de entrada para campos de texto ({@link EditText}) que restringe
 * los caracteres ingresados únicamente a letras minúsculas, números y caracteres especiales permitidos
 * para nombres de usuario (letras a-z, 0-9, y símbolos _, $, &, .).
 *
 * @author Eduardo
 * @version 1.0
 */
public class LowerCaseInputFilter implements InputFilter {

    /**
     * Filtra los caracteres ingresados por el usuario impidiendo el ingreso de caracteres no permitidos o mayúsculas.
     *
     * @param source Cadena de texto de entrada que el usuario intenta ingresar.
     * @param start  Índice inicial en source.
     * @param end    Índice final en source.
     * @param dest   Texto de destino actual en el campo de entrada.
     * @param dstart Índice de inicio dentro del campo de destino.
     * @param dend   Índice final dentro del campo de destino.
     * @return Cadena vacía {@code ""} si el carácter no es permitido (para bloquearlo); o {@code null} si es válido.
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // Conjunto de caracteres permitidos explícitamente
        String allowedChars = "abcdefghijklmnopqrstuvwxyz0123456789_$&.";

        // Recorrer los caracteres ingresados
        for (int i = start; i < end; i++) {
            if (!allowedChars.contains(String.valueOf(source.charAt(i)))) {
                // Bloquear el carácter no permitido
                return "";
            }
        }

        // Permitir la entrada si todos los caracteres son válidos
        return null;
    }
}
