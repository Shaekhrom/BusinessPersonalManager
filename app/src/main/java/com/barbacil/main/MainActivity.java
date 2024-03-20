package com.barbacil.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import connections.Registrarse;

public class MainActivity extends AppCompatActivity {

    // Declaración de variables
    private final String supabaseUrl = "https://vlbsmlsjguviymvrzeqe.supabase.co";
    private final String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZsYnNtbHNqZ3V2aXltdnJ6ZXFlIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcxMDUzMDU3NywiZXhwIjoyMDI2MTA2NTc3fQ.SbdvAkaVbxXUgH7txb0x5Cnci4wMpyfSK6zqTq_Dqz4";
    Registrarse supabaseClient = new Registrarse(supabaseUrl, apiKey);

    Button botonRegistro, botonTest;
    Intent intentRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asociamos variable con botón por id
        botonRegistro = findViewById(R.id.registerButtonMain);

        // Test
        botonTest = findViewById(R.id.test);

        botonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pingDatabaseAsync();
            }
        });

        // Configuración del listener (se activa al hacer click)
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intentRegistro = new Intent(MainActivity.this, RegisterActivity.class);

                // Inicia la actividad de registro
                startActivity(intentRegistro);
            }
        });
    }

    private void pingDatabaseAsync() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            // Operación de red en un hilo secundario
            final boolean isAvailable = supabaseClient.pingDatabase();

            handler.post(() -> {
                // Actualizar UI en el hilo principal
                if (isAvailable) {
                    System.out.println("La base de datos está disponible.");
                } else {
                    System.out.println("No se pudo establecer conexión con la base de datos.");
                }
            });
        });
    }
}
