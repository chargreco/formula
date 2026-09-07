package com.rechargoyo.formula;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Actividad desplegada cuando ocurre un fallo de red o cuando el dispositivo pierde la conexión a internet.
 * Ofrece al usuario un botón de reintento para finalizar la pantalla y regresar a la vista anterior.
 *
 * @author Eduardo
 * @version 1.0
 */
public class NoConection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_no_conection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Evento asociado al botón de reintentar en la pantalla.
     * Cierra la actividad actual para reintentar la operación anterior.
     *
     * @param v Vista (botón) que ejecutó la acción.
     */
    public void retry(View v) {
        finish();
    }
}
