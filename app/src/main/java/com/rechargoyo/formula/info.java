package com.rechargoyo.formula;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Actividad que despliega la vista previa detallada de un juego seleccionado.
 * Muestra la portada según el tipo de minijuego, el nombre del creador, el título
 * y el código hexadecimal único asignado ("Fórmula"). Incluye el botón para iniciar la partida.
 *
 * @author Eduardo
 * @version 1.0
 */
public class info extends AppCompatActivity {

    private String nombreJuego;
    private int idJuego;
    private String tipoJuego;
    private String creadorJuego;
    private int tipoJuegoInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        idJuego = intent.getIntExtra("idJuego", 0);
        nombreJuego = intent.getStringExtra("nombreJuego");
        creadorJuego = intent.getStringExtra("creadorJuego");
        tipoJuegoInt = intent.getIntExtra("tipoJuegoInt", 10);

        tipoJuego = (tipoJuegoInt == 0 ? "Cuestionario Simple" : (tipoJuegoInt == 1 ? "Relacionar columnas" : (tipoJuegoInt == 2 ? "Tres en Raya" : "Memorama")));
        
        if (creadorJuego != null && !creadorJuego.isEmpty() && creadorJuego.charAt(0) != '@') {
            creadorJuego = "@" + creadorJuego;
        }

        TextView codigoJuego = findViewById(R.id.codigoJuego);
        codigoJuego.setText(Integer.toHexString(1000000 - idJuego).toUpperCase());

        ImageView imagenJuego = findViewById(R.id.imagenJuego);
        TextView tvNombreJuego = findViewById(R.id.nombreJuego);
        TextView tvTipoJuego = findViewById(R.id.tipoJuego);
        TextView tvCreadorJuego = findViewById(R.id.creadorJuego);

        // Seleccionar la portada según el tipo de juego
        switch (tipoJuegoInt) {
            case 0:
                imagenJuego.setImageResource(R.drawable.bquest);
                break;
            case 1:
                imagenJuego.setImageResource(R.drawable.brel);
                break;
            case 2:
                imagenJuego.setImageResource(R.drawable.btictac);
                break;
            case 3:
                imagenJuego.setImageResource(R.drawable.bmem);
                break;
            default:
                imagenJuego.setImageResource(R.drawable.brel);
                break;
        }

        tvNombreJuego.setText(nombreJuego);
        tvTipoJuego.setText(tipoJuego);
        tvCreadorJuego.setText(creadorJuego);
    }

    /**
     * Inicia la actividad correspondiente al minijuego seleccionado.
     *
     * @param v Vista (botón) que disparó el evento.
     */
    public void jugar(View v) {
        Intent intent;
        if (tipoJuegoInt == 0) {
            intent = new Intent(this, Quest.class);
        } else if (tipoJuegoInt == 1) {
            intent = new Intent(this, Rel.class);
        } else if (tipoJuegoInt == 2) {
            intent = new Intent(this, Tictac.class);
        } else if (tipoJuegoInt == 3) {
            intent = new Intent(this, Mem.class);
        } else {
            return;
        }

        intent.putExtra("idJuego", idJuego);
        intent.putExtra("nombreJuego", nombreJuego);
        intent.putExtra("creadorJuego", creadorJuego);
        startActivity(intent);
    }
}
