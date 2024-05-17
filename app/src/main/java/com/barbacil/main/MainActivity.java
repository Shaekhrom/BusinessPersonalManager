package com.barbacil.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;

import connections.Conexion;
import connections.UserFetchCallback;
import objectClasses.Estatica;
import objectClasses.Usuario;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    Conexion supabaseClient = new Conexion();

    Button botonIniciarSesion, botonRegistrarse, botonTest;
    EditText emailET, contrasegnaET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HideUI.setImmersiveMode(this);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //(Iniciar sesion 1/2)- Inicio de sesion y establecemos usuario estatico
        botonIniciarSesion= findViewById(R.id.botonLogin);
        emailET = findViewById(R.id.emailLogin);
        contrasegnaET = findViewById(R.id.contrasegnaLogin);

        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el email y la contraseña de los EditText
                String email = emailET.getText().toString();
                String contrasegna = contrasegnaET.getText().toString();

                // Llamar al método obtenerUsuarioPorCredenciales de Usuario
                Usuario.obtenerUsuarioPorCredenciales(email, contrasegna, new UserFetchCallback() {
                    @Override
                    public void onUserFetched(Usuario usuario) {
                        Estatica.setUsuarioEstatico(usuario);
                        Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                        if(Estatica.getUsuarioEstatico().getIdEmpresa().equals("0")){
                            Intent intent = new Intent(MainActivity.this, CompanySelectorActivity.class);
                            startActivity(intent);
                        }else{

                            Intent intent = new Intent(MainActivity.this, UserPageActivity.class);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onUserFetchFailure(String errorMessage) {
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////////////////////////////////////
        //(Registrarse 1/3)-Registrarse
        botonRegistrarse= findViewById(R.id.botonRegistroMain);
        botonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////////////////////////////////////
        //(Test 1/2)-Llama a la clase interna TestConexionClass
        botonTest= findViewById(R.id.botonTest);
        botonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TestConexionClass(MainActivity.this).execute();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    //(Test 2/2)-Clase para probar la conexion a la BBDD
    private static class TestConexionClass extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<MainActivity> activityReference;

        TestConexionClass(MainActivity activity) {
            activityReference = new WeakReference<>(activity);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return false;
            }

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(activity.supabaseClient.getSupabaseUrl() + "/rest/v1/usuario?select=*&limit=1")
                    .addHeader("apikey", activity.supabaseClient.getApiKey())
                    .build();
            try (okhttp3.Response response = client.newCall(request).execute()) {
                return response.isSuccessful();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean servidorDisponible) {
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }


            if (servidorDisponible) {
                Toast.makeText(activity, "Servidor disponible", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Servidor no disponible", Toast.LENGTH_SHORT).show();
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
}
