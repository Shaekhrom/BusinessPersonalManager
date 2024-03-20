package connections;

import android.os.Looper;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogRecord;
import android.os.Handler;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Registrarse {

    private final String supabaseUrl;
    private final String apiKey;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final OkHttpClient client = new OkHttpClient();


    public Registrarse(String supabaseUrl, String apiKey) {
        this.supabaseUrl = supabaseUrl;
        this.apiKey = apiKey;
    }



    //pingea la base de datos para ver si esta disponible
    public boolean pingDatabase() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(supabaseUrl + "/rest/v1/usuario?select=*&limit=1")
                .addHeader("apikey", apiKey) // O "Authorization", "Bearer tu_api_key" si es necesario
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.code() == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //inserta un usario en la bbdd
    public void insertUser(String idEmpresa, String email, String nombre, String contrasena, int edad, String genero, boolean esAdmin, double salario, double puntuacion, UserInsertCallback callback) {
        executorService.execute(() -> {
            try {
                JSONObject json = new JSONObject();
                json.put("idempresa", idEmpresa);
                json.put("email", email);
                json.put("nombre", nombre);
                json.put("contrasena", contrasena);
                json.put("edad", edad);
                json.put("genero", genero);
                json.put("esadmin", esAdmin);
                json.put("salario", salario);
                json.put("puntuacion", puntuacion);

                String requestBody = json.toString();

                RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), requestBody);
                Request request = new Request.Builder()
                        .url(supabaseUrl + "/rest/v1/usuario")
                        .addHeader("apikey", apiKey)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Prefer", "return=representation")
                        .post(body)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    // Se ejecuta en el hilo de red, planifica una tarea en el hilo principal para ejecutar el callback
                    handler.post(() -> callback.onCompleted(response.isSuccessful()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> callback.onCompleted(false)); // Notifica fallo si se captura una excepción
            }
        });
    }





}
