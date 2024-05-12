package com.barbacil.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import objectClasses.Estatica;
import objectClasses.Usuario;

public class EventosActivity extends AppCompatActivity {
    TextView volverEVTV,agnadirEventosEVTV;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eventos);
        HideUI.setImmersiveMode(this);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Volver
        volverEVTV = findViewById(R.id.volverEVTV);
        volverEVTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(EventosActivity.this, UserPageActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Añadir un evento
        agnadirEventosEVTV = findViewById(R.id.agnadirEventosEVTV);
        agnadirEventosEVTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usuario = Estatica.getUsuarioEstatico();
                if(usuario.isEsAdmin()){
                    intent = new Intent(EventosActivity.this, AgnadirEventoActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(EventosActivity.this, "Necesitas ser Administrador para crear eventos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }
}