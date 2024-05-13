package com.barbacil.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import connections.EventoFetchCallback;
import objectClasses.Estatica;
import objectClasses.Evento;
import objectClasses.Usuario;

public class EventosActivity extends AppCompatActivity {
    TextView volverEVTV,agnadirEventosEVTV;
    Intent intent;

    private ListView listViewEventos;
    private EventoAdapter eventoAdapter;
    private List<Evento> listaEventos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eventos);
        HideUI.setImmersiveMode(this);

        // Obtener la referencia de la ListView
        listViewEventos = findViewById(R.id.listaDeEventosEVTV);

        // Crear un adaptador para los eventos
        listaEventos = new ArrayList<>();
        eventoAdapter = new EventoAdapter(this, listaEventos);

        // Establecer el adaptador en la ListView
        listViewEventos.setAdapter(eventoAdapter);

        // Obtener los eventos y agregarlos al adaptador
        obtenerEventos();



        ////////////////////////////////////////////////////////////////////////////////////////////
        //Volver
        volverEVTV = findViewById(R.id.volverEVTV);
        volverEVTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(EventosActivity.this, UserPageActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Añadir un evento
        agnadirEventosEVTV = findViewById(R.id.agnadirEventosEVTV);
        agnadirEventosEVTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usuario = Estatica.getUsuarioEstatico();
                if(usuario.isEsAdmin()){
                    intent = new Intent(EventosActivity.this, AgnadirEventoActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(EventosActivity.this, "Necesitas ser Administrador para crear eventos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }

    // Método para obtener los eventos desde la base de datos
    public void obtenerEventos() {
        Evento.obtenerEventosPorEmpresa(Estatica.getUsuarioEstatico().getIdEmpresa(), new EventoFetchCallback() {
            @Override
            public void onEventosFetched(List<Evento> eventos) {
                // Limpiar la lista actual de eventos y agregar los nuevos eventos
                listaEventos.clear();
                listaEventos.addAll(eventos);
                // Notificar al adaptador que los datos han cambiado
                eventoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onEventosFetchFailure(String errorMessage) {
                // Manejar el error al recuperar eventos
                Log.e("EventosFetchError", errorMessage);
                // Mostrar un mensaje de error al usuario, por ejemplo:
                Toast.makeText(EventosActivity.this, "Error al recuperar eventos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}