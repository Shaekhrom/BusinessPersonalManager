// MostrarJornadasActivity.java
package com.barbacil.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import objectClasses.Jornada;
import java.util.ArrayList;

public class MostrarJornadasActivity extends AppCompatActivity {

    ListView listViewJornadas;
    ArrayList<Jornada> jornadasList;
    Button botonVolverAJornadas;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_jornadas);
        HideUI.setImmersiveMode(this);

        listViewJornadas = findViewById(R.id.listViewJornadas);

        // Recibir la lista de jornadas del intent
        jornadasList = (ArrayList<Jornada>) getIntent().getSerializableExtra("jornadasList");

        // Convertir la lista de jornadas a una lista de strings para mostrar en el ListView
        ArrayList<String> jornadasStringList = new ArrayList<>();
        for (Jornada jornada : jornadasList) {
            jornadasStringList.add("Fecha: " + jornada.getFecha() + " - Inicio: " + jornada.getHorainicio() + " - Final: " + jornada.getHorafinal());
        }

        // Crear y configurar el adaptador para el ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_jornada, R.id.jornadaItemTextView, jornadasStringList);
        listViewJornadas.setAdapter(adapter);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Volver
        botonVolverAJornadas = findViewById(R.id.botonVolverAJornadas);
        botonVolverAJornadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(MostrarJornadasActivity.this, JornadaActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }
}
