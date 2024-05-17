package com.barbacil.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import objectClasses.Correo;

public class DetallesCorreoActivity extends AppCompatActivity {

    Button botonVolverDetallesCorreo;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_correo);
        HideUI.setImmersiveMode(this);

        // Obtener el objeto Correo enviado desde la actividad principal
        Correo correo = getIntent().getParcelableExtra("correo");

        // Mostrar los detalles del correo en la interfaz de usuario
        TextView remitenteTextView = findViewById(R.id.remitenteTextView);
        TextView asuntoTextView = findViewById(R.id.asuntoTextView);
        TextView mensajeTextView = findViewById(R.id.mensajeTextView);

        remitenteTextView.setText(correo.getCorreoemisor());
        asuntoTextView.setText(correo.getTitulomensaje());
        mensajeTextView.setText(correo.getMensaje());

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Volver
        botonVolverDetallesCorreo= findViewById(R.id.botonVolverDetallesCorreo);
        botonVolverDetallesCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallesCorreoActivity.this, CorreoActivity.class);
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }
}