package com.barbacil.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserPageActivity extends AppCompatActivity {

    Button botonEventos;
    Intent intentEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        HideUI.setImmersiveMode(this);

        botonEventos = findViewById(R.id.botonEventos);
        botonEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intentEventos = new Intent(UserPageActivity.this, EventosActivity.class);

                // Inicia la actividad de eventos
                startActivity(intentEventos);
            }
        });
    }
}