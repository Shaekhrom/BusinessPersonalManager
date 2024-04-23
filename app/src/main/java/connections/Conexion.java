package connections;

import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class Conexion {
    private static String supabaseUrl;
    private static String apiKey;
    private static boolean registradoEnEmpresa = false;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final OkHttpClient client = new OkHttpClient();

    private boolean authenticated;

    public Conexion(String supabaseUrl, String apiKey) {
        this.supabaseUrl = supabaseUrl;
        this.apiKey = apiKey;
    }


    ////////////////////////////////////////////////////////////////
    //pingea la base de datos para ver si esta disponible
    public boolean pingDatabase() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(supabaseUrl + "/rest/v1/usuario?select=*&limit=1")
                .addHeader("apikey", apiKey) // O "Authorization", "Bearer tu_api_key" si es necesario
                .build();

        try (okhttp3.Response response = client.newCall(request).execute()) {
            return response.code() == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    ////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////
    //inicio de sesion, comprobacion
    public static boolean iniciarSesion(String email, String contrasena) {
        OkHttpClient client = new OkHttpClient();

        // Consulta a la base de datos para obtener todos los usuarios
        Request request = new Request.Builder()
                .url(supabaseUrl + "/rest/v1/usuario?select=email,contrasena,idempresa")
                .addHeader("apikey", apiKey)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                // Manejar el caso de respuesta no exitosa
                return false;
            }

            // Obtener la respuesta y verificar las credenciales
            String responseBody = response.body().string();
            JSONArray usuarios = new JSONArray(responseBody);

            for (int i = 0; i < usuarios.length(); i++) {
                JSONObject usuario = usuarios.getJSONObject(i);
                String emailDB = usuario.getString("email");
                String contrasenaDB = usuario.getString("contrasena");
                String idEmpresa = usuario.getString("idempresa");

                // Verificar si las credenciales coinciden
                if (email.equals(emailDB) && contrasena.equals(contrasenaDB)) {
                    if (idEmpresa.equals("0")){
                        registradoEnEmpresa = false;
                    }else{
                        registradoEnEmpresa = true;
                    }
                    return true; // Las credenciales son válidas
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false; // Manejar excepciones
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return false; // Si no se encuentra coincidencia
    }


    ////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////////////////////////
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

                try (okhttp3.Response response = client.newCall(request).execute()) {
                    // Se ejecuta en el hilo de red, planifica una tarea en el hilo principal para ejecutar el callback
                    handler.post(() -> callback.onCompleted(response.isSuccessful()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> callback.onCompleted(false)); // Notifica fallo si se captura una excepción
            }
        });
    }
    ////////////////////////////////////////////////////////////////



    //////////////////////////////////////////////////////////////
    //si el String id de la BBDD del usuario es igual a 0 este metodo devolvera false, sino true
    public static boolean empresaTrigger(){
        if (registradoEnEmpresa){
            return true;
        }else{
            return false;
        }
    }




}
