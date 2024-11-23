package com.rechargoyo.formula;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class info extends AppCompatActivity {

    String nombreJuego;
    int idJuego;
    String tipoJuego;
    String creadorJuego;
    int tipoJuegoInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Obtener el Intent que inició la actividad
        Intent intent = getIntent();

        // Extraer datos del Intent
        idJuego = intent.getIntExtra("idJuego",0);



        nombreJuego = intent.getStringExtra("nombreJuego");
        creadorJuego = intent.getStringExtra("creadorJuego");
        tipoJuegoInt = intent.getIntExtra("tipoJuegoInt", 10);
        tipoJuego = (tipoJuegoInt == 0 ? "Cuestionario Simple" : (tipoJuegoInt == 1 ? "Relacionar columnas" : (tipoJuegoInt == 2 ? "Tres en Raya" : "Memorama")));
        if(! (creadorJuego.charAt(0)=='@')) creadorJuego = "@" + creadorJuego;


        TextView codigoJuego = findViewById(R.id.codigoJuego);
        codigoJuego.setText(Integer.toHexString(1000000-idJuego).toUpperCase());



        // Obtener referencias a los elementos de la vista
        ImageView imagenJuego = findViewById(R.id.imagenJuego);
        TextView tvNombreJuego = findViewById(R.id.nombreJuego);
        TextView tvTipoJuego = findViewById(R.id.tipoJuego);
        TextView tvCreadorJuego = findViewById(R.id.creadorJuego);

        // Cambiar la imagen dependiendo del tipo de juego
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
                imagenJuego.setImageResource(R.drawable.brel); // Imagen por defecto
                break;
        }

        // Actualizar TextViews
        tvNombreJuego.setText(nombreJuego);
        tvTipoJuego.setText(tipoJuego);
        tvCreadorJuego.setText(creadorJuego);

    }

    public void jugar(View v){
        if(tipoJuegoInt == 0) {
            Intent intent = new Intent(this, Quest.class);
            intent.putExtra("idJuego", idJuego);
            intent.putExtra("nombreJuego", nombreJuego);
            intent.putExtra("creadorJuego", creadorJuego);
            startActivity(intent);
        } else if (tipoJuegoInt == 1) {
            Intent intent = new Intent(this, Rel.class);
            intent.putExtra("idJuego", idJuego);
            intent.putExtra("nombreJuego", nombreJuego);
            intent.putExtra("creadorJuego", creadorJuego);
            startActivity(intent);
        } else if (tipoJuegoInt == 2) {
            Intent intent = new Intent(this, Tictac.class);
            intent.putExtra("idJuego", idJuego);
            intent.putExtra("nombreJuego", nombreJuego);
            intent.putExtra("creadorJuego", creadorJuego);
            startActivity(intent);
        } else if (tipoJuegoInt == 3) {
            Intent intent = new Intent(this, Mem.class);
            intent.putExtra("idJuego", idJuego);
            intent.putExtra("nombreJuego", nombreJuego);
            intent.putExtra("creadorJuego", creadorJuego);
            startActivity(intent);
        }
    }
}
