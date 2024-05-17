package com.barbacil.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import connections.PuntuacionInsertCallback;
import connections.PuntuacionUpdateCallback;
import connections.UsuarioUpdateCallback;
import objectClasses.Estatica;
import objectClasses.Puntuacion;
import objectClasses.Usuario;

public class GestionarPuntuacion extends AppCompatActivity {
    Button botonVolverGestionarPuntuacion, botonGuardarCambiosGestionarPuntuacion;
    int puntuacion;
    Intent intent;

    String idEmpresa = Estatica.getUsuarioEstatico().getIdEmpresa();

    EditText emailUsuarioGestionarPuntuacion, detallesGestionarPuntuacioon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestionar_puntuacion);
        HideUI.setImmersiveMode(this);



        Spinner mySpinner = findViewById(R.id.spinnerGestionarPuntuacion);
        // Crear un ArrayAdapter usando la lista de opciones y un diseño de spinner por defecto
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerGestionarPuntuacion, android.R.layout.simple_spinner_item);

        // Especificar el diseño a usar cuando la lista de opciones aparece
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplicar el adaptador al spinner
        mySpinner.setAdapter(adapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el elemento seleccionado
                String selectedItem = parent.getItemAtPosition(position).toString();

                // Ejecutar código en función del elemento seleccionado
                switch (selectedItem) {
                    case "0 estrellas":
                        puntuacion = 0;
                        break;
                    case "1 estrellas":
                        puntuacion = 1;
                        break;
                    case "2 estrellas":
                        puntuacion = 2;
                        break;
                    case "3 estrellas":
                        puntuacion = 3;
                        break;
                    case "4 estrellas":
                        puntuacion = 4;
                        break;
                    case "5 estrellas":
                        puntuacion = 5;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        //guardar cambios en la BBDD
        botonGuardarCambiosGestionarPuntuacion= findViewById(R.id.botonGuardarCambiosGestionarPuntuacion);

        emailUsuarioGestionarPuntuacion = findViewById(R.id.emailUsuarioGestionarPuntuacion);
        detallesGestionarPuntuacioon = findViewById(R.id.detallesGestionarPuntuacioon);
        botonGuardarCambiosGestionarPuntuacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailUsuarioGestionarPuntuacion.getText().toString();
                String comentario = detallesGestionarPuntuacioon.getText().toString();

                Puntuacion puntuacionAInsertar = new Puntuacion(email, puntuacion, comentario, idEmpresa);
                Puntuacion.insertarPuntuacionEnBaseDeDatos(puntuacionAInsertar, new PuntuacionInsertCallback() {
                    @Override
                    public void onCompleted(boolean success) {
                        if (success) {
                            mostrarToast("Puntuación guardada correctamente");
                            Usuario.actualizarPuntuacionPorEmail(email, puntuacion, new UsuarioUpdateCallback() {
                                @Override
                                public void onUpdateCompleted(boolean success) {
                                    if (success) {
                                        mostrarToast("Puntuación del usuario actualizada correctamente");
                                    } else {
                                        mostrarToast("Error al actualizar la puntuación del usuario");
                                    }
                                }
                            });

                        } else {
                            Puntuacion.actualizarPuntuacionEnBaseDeDatos(puntuacionAInsertar, new PuntuacionUpdateCallback() {
                                @Override
                                public void onUpdateCompleted(boolean success) {
                                    if (success) {
                                        // La puntuación se actualizó correctamente
                                        mostrarToast("Puntuación guardada correctamente");
                                        Usuario.actualizarPuntuacionPorEmail(email, puntuacion, new UsuarioUpdateCallback() {
                                            @Override
                                            public void onUpdateCompleted(boolean success) {
                                                if (success) {
                                                    mostrarToast("Puntuación del usuario actualizada correctamente");
                                                } else {
                                                    mostrarToast("Error al actualizar la puntuación del usuario");
                                                }
                                            }
                                        });
                                    } else {
                                        // Hubo un error al actualizar la puntuación
                                        mostrarToast("Error al guardar la puntuación");
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Volver
        botonVolverGestionarPuntuacion = findViewById(R.id.botonVolverGestionarPuntuacion);
        botonVolverGestionarPuntuacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(GestionarPuntuacion.this, PuntuacionActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        }
    private void mostrarToast(String mensaje) {
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
    }
