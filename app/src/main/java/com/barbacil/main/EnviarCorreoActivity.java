package com.barbacil.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import connections.InsertCorreoCallback;
import objectClasses.Correo;
import objectClasses.Estatica;

public class EnviarCorreoActivity extends AppCompatActivity {

    Button botonEnviarCorreo, botonVolverABandejaEntrada;
    Intent intent;

    EditText etReceptor, etTituloCorreo, etCuerpoDelCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enviar_correo);
        HideUI.setImmersiveMode(this);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //enviar correo
        botonEnviarCorreo = findViewById(R.id.botonEnviarCorreo);
        botonEnviarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etReceptor = findViewById(R.id.etReceptor);
                etTituloCorreo = findViewById(R.id.etTituloCorreo);
                etCuerpoDelCorreo = findViewById(R.id.etCuerpoDelCorreo);

                String correoreceptor = etReceptor.getText().toString();
                String mensaje = etTituloCorreo.getText().toString();
                String tituloMensaje = etCuerpoDelCorreo.getText().toString();

                Correo correo = new Correo(Estatica.getUsuarioEstatico().getEmail(), correoreceptor, mensaje, tituloMensaje);
                Correo.insertarCorreo(correo, new InsertCorreoCallback() {
                    @Override
                    public void onCorreoInsertCompleted(boolean success) {
                        if (success) {
                            // Inserción exitosa
                            // Realizar alguna acción, como mostrar un mensaje de éxito
                            Toast.makeText(getApplicationContext(), "Correo enviado con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            // Error al insertar el correo
                            // Realizar alguna acción, como mostrar un mensaje de error
                            Toast.makeText(getApplicationContext(), "Error al enviar el correo, comprueba el e-mail del receptor", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        //volver
        botonVolverABandejaEntrada = findViewById(R.id.botonVolverABandejaEntrada);
        botonVolverABandejaEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(EnviarCorreoActivity.this, CorreoActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

    }
}