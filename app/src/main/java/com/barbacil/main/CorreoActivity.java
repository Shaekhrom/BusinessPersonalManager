package com.barbacil.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import connections.ObtenerCorreosCallback;
import objectClasses.Correo;
import objectClasses.Estatica;

public class CorreoActivity extends AppCompatActivity {
    private CorreoAdapter adapter;
    private Handler handler;
    TextView volverCT, agnadirCorreoCT;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_correosmain);
        HideUI.setImmersiveMode(this);
        handler = new Handler(Looper.getMainLooper());
        // Crear un ArrayList para almacenar los correos
        ArrayList<Correo> correos = new ArrayList<>();

        adapter = new CorreoAdapter(correos);


        // Llamar al método obtenerCorreosPorReceptor
        Correo.obtenerCorreosPorReceptor(Estatica.getUsuarioEstatico().getEmail(), new ObtenerCorreosCallback() {
            @Override
            public void onCorreosObtenidos(ArrayList<Correo> correosObtenidos, String errorMessage) {
                if (errorMessage != null) {
                    Log.e("CorreoActivity", "Error al obtener correos: " + errorMessage);
                } else {
                    Log.d("CorreoActivity", "Correos obtenidos correctamente: " + correosObtenidos.size());
                    correos.clear();
                    correos.addAll(correosObtenidos);
                    adapter.notifyDataSetChanged();
                }
            }
        });




        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Configurar el listener de clic en el adaptador
        adapter.setOnItemClickListener(new CorreoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Correo correo) {
                // Al hacer clic en un correo, abrir la actividad de detalles del correo
                Intent intent = new Intent(CorreoActivity.this, DetallesCorreoActivity.class);
                intent.putExtra("correo", correo); // Pasar el objeto Correo a la actividad de detalles del correo
                startActivity(intent);
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Volver
        volverCT = findViewById(R.id.volverCT);
        volverCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(CorreoActivity.this, UserPageActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //ir a enviar correo
        agnadirCorreoCT = findViewById(R.id.agnadirCorreoCT);
        agnadirCorreoCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(CorreoActivity.this, EnviarCorreoActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }
}