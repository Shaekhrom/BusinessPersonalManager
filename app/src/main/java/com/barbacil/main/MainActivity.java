package com.barbacil.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import connections.Conexion;
import connections.UserInsertCallback;

public class MainActivity extends AppCompatActivity {

    // Declaración de variables
    private final String supabaseUrl = "https://vlbsmlsjguviymvrzeqe.supabase.co";
    private final String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZsYnNtbHNqZ3V2aXltdnJ6ZXFlIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcxMDUzMDU3NywiZXhwIjoyMDI2MTA2NTc3fQ.SbdvAkaVbxXUgH7txb0x5Cnci4wMpyfSK6zqTq_Dqz4";
    Conexion supabaseClient = new Conexion(supabaseUrl, apiKey);

    Button botonRegistro, botonTest, botonLogin;
    Intent intentRegistro, intentLogin;


    EditText editTextEmail, editTextcontrasegna;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asociamos variable con botón por id
        botonRegistro = findViewById(R.id.registerButtonMain);
        botonLogin = findViewById(R.id.botonLogin);

        //asociamos editTexts con sus strings
        editTextEmail = findViewById(R.id.email);

        editTextcontrasegna = findViewById(R.id.contrasegna);



        // Test
        botonTest = findViewById(R.id.test);

        botonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pingDatabaseAsync();
            }
        });



        // Configuración del listener del registro (se activa al hacer click)
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intentRegistro = new Intent(MainActivity.this, RegisterActivity.class);

                // Inicia la actividad de registro
                startActivity(intentRegistro);
            }
        });


        ////////////////////////////////////////////////////////////////////////////////////////////
        // Configuración del listener del login (se activa al hacer click)
        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el email y la contraseña de los EditText
                String email = editTextEmail.getText().toString();
                String contrasegna = editTextcontrasegna.getText().toString();

                // Crear un nuevo hilo para realizar la operación de inicio de sesion
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Llamar al método de inicio de sesión en la clase Conexion
                        boolean inicioSesionExitoso = supabaseClient.iniciarSesion(email, contrasegna);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (inicioSesionExitoso) {
                                    // Iniciar sesion si ha devuelto true
                                    Intent intent = new Intent(MainActivity.this, UserPageActivity.class);
                                    startActivity(intent);
                                } else {
                                    // Mostrar un mensaje de error si el inicio de sesión fallo
                                    Toast.makeText(MainActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////





    }

    ///////////////////////////////////////////////////////////////
    //comprobar disponibilidad de la bbdd
    private void pingDatabaseAsync() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            // Operación de red en un hilo secundario
            final boolean isAvailable = supabaseClient.pingDatabase();

            handler.post(() -> {
                // Actualizar UI en el hilo principal
                if (isAvailable) {
                    Toast.makeText(MainActivity.this, "La base de datos esta disponible", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "La base de datos no esta disponible", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    ///////////////////////////////////////////////////////////////

    //iniciar sesion



}
