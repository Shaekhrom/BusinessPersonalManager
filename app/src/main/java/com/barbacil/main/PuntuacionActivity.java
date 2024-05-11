package com.barbacil.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PuntuacionActivity extends AppCompatActivity {
    Button botonVolverPT;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_puntuacion);
        HideUI.setImmersiveMode(this);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Volver
        botonVolverPT = findViewById(R.id.botonVolverPT);
        botonVolverPT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(PuntuacionActivity.this, UserPageActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }
}