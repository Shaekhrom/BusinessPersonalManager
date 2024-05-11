package com.barbacil.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import connections.Conexion;
import connections.EmpresaFetchCallback;
import connections.UsuarioUpdateCallback;
import objectClasses.Empresa;
import objectClasses.Estatica;
import objectClasses.Usuario;

public class CompanySelectorActivity extends AppCompatActivity {
    Conexion supabaseClient = new Conexion();

    Button botonUnirseAEmpresa, botonCrearEmpresaJE, botonVolverJE;
    EditText nombreEmpresaUE, contrasegnaEmpresaUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_selector);

        HideUI.setImmersiveMode(this);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //(Unirse a empresa 1/3)-Unirse a empresa
        botonUnirseAEmpresa= findViewById(R.id.asdasd);
        nombreEmpresaUE = findViewById(R.id.nombreEmpresaUE);
        contrasegnaEmpresaUE = findViewById(R.id.contrasegnaEmpresaUE);

        botonUnirseAEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreEmpresa = nombreEmpresaUE.getText().toString();
                String contrasegnaEmpresa = contrasegnaEmpresaUE.getText().toString();

                // Llamar al método obtenerDatosEmpresa
                Empresa.obtenerDatosEmpresa(nombreEmpresa, contrasegnaEmpresa, new EmpresaFetchCallback() {
                    @Override
                    public void onEmpresaFetched(Empresa empresa) {
                        Estatica.setEmpresaEstatica(empresa);
                        Usuario.actualizarIdEmpresa(Estatica.getUsuarioEstatico().getEmail(), Estatica.getUsuarioEstatico().getContrasegna(), empresa.getId(), new UsuarioUpdateCallback() {
                            @Override
                            public void onUpdateCompleted(boolean success) {
                                // La actualización del ID de empresa ha finalizado
                                if (success) {
                                    // La actualización fue exitosa
                                    Toast.makeText(CompanySelectorActivity.this, "ID de empresa actualizado exitosamente", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CompanySelectorActivity.this, UserPageActivity.class);
                                    startActivity(intent);
                                } else {
                                    // La actualización falló
                                    Toast.makeText(CompanySelectorActivity.this, "Error al actualizar el ID de empresa", Toast.LENGTH_SHORT).show();
                                    // Realizar acciones adicionales si es necesario
                                }
                            }
                        });
                    }

                    @Override
                    public void onEmpresaFetchFailure(String errorMessage) {

                        Toast.makeText(CompanySelectorActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //(Crear empresa 1/5)-Crear empresa
        botonCrearEmpresaJE= findViewById(R.id.botonCrearEmpresaJE);
        botonCrearEmpresaJE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanySelectorActivity.this, CompanyCreatorActivity.class);
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Volver
        botonVolverJE= findViewById(R.id.botonVolverPT);
        botonVolverJE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanySelectorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

    }
}