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

public class CompanySelectorActivity extends AppCompatActivity {

    //variables
    Button botonCrear, botonVolverJE, botonUnirseAEmpresa;
    Intent intentCrearEmpresa, intentVolver, intentUserPage;

    EditText nombreEmpresaU, contrasegnaEmpresaU;

    private final String supabaseUrl = "https://vlbsmlsjguviymvrzeqe.supabase.co";
    private final String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZsYnNtbHNqZ3V2aXltdnJ6ZXFlIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcxMDUzMDU3NywiZXhwIjoyMDI2MTA2NTc3fQ.SbdvAkaVbxXUgH7txb0x5Cnci4wMpyfSK6zqTq_Dqz4";
    Conexion supabaseClient = new Conexion(supabaseUrl, apiKey);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_selector);

        HideUI.setImmersiveMode(this);

        //asignamos id a variables
        botonVolverJE = findViewById(R.id.botonVolverJE);
        botonCrear = findViewById(R.id.botonCrearEmpresa);
        botonUnirseAEmpresa = findViewById(R.id.botonUnirseAEmpresa);

        /////////////////////////////////////////////////////////////////////////
        //metodo al pulsar el boton Crear empresa
        botonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentCrearEmpresa = new Intent(CompanySelectorActivity.this, CompanyCreatorActivity.class);
                startActivity(intentCrearEmpresa);
            }
        });
        /////////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////////
        //metodo al pulsar el boton volver
        botonVolverJE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentVolver = new Intent(CompanySelectorActivity.this, MainActivity.class);
                startActivity(intentVolver);
            }
        });
        /////////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////////
        //metodo unirse a empresa


        botonUnirseAEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreEmpresaU = findViewById(R.id.editTextNombre);
                contrasegnaEmpresaU = findViewById(R.id.editTextContrasegna);

                String contenidoNombre = nombreEmpresaU.getText().toString();
                String contenidoContrasegna = contrasegnaEmpresaU.getText().toString();

                String idEmpresa = Conexion.obtenerIdEmpresa(contenidoNombre, contenidoContrasegna);
                Usuario user = MainActivity.conseguirUsuario();




                supabaseClient.actualizarIdEmpresaUsuario(user.getEmail(), user.getContrasegna(),idEmpresa, isSuccess -> {
                    runOnUiThread(() -> { // Asegurándonos de que el Toast se muestre en el hilo principal
                        if (isSuccess) {
                            Toast.makeText(CompanySelectorActivity.this, "Unión realizada con éxito", Toast.LENGTH_SHORT).show();
                            intentUserPage = new Intent(CompanySelectorActivity.this, UserPageActivity.class);
                            startActivity(intentUserPage);
                        } else {
                            Toast.makeText(CompanySelectorActivity.this, "Error al unirse a una empresa", Toast.LENGTH_SHORT).show();
                        }
                    });
                });


            }
        });

        /////////////////////////////////////////////////////////////////////////

    }
}