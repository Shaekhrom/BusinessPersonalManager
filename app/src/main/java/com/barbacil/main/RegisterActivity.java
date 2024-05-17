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

import connections.UserInsertCallback;
import objectClasses.Estatica;
import objectClasses.Usuario;
import connections.Conexion;

public class RegisterActivity extends AppCompatActivity {
    Conexion supabaseClient = new Conexion();
    Button botonVolver, botonRegistrarse;
    EditText emailET, nombreET, contrasegnaET, edadET;
    String email, nombre, contrasegna,edad;
    int edadNumerica;
    RadioGroup generoRadioGroup;
    RadioButton generoRadioButton;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        handler = new Handler(Looper.getMainLooper());
        HideUI.setImmersiveMode(this);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //(Registrarse 2/3)-Registrarse
        botonRegistrarse= findViewById(R.id.botonRegistrarseRegister);
        emailET = findViewById(R.id.emailETregister);
        nombreET = findViewById(R.id.nameETregister);
        contrasegnaET = findViewById(R.id.passwordETregister);
        edadET = findViewById(R.id.ageETregister);
        edad = edadET.getText().toString();
        generoRadioGroup = findViewById(R.id.genderRadioGroup);

        botonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailET.getText().toString();
                nombre = nombreET.getText().toString();
                contrasegna = contrasegnaET.getText().toString();
                edad = edadET.getText().toString();
                edadNumerica = Integer.parseInt(edad);
                int selectedRadioButtonId = generoRadioGroup.getCheckedRadioButtonId();
                generoRadioButton = findViewById(selectedRadioButtonId);
                String genero = generoRadioButton.getText().toString();

                // Creamos un nuevo usuario
                Usuario usuario = new Usuario("0", email, nombre, contrasegna, edadNumerica, genero, false, 0.0,0);
                Estatica.setUsuarioEstatico(usuario);

                // Llamamos al método insertarUsuarioEnBaseDeDatos con el callback
                Usuario.insertarUsuarioEnBaseDeDatos(usuario, new UserInsertCallback() {
                    @Override
                    public void onCompleted(boolean success) {
                        if (success) {
                            // La inserción del usuario fue exitosa
                            // Aquí puedes realizar acciones adicionales si es necesario
                            Intent intent = new Intent(RegisterActivity.this, CompanySelectorActivity.class);
                            startActivity(intent);
                        } else {
                            // La inserción del usuario falló
                            Toast.makeText(RegisterActivity.this, "Error al insertar usuario en la base de datos.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////////////////////////////////////
        //Volver
        botonVolver= findViewById(R.id.backButtonRegister);
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////


    }
}
