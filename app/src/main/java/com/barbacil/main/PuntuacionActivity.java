package com.barbacil.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import connections.ComentarioCallback;
import objectClasses.Estatica;
import objectClasses.Puntuacion;

public class PuntuacionActivity extends AppCompatActivity {
    Button botonVolverPT, botonGestionarPuntuacion;
    Intent intent;
    TextView emojiPuntuacionTV, detallesPuntuacionTV;
    int opcion = Estatica.getUsuarioEstatico().getPuntuacion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_puntuacion);
        HideUI.setImmersiveMode(this);

        emojiPuntuacionTV = findViewById(R.id.emojiPuntuacionTV);

        switch(opcion) {
            case 0:
                emojiPuntuacionTV.setText("Puntuacion: 0/5");
                break;
            case 1:
                emojiPuntuacionTV.setText("Puntuacion: 1/5");
                break;
            case 2:
                emojiPuntuacionTV.setText("Puntuacion: 2/5");
                break;
            case 3:
                emojiPuntuacionTV.setText("Puntuacion: 3/5");
                break;
            case 4:
                emojiPuntuacionTV.setText("Puntuacion: 4/5");
                break;
            case 5:
                emojiPuntuacionTV.setText("Puntuacion: 5/5");
                break;
            default:
                System.out.println("Opción no válida");
                break;
        }

        detallesPuntuacionTV = findViewById(R.id.detallesPuntuacionTV);

        Puntuacion.obtenerComentarioPorEmail(Estatica.getUsuarioEstatico().getEmail(), new ComentarioCallback(){
            @Override
            public void onComentarioObtenido(String comentario) {
                if (comentario != null) {
                    detallesPuntuacionTV.setText(comentario);
                } else {
                    detallesPuntuacionTV.setText("Sin comentarios");
                }
            }
        });



        ////////////////////////////////////////////////////////////////////////////////////////////
        //ir a gestion
        botonGestionarPuntuacion = findViewById(R.id.botonGestionarPuntuacion);
        botonGestionarPuntuacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Estatica.getUsuarioEstatico().isEsAdmin()) {
                    // Creamos intent para ir a otra actividad
                    intent = new Intent(PuntuacionActivity.this, GestionarPuntuacion.class);

                    // Inicia la actividad de eventos
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Debes de ser administrador para gestionar puntuaciones", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

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