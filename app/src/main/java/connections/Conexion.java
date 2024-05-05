package connections;

import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
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
    ///////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////
    //inserta una empresa en la bbdd
    public void insertEmpresa(String contenidoNombre, String contenidoContrasegna, String contenidoSector, String contenidoDetalles, UserInsertCallback callback) {
        executorService.execute(() -> {
            try {
                JSONObject json = new JSONObject();
                json.put("nombreempresa", contenidoNombre);
                json.put("contrasenaempresa", contenidoContrasegna);
                json.put("sector", contenidoSector);
                json.put("detalles", contenidoDetalles);


                String requestBody = json.toString();

                RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), requestBody);
                Request request = new Request.Builder()
                        .url(supabaseUrl + "/rest/v1/empresa")
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

    ///////////////////////////////////////////////////////////////
    //devuelve el ID de una empresa
    public static String obtenerIdEmpresa(String nombreEmpresa, String contrasenaEmpresa) {
        OkHttpClient client = new OkHttpClient();

        // Consulta a la base de datos para obtener todas las empresas
        Request request = new Request.Builder()
                .url(supabaseUrl + "/rest/v1/empresa?select=nombreempresa,contrasenaempresa,idempresa")
                .addHeader("apikey", apiKey)
                .build();

        Callable<String> obtenerIdTask = () -> {
            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    // Manejar el caso de respuesta no exitosa
                    return null;
                }

                // Obtener la respuesta y buscar la empresa con el nombre y contraseña proporcionados
                String responseBody = response.body().string();
                JSONArray empresas = new JSONArray(responseBody);

                for (int i = 0; i < empresas.length(); i++) {
                    JSONObject empresa = empresas.getJSONObject(i);
                    String nombre = empresa.getString("nombreempresa");
                    String contra = empresa.getString("contrasenaempresa");
                    String idEmpresa = empresa.getString("idempresa");

                    // Verificar si el nombre y la contraseña coinciden
                    if (nombre.equals(nombreEmpresa) && contra.equals(contrasenaEmpresa)) {
                        return idEmpresa;
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                // Manejar excepciones
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null; // Si no se encuentra coincidencia
        };

        Future<String> future = executorService.submit(obtenerIdTask);
        try {
            return future.get(); // Devolver el resultado del Future
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // Manejar excepciones
            return null;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////
    //devuelve el id de un usuario
    public static String obtenerIdUsuario(String email, String contrasena) {
        OkHttpClient client = new OkHttpClient();

        // Consulta a la base de datos para obtener todos los usuarios
        Request request = new Request.Builder()
                .url(supabaseUrl + "/rest/v1/usuario?select=email,contrasena,idusuario")
                .addHeader("apikey", apiKey)
                .build();

        Callable<String> obtenerIdTask = () -> {
            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    // Manejar el caso de respuesta no exitosa
                    return null;
                }

                // Obtener la respuesta y buscar el usuario con el nombre y contraseña proporcionados
                String responseBody = response.body().string();
                JSONArray usuarios = new JSONArray(responseBody);

                for (int i = 0; i < usuarios.length(); i++) {
                    JSONObject usuario = usuarios.getJSONObject(i);
                    String emailDB = usuario.getString("email");
                    String contrasenaDB = usuario.getString("contrasena");
                    String idUsuario = usuario.getString("idusuario");

                    // Verificar si el nombre y la contraseña coinciden
                    if (email.equals(emailDB) && contrasena.equals(contrasenaDB)) {
                        return idUsuario;
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                // Manejar excepciones
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null; // Si no se encuentra coincidencia
        };

        Future<String> future = executorService.submit(obtenerIdTask);
        try {
            return future.get(); // Devolver el resultado del Future
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // Manejar excepciones
            return null;
        }
    }

    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    public void insertarRelacionEmpresaUsuario(String idEmpresa, String idUsuario, UserInsertCallback callback) {
        executorService.execute(() -> {
            try {
                JSONObject json = new JSONObject();
                json.put("idempresa", idEmpresa);
                json.put("idusuario", idUsuario);

                String requestBody = json.toString();

                RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), requestBody);
                Request request = new Request.Builder()
                        .url(supabaseUrl + "/rest/v1/empresaconusuarios")
                        .addHeader("apikey", apiKey)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Prefer", "return=representation")
                        .post(body)
                        .build();

                try (okhttp3.Response response = client.newCall(request).execute()) {
                    boolean isSuccess = response.isSuccessful();
                    // Llamar al método onCompleted del callback con el resultado de la inserción
                    handler.post(() -> callback.onCompleted(isSuccess));
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> callback.onCompleted(false)); // Notificar fallo si se captura una excepción
            }
        });
    }


    /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////
    //actualizar id empresa de la tabla usuario
    public void actualizarIdEmpresaUsuario(String emailusuario, String contrasenaUsuario, String nuevoIdEmpresa, UserUpdateCallback callback) {
        executorService.execute(() -> {
            try {
                JSONObject json = new JSONObject();
                json.put("idempresa", nuevoIdEmpresa);

                // Construir la URL para la actualización
                String url = supabaseUrl + "/rest/v1/usuario?email=eq." + emailusuario + "&contrasena=eq." + contrasenaUsuario;

                // Construir el cuerpo de la solicitud
                String requestBody = json.toString();

                // Crear la solicitud HTTP PATCH para actualizar la columna idempresa
                RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), requestBody);
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("apikey", apiKey)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Prefer", "return=representation")
                        .patch(body)
                        .build();

                try (okhttp3.Response response = client.newCall(request).execute()) {
                    boolean isSuccess = response.isSuccessful();
                    // Llamar al método onCompleted del callback con el resultado de la actualización
                    handler.post(() -> callback.onCompleted(isSuccess));
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> callback.onCompleted(false)); // Notificar fallo si se captura una excepción
            }
        });
    }





}
