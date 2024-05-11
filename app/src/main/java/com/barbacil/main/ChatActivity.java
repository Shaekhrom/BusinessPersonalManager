package com.barbacil.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {
    TextView volverCT;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chatmain);
        HideUI.setImmersiveMode(this);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Volver
        volverCT = findViewById(R.id.volverCT);
        volverCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos intent para ir a otra actividad
                intent = new Intent(ChatActivity.this, UserPageActivity.class);

                // Inicia la actividad de eventos
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }
}