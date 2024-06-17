package com.barbacil.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import connections.Conexion;
import connections.EmpresaIdCallback;
import connections.EmpresaInsertCallback;
import connections.UsuarioUpdateCallback;
import objectClasses.Empresa;
import objectClasses.Estatica;
import objectClasses.Usuario;

public class CompanyCreatorActivity extends AppCompatActivity {
    Conexion supabaseClient = new Conexion();

    Button buttonCrearEmpresa, buttonVolverCE;

    EditText nombreET, contrasegnaET, sectorET, detallesET;
    String nombreEmpresa,contrasegnaEmpresa,sectorEmpresa,detallesEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_creator);
        HideUI.setImmersiveMode(this);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //(Crear empresa 2/6)-Crear empresa
        buttonCrearEmpresa= findViewById(R.id.buttonCrearEmpresa);
        nombreET = findViewById(R.id.nombreEmpresaET);
        contrasegnaET = findViewById(R.id.contrasegnaEmpresaCE);
        sectorET = findViewById(R.id.sectorEmpresaCE);
        detallesET = findViewById(R.id.detallesEmpresaCE);

        buttonCrearEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos de la empresa de los EditText
                String nombreEmpresa = nombreET.getText().toString();
                String contrasegnaEmpresa = contrasegnaET.getText().toString();
                String sectorEmpresa = sectorET.getText().toString();
                String detallesEmpresa = detallesET.getText().toString();

                // Crear un objeto Empresa con los datos obtenidos
                Empresa empresa = new Empresa(nombreEmpresa, contrasegnaEmpresa, sectorEmpresa, detallesEmpresa);

                // Llamar al método insertarEmpresa de la clase Empresa
                Empresa.insertarEmpresa(empresa, new EmpresaInsertCallback() {
                    @Override
                    public void onCompleted(boolean success) {
                        // La inserción de la empresa ha finalizado
                        if (success) {

                            Toast.makeText(CompanyCreatorActivity.this, "Empresa creada exitosamente", Toast.LENGTH_SHORT).show();
                            // Llamar al método obtenerIdEmpresa de la clase Empresa
                            Empresa.obtenerIdEmpresa(nombreEmpresa, contrasegnaEmpresa, new EmpresaIdCallback() {
                                @Override
                                public void onIdFetched(String idEmpresa) {
                                    Usuario.actualizarIdEmpresa(Estatica.getUsuarioEstatico().getEmail(), Estatica.getUsuarioEstatico().getContrasegna(), idEmpresa, new UsuarioUpdateCallback() {
                                        @Override
                                        public void onUpdateCompleted(boolean success) {
                                            if (success) {
                                                Toast.makeText(CompanyCreatorActivity.this, "ID de empresa actualizado exitosamente", Toast.LENGTH_SHORT).show();
                                                Usuario.actualizarAdmin(Estatica.getUsuarioEstatico().getEmail(), Estatica.getUsuarioEstatico().getContrasegna(), new UsuarioUpdateCallback() {
                                                    @Override
                                                    public void onUpdateCompleted(boolean success) {
                                                        if(success) {
                                                            Usuario usuarioAdmin = Estatica.getUsuarioEstatico();
                                                            usuarioAdmin.setEsAdmin(true);

                                                            Estatica.setUsuarioEstatico(usuarioAdmin);
                                                            Intent intent = new Intent(CompanyCreatorActivity.this, UserPageActivity.class);
                                                            startActivity(intent);
                                                        }else{
                                                            Toast.makeText(CompanyCreatorActivity.this, "Error al establecer Usuario como Administrador", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            } else {
                                                // La actualización falló
                                                Toast.makeText(CompanyCreatorActivity.this, "Error al actualizar el ID de empresa", Toast.LENGTH_SHORT).show();
                                                // Realizar acciones adicionales si es necesario
                                            }
                                        }
                                    });

                                }

                                @Override
                                public void onIdFetchFailure(String errorMessage) {
                                    Toast.makeText(CompanyCreatorActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });


                        } else {
                            // La inserción falló
                            Toast.makeText(CompanyCreatorActivity.this, "Error al crear la empresa", Toast.LENGTH_SHORT).show();
                            // Realizar acciones adicionales si es necesario
                        }
                    }
                });
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////////////////////////////////////
        //Volver
        buttonVolverCE= findViewById(R.id.buttonVolverCE);
        buttonVolverCE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyCreatorActivity.this, CompanySelectorActivity.class);
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////


    }
}