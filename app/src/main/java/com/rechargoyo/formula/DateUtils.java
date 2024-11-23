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

public class DateUtils {

    @SuppressLint("SimpleDateFormat")
    public static String formatDate(String inputDate) {
        SimpleDateFormat initialFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;

        try {
            date = initialFormat.parse(inputDate);
        } catch (ParseException e) {
            return inputDate; // Si no se puede parsear, devolvemos la fecha original
        }

        // Convertimos a LocalDateTime para facilitar el cálculo de diferencias
        ZonedDateTime zonedDate = date.toInstant().atZone(ZoneId.systemDefault());
        LocalDateTime dateTime = zonedDate.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();

        // Calcular la diferencia de tiempo
        long diffMinutes = ChronoUnit.MINUTES.between(dateTime, now);
        long diffHours = ChronoUnit.HOURS.between(dateTime, now);
        long diffDays = ChronoUnit.DAYS.between(dateTime, now);

        if (diffDays == 0) {
            if(diffMinutes == 0){
                return "Justo ahora";
            } else if (diffMinutes < 60) {
                return "Hace " + diffMinutes + " minutos";
            } else {
                return "Hace " + diffHours + " horas";
            }
        } else if (diffDays == 1) {
            return "Ayer";
        } else {
            // Formato para fechas que no son de hoy ni de ayer
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy");
            return dateTime.format(formatter);
        }
    }
}
