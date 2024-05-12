package com.barbacil.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class AgnadirEventoActivity extends AppCompatActivity {
    Button agnadirEventoBotonVolver;
    EditText agnadirEventoFechaEventoDate;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agnadir_evento);
        HideUI.setImmersiveMode(this);
        //datepicker para la fecha
        agnadirEventoFechaEventoDate = findViewById(R.id.agnadirEventoFechaEventoDate);
        agnadirEventoFechaEventoDate.setOnClickListener(v -> showDatePickerDialog());

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
    }

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
}