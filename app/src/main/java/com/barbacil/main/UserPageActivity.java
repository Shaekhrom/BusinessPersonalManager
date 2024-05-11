package com.barbacil.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserPageActivity extends AppCompatActivity {

    Button botonEventos, botonJornada,botonChat,botonPuntuacion,botonAjustes,botonSalir;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        HideUI.setImmersiveMode(this);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //ir a eventos
        botonEventos = findViewById(R.id.botonEventos);
        botonEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(UserPageActivity.this, EventosActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //ir a jornada
        botonJornada = findViewById(R.id.botonJornada);
        botonJornada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(UserPageActivity.this, JornadaActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //ir a chat
        botonChat = findViewById(R.id.botonChat);
        botonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(UserPageActivity.this, ChatActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //ir a puntuacion
        botonPuntuacion = findViewById(R.id.botonPuntuacion);
        botonPuntuacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(UserPageActivity.this, PuntuacionActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //ir a ajustes
        botonAjustes = findViewById(R.id.botonAjustes);
        botonAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(UserPageActivity.this, AjustesActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //salir de la app
        botonSalir = findViewById(R.id.botonSalir);
        botonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

    }
}