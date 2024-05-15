package com.barbacil.main;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import objectClasses.Correo;

public class DetallesCorreoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_correo);

        // Obtener el objeto Correo enviado desde la actividad principal
        Correo correo = getIntent().getParcelableExtra("correo");

        // Mostrar los detalles del correo en la interfaz de usuario
        TextView remitenteTextView = findViewById(R.id.remitenteTextView);
        TextView asuntoTextView = findViewById(R.id.asuntoTextView);
        TextView mensajeTextView = findViewById(R.id.mensajeTextView);

        remitenteTextView.setText(correo.getCorreoemisor());
        asuntoTextView.setText(correo.getTitulomensaje());
        mensajeTextView.setText(correo.getMensaje());
    }
}