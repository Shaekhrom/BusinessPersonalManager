package com.barbacil.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import connections.Conexion;
import objectClasses.Empresa;
import objectClasses.Usuario;

public class CompanyCreatorActivity extends AppCompatActivity {

    Button volverCE, buttonCrearEmpresa;
    Intent volverAtras,intentUserPage;

    EditText nombreEmpresaET, contrasegnaEmpresaCE, sectorEmpresaCE, detallesEmpresaCE;

    private final String supabaseUrl = "https://vlbsmlsjguviymvrzeqe.supabase.co";
    private final String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZsYnNtbHNqZ3V2aXltdnJ6ZXFlIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcxMDUzMDU3NywiZXhwIjoyMDI2MTA2NTc3fQ.SbdvAkaVbxXUgH7txb0x5Cnci4wMpyfSK6zqTq_Dqz4";
    Conexion supabaseClient = new Conexion(supabaseUrl, apiKey);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_creator);

        HideUI.setImmersiveMode(this);

        volverCE = findViewById(R.id.buttonVolverCE);

        /////////////////////////////////////////////////////////////////////////
        //metodo al pulsar el boton volver, vuelve a selector de empresas
        volverCE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volverAtras = new Intent(CompanyCreatorActivity.this, CompanySelectorActivity.class);
                startActivity(volverAtras);
            }
        });
        /////////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////////
        //metodo crear empresa

        //asignamos variables por id
        buttonCrearEmpresa = findViewById(R.id.buttonCrearEmpresa);

        nombreEmpresaET = findViewById(R.id.nombreEmpresaET);
        contrasegnaEmpresaCE = findViewById(R.id.contrasegnaEmpresaCE);
        sectorEmpresaCE = findViewById(R.id.sectorEmpresaCE);
        detallesEmpresaCE = findViewById(R.id.detallesEmpresaCE);

        buttonCrearEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contenidoNombre = nombreEmpresaET.getText().toString();
                String contenidoContrasegna = contrasegnaEmpresaCE.getText().toString();
                String contenidoSector = sectorEmpresaCE.getText().toString();
                String contenidoDetalles = detallesEmpresaCE.getText().toString();

                Empresa empresa = new Empresa(contenidoNombre, contenidoContrasegna, contenidoSector,contenidoDetalles);

                supabaseClient.insertEmpresa(contenidoNombre, contenidoContrasegna, contenidoSector, contenidoDetalles, isSuccess -> {
                    runOnUiThread(() -> { // Asegurándonos de que el Toast se muestre en el hilo principal
                        if (isSuccess) {
                            Toast.makeText(CompanyCreatorActivity.this, "Empresa creada con éxito", Toast.LENGTH_SHORT).show();
                            intentUserPage = new Intent(CompanyCreatorActivity.this, UserPageActivity.class);
                            startActivity(intentUserPage);
                        } else {
                            Toast.makeText(CompanyCreatorActivity.this, "Error al crear una empresa", Toast.LENGTH_SHORT).show();
                        }
                    });
                });


            }
        });


        /////////////////////////////////////////////////////////////////////////

    }
}