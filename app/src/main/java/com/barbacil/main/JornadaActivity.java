package com.barbacil.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class JornadaActivity extends AppCompatActivity {
    Button botonVolverRJ;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jornada);
        HideUI.setImmersiveMode(this);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Volver
        botonVolverRJ = findViewById(R.id.botonVolverRJ);
        botonVolverRJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(JornadaActivity.this, UserPageActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

    }
}