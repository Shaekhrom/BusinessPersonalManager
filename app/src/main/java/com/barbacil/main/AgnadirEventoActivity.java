package com.barbacil.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import connections.EventoInsertCallback;
import connections.EventoDeleteCallback;
import objectClasses.Estatica;
import objectClasses.Evento;

public class AgnadirEventoActivity extends AppCompatActivity {
    Button agnadirEventoBotonVolver, agnadirEventoBotonAgnadir, botonBorrarEventoGE;
    EditText agnadirEventoNombreEventoET, agnadirEventoDetallesET, agnadirEventoFechaEventoDate, borrarEventoGEET;
    DatePickerDialog datePickerDialog;
    Date fechaEvento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agnadir_evento);
        HideUI.setImmersiveMode(this);

        agnadirEventoNombreEventoET = findViewById(R.id.agnadirEventoNombreEventoET);
        agnadirEventoDetallesET = findViewById(R.id.agnadirEventoDetallesET);

        //datepicker para la fecha
        agnadirEventoFechaEventoDate = findViewById(R.id.agnadirEventoFechaEventoDate);
        agnadirEventoFechaEventoDate.setOnClickListener(v -> showDatePickerDialog());

        borrarEventoGEET = findViewById(R.id.borrarEventoGEET);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Volver
        agnadirEventoBotonVolver= findViewById(R.id.agnadirEventoBotonVolver);
        agnadirEventoBotonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgnadirEventoActivity.this, EventosActivity.class);
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //agnadir evento
        agnadirEventoBotonAgnadir= findViewById(R.id.agnadirEventoBotonAgnadir);
        agnadirEventoBotonAgnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos objeto evento
                String nombreEvento = agnadirEventoNombreEventoET.getText().toString();
                String detallesEvento = agnadirEventoDetallesET.getText().toString();
                String fechaEvento = agnadirEventoFechaEventoDate.getText().toString();
                Evento evento = new Evento(nombreEvento, detallesEvento, fechaEvento, Estatica.getUsuarioEstatico().getIdEmpresa());

                // Insertar el evento en la base de datos
                Evento.insertarEvento(evento, new EventoInsertCallback() {
                    @Override
                    public void onEventoInserted(boolean success) {
                        if (success) {
                            // El evento se insertó con éxito
                            Toast.makeText(AgnadirEventoActivity.this, "Evento añadido correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            // Hubo un error al insertar el evento
                            Toast.makeText(AgnadirEventoActivity.this, "Error al añadir el evento", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //borrar evento
        botonBorrarEventoGE= findViewById(R.id.botonBorrarEventoGE);
        botonBorrarEventoGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreABorrar = borrarEventoGEET.getText().toString();

                Evento.borrarEventoPorNombreYEmpresa(nombreABorrar, Estatica.getUsuarioEstatico().getIdEmpresa(), new EventoDeleteCallback() {
                    @Override
                    public void onEventoDeleted(boolean success) {
                        if (success) {
                            // El evento se borró exitosamente
                            Toast.makeText(AgnadirEventoActivity.this, "Evento borrado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            // Hubo un error al borrar el evento
                            Toast.makeText(AgnadirEventoActivity.this, "Error al borrar el método", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
    //metodo para el datePicker
    private void showDatePickerDialog() {

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Crea una nueva instancia de DatePickerDialog y configúrala con la fecha actual
        datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth1) -> {
                    // Cuando el usuario seleccione una fecha, actualiza el texto del EditText
                    String selectedDate = dayOfMonth1 + "/" + (monthOfYear + 1) + "/" + year1;
                    agnadirEventoFechaEventoDate.setText(selectedDate);
                }, year, month, dayOfMonth);
        // Configura la ventana del diálogo para que use el modo inmersivo
        datePickerDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        datePickerDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Muestra el DatePickerDialog
        datePickerDialog.show();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
}