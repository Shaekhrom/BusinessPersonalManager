package com.barbacil.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AjustesActivity extends AppCompatActivity {
    Button botonVolverAJ;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ajustes);
        HideUI.setImmersiveMode(this);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Volver
        botonVolverAJ = findViewById(R.id.botonVolverAJ);
        botonVolverAJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(AjustesActivity.this, UserPageActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }
}