package com.rechargoyo.formula;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Clase de utilidad para formatear cadenas de fecha y hora recibidas desde el servidor
 * en textos amigables relativos (ejemplo: "Justo ahora", "Hace 5 minutos", "Hace 2 horas", "Ayer" o fecha formateada).
 *
 * @author Eduardo
 * @version 1.0
 */
public class DateUtils {

    /**
     * Convierte una cadena de fecha con formato {@code "yyyy-MM-dd HH:mm:ss"} en un texto descriptivo
     * que expresa el tiempo transcurrido respecto al momento actual.
     *
     * @param inputDate Cadena de texto con la fecha original recibida de la base de datos.
     * @return Texto formateado de manera relativa (ej. "Justo ahora", "Hace X minutos", "Ayer", etc.).
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDate(String inputDate) {
        SimpleDateFormat initialFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;

        try {
            date = initialFormat.parse(inputDate);
        } catch (ParseException e) {
            return inputDate; // Si falla el parseo, se devuelve la cadena original sin alteración
        }

        // Conversión a LocalDateTime para facilitar el cálculo de diferencias
        ZonedDateTime zonedDate = date.toInstant().atZone(ZoneId.systemDefault());
        LocalDateTime dateTime = zonedDate.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();

        // Cálculo de diferencias temporales
        long diffMinutes = ChronoUnit.MINUTES.between(dateTime, now);
        long diffHours = ChronoUnit.HOURS.between(dateTime, now);
        long diffDays = ChronoUnit.DAYS.between(dateTime, now);

        if (diffDays == 0) {
            if (diffMinutes == 0) {
                return "Justo ahora";
            } else if (diffMinutes < 60) {
                return "Hace " + diffMinutes + " minutos";
            } else {
                return "Hace " + diffHours + " horas";
            }
        } else if (diffDays == 1) {
            return "Ayer";
        } else {
            // Formato para fechas antiguas
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy");
            return dateTime.format(formatter);
        }
    }
}
