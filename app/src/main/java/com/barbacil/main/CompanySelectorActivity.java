package com.barbacil.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CompanySelectorActivity extends AppCompatActivity {

    //variables
    Button botonCrear;
    Intent intentCrearEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_selector);

        //asignamos id a variables
        botonCrear = findViewById(R.id.botonCrearEmpresa);

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

    }
}