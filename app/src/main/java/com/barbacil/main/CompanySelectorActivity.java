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
    Conexion supabaseClient = new Conexion();

    Button botonUnirseAEmpresa, botonCrearEmpresaJE, botonVolverJE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_selector);

        HideUI.setImmersiveMode(this);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //(Unirse a empresa 1/3)-Unirse a empresa
        botonUnirseAEmpresa= findViewById(R.id.botonUnirseAEmpresa);
        botonUnirseAEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanySelectorActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        //(Crear empresa 1/3)-Crear empresa
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
        botonVolverJE= findViewById(R.id.botonVolverJE);
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