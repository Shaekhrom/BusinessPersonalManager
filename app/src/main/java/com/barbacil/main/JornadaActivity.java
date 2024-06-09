package com.barbacil.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import connections.JornadaInsertCallback;
import connections.JornadaListCallback;
import connections.JornadaUpdateCallback;
import connections.VerificacionCallback;
import objectClasses.Estatica;
import objectClasses.Jornada;

public class JornadaActivity extends AppCompatActivity {
    Button botonVolverRJ, botonRegistrarInicio,botonRegistrarFinal;
    Intent intent;
    TextView textViewHora, textViewFecha;

    ImageView verTodasJornadas;
    Handler handlerFechaYHora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jornada);
        HideUI.setImmersiveMode(this);

        textViewHora = findViewById(R.id.textViewHora);
        textViewFecha = findViewById(R.id.textViewFecha);
        handlerFechaYHora = new Handler();
        handlerFechaYHora.post(runnable);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Volver
        botonVolverRJ = findViewById(R.id.botonVolverRJ);
        botonVolverRJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(JornadaActivity.this, UserPageActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //insertar jornada
        botonRegistrarInicio = findViewById(R.id.botonRegistrarInicio);
        botonRegistrarInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDate = getCurrentDate();
                String currentTime = getCurrentTime();
                String idUsuario = Estatica.getUsuarioEstatico().getId();
                // Crear una instancia de Jornada con los datos necesarios
                Jornada jornada = new Jornada(idUsuario,currentDate, currentTime, "not_registered");

                // Llamar al método insertarJornada y pasar un callback para manejar la respuesta
                Jornada.insertarJornada(jornada, new JornadaInsertCallback() {
                    @Override
                    public void onJornadaInserted(boolean success) {
                        if (success) {
                            Toast.makeText(JornadaActivity.this, "Jornada registrada con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(JornadaActivity.this, "Error, inicio de jornada ya registrado o fallo al conectar con el servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //registrar fin de jornada
        botonRegistrarFinal = findViewById(R.id.botonRegistrarFinal);
        botonRegistrarFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener idUsuario, fecha y horaFinal
                String idUsuario = Estatica.getUsuarioEstatico().getId();
                String fecha = getCurrentDate();
                String horaFinal = getCurrentTime();

                // Verificar si la hora final ya está registrada correctamente
                Jornada.verificarHoraFinalNoRegistrada(idUsuario, fecha, new VerificacionCallback() {
                    @Override
                    public void onVerificacionCompleted(boolean success) {
                        if (success) {
                            // La hora final ya está registrada como "not_registered", por lo que podemos llamar a registrarHoraFinal
                            Jornada.registrarHoraFinal(idUsuario, fecha, horaFinal, new JornadaUpdateCallback() {
                                @Override
                                public void onJornadaUpdated(boolean success) {
                                    if (success) {
                                        Toast.makeText(JornadaActivity.this, "Final de jornada registrada con éxito", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(JornadaActivity.this, "Error al registrar fin de jornada", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(JornadaActivity.this, "Error, fin de jornada ya registrada o falta de registro de inicio de jornada", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        //mostrar todas las jornadas por email
        verTodasJornadas = findViewById(R.id.verTodasJornadas);
        verTodasJornadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idusuario = Estatica.getUsuarioEstatico().getId();
                Jornada.obtenerJornadasPorEmail(idusuario, new JornadaListCallback() {
                    @Override
                    public void onJornadaListReceived(ArrayList<Jornada> jornadasList) {
                        Intent intent = new Intent(JornadaActivity.this, MostrarJornadasActivity.class);
                        intent.putExtra("jornadasList", jornadasList);
                        startActivity(intent);
                    }
                });
            }
        });

        ////////////////////


    }
    ////////////////////////////////////////////////////////////////////////////////////////////
    //manejo de la hora
    // Runnable para actualizar el reloj
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateTime();
            updateDate();
            handlerFechaYHora.postDelayed(this, 1000); // Programar la próxima actualización
        }
    };

    // Método para actualizar la hora en el TextView
    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        String currentTime = sdf.format(new Date());
        textViewHora.setText("Hora: " + currentTime);
    }

    private void updateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Madrid")); // Establecer la zona horaria de España
        String currentDate = sdf.format(new Date());
        textViewFecha.setText("Fecha: " + currentDate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerFechaYHora.removeCallbacks(runnable);
    }

    // Método para obtener la hora actual como String
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        return sdf.format(new Date());
    }

    // Método para obtener la fecha actual como String
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        return sdf.format(new Date());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
}