package com.barbacil.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import connections.Registrarse;

public class MainActivity extends AppCompatActivity {

    // Declaracion de variables
    Button botonRegistro, botonTest;
    Intent intentRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // asociamos variable con boton por id
        botonRegistro = findViewById(R.id.registerButtonMain);

        //test
        botonTest = findViewById(R.id.test);

        // configuracion del listener (se activa al hacer click)
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creamos intent para ir a otra actividad
                intentRegistro = new Intent(MainActivity.this, RegisterActivity.class);

                // Inicia la actividad de registro
                startActivity(intentRegistro);
            }
        });




    }
}
