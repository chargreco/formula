package com.rechargoyo.formula;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;

public class LowerCaseInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // Definir los caracteres permitidos
        String allowedChars = "abcdefghijklmnopqrstuvwxyz0123456789_$&.";

        // Recorrer los caracteres ingresados
        for (int i = start; i < end; i++) {
            // Verificar si el caracter no está permitido
            if (!allowedChars.contains(String.valueOf(source.charAt(i)))) {
                // Remover el caracter no permitido
                return "";
            }
        }

        // Permitir el texto ingresado
        return null;
    }
}
