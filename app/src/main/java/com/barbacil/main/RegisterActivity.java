package com.barbacil.main;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import objectClasses.Usuario;
import connections.Conexion;

public class RegisterActivity extends AppCompatActivity {

    Button botonVolver, botonRegistrarse;
    Intent intentVolver;
    EditText emailET, nombreET, contrasegnaET, edadET;
    RadioGroup grupoGenero;
    RadioButton radioButton;
    String opcionGenero;

    Usuario usuario;



    private final String supabaseUrl = "https://vlbsmlsjguviymvrzeqe.supabase.co";
    private final String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZsYnNtbHNqZ3V2aXltdnJ6ZXFlIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcxMDUzMDU3NywiZXhwIjoyMDI2MTA2NTc3fQ.SbdvAkaVbxXUgH7txb0x5Cnci4wMpyfSK6zqTq_Dqz4";
    Conexion supabaseClient = new Conexion(supabaseUrl, apiKey);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        HideUI.setImmersiveMode(this);

        //seccion boton volver//
        // asociamos variable con boton por id
        botonVolver = findViewById(R.id.backButtonRegister);

        // configuracion del listener para el boton volver (vuelve a la actividad principal)
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creamos intent para ir a otra actividad
                intentVolver = new Intent(RegisterActivity.this, MainActivity.class);

                // Inicia la actividad de registro
                startActivity(intentVolver);
            }
        });
        ///////////////////////////////////////////////

        //seccion boton registrarse (comprueba que todos los campos han sido rellenados correctamente y manda peticion al servidor//
        // asociamos variables con elementos por id
        botonRegistrarse = findViewById(R.id.registerButton);
        emailET = findViewById(R.id.emailETregister);
        nombreET = findViewById(R.id.nameETregister);
        contrasegnaET = findViewById(R.id.passwordETregister);
        edadET = findViewById(R.id.ageETregister);

        //configuracion del listener para comprobar si esta correcto y enviar peticion al servidor
        botonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contenidoEmail = emailET.getText().toString();
                String contenidoNombre = nombreET.getText().toString();
                String contenidoContrasegna = contrasegnaET.getText().toString();
                String contenidoEdadString = edadET.getText().toString();
                int contenidoEdad = Integer.parseInt(contenidoEdadString);
                usuario = new Usuario(contenidoEmail, contenidoNombre, contenidoContrasegna,contenidoEdad,opcionGenero);

                supabaseClient.insertUser("0", contenidoEmail, contenidoNombre, contenidoContrasegna, contenidoEdad, opcionGenero, false, 0, 0, isSuccess -> {
                    runOnUiThread(() -> { // Asegurándonos de que el Toast se muestre en el hilo principal
                        if (isSuccess) {
                            Toast.makeText(RegisterActivity.this, "Usuario insertado con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error al insertar usuario", Toast.LENGTH_SHORT).show();
                        }
                    });
                });


            }
        });
        /////////////////////////////////////////////////////

        //seccion radiogroup, convierte el genero elegido en un String//
        // asociamos variables con elementos por id
        grupoGenero = findViewById(R.id.genderRadioGroup);

        //configuracion del listener para transformar en string la eleccion al pulsarla
        grupoGenero.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = findViewById(checkedId);
                opcionGenero = radioButton.getText().toString();
            }
        });
        //////////////////////////////////////////////////////


    }




}
