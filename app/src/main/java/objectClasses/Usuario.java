package objectClasses;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import connections.Conexion;
import connections.UserFetchCallback;
import connections.UserInsertCallback;
import connections.UsuarioUpdateCallback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Usuario {
    static Conexion supabaseClient = new Conexion();
    //atributos de usuario
    private String id;
    private String idEmpresa;
    private String email;
    private String nombre;
    private String contrasegna;
    private int edad;
    private String genero;
    private boolean esAdmin;
    private double salario;
    private double puntuacion;
    private static Handler handler;


    //constructors


    public Usuario(String idEmpresa, String email, String nombre, String contrasegna, int edad, String genero, boolean esAdmin, double salario, double puntuacion) {
        this.idEmpresa = idEmpresa;
        this.email = email;
        this.nombre = nombre;
        this.contrasegna = contrasegna;
        this.edad = edad;
        this.genero = genero;
        this.esAdmin = esAdmin;
        this.salario = salario;
        this.puntuacion = puntuacion;
    }

    //toString

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", idEmpresa=" + idEmpresa +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", contrasegna='" + contrasegna + '\'' +
                ", edad=" + edad +
                ", genero='" + genero + '\'' +
                ", esAdmin=" + esAdmin +
                ", salario=" + salario +
                ", puntuacion=" + puntuacion +
                '}';
    }


    //gettersAndSetters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasegna() {
        return contrasegna;
    }

    public void setContrasegna(String contrasegna) {
        this.contrasegna = contrasegna;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public boolean isEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //(Registrarse 3/3)insertar ususario
    public static void insertarUsuarioEnBaseDeDatos(Usuario usuario, UserInsertCallback callback) {
        handler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    json.put("idempresa", usuario.getIdEmpresa());
                    json.put("email", usuario.getEmail());
                    json.put("nombre", usuario.getNombre());
                    json.put("contrasena", usuario.getContrasegna());
                    json.put("edad", usuario.getEdad());
                    json.put("genero", usuario.getGenero());
                    json.put("esadmin", usuario.isEsAdmin());
                    json.put("salario", usuario.getSalario());
                    json.put("puntuacion", usuario.getPuntuacion());

                    String requestBody = json.toString();

                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), requestBody);
                    Request request = new Request.Builder()
                            .url(supabaseClient.getSupabaseUrl() + "/rest/v1/usuario")
                            .addHeader("apikey", supabaseClient.getApiKey())
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Prefer", "return=representation")
                            .post(body)
                            .build();

                    Log.d("HTTP_DEBUG", "Enviando solicitud HTTP para insertar usuario...");

                    try (okhttp3.Response response = client.newCall(request).execute()) {
                        boolean success = response.isSuccessful();

                        if (success) {
                            Log.d("HTTP_DEBUG", "Solicitud HTTP exitosa: " + response.code());
                        } else {
                            Log.d("HTTP_DEBUG", "Solicitud HTTP fallida: " + response.code());
                        }

                        handler.post(() -> callback.onCompleted(success));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(() -> callback.onCompleted(false));
                }
            }
        }).start();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //(Iniciar sesion 2/2)- comprobamos y devolvemos usuario
    public static void obtenerUsuarioPorCredenciales(String email, String contrasena, UserFetchCallback callback) {
        handler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(supabaseClient.getSupabaseUrl() + "/rest/v1/usuario?email=eq." + email + "&contrasena=eq." + contrasena)
                            .addHeader("apikey", supabaseClient.getApiKey())
                            .build();

                    Log.d("HTTP_DEBUG", "Enviando solicitud HTTP para obtener usuario por credenciales...");

                    try (Response response = client.newCall(request).execute()) {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JSONArray jsonArray = new JSONArray(responseData);

                            if (jsonArray.length() > 0) {
                                // Se encontró un usuario con las credenciales proporcionadas
                                // Obtener los datos del primer usuario en el JSONArray
                                JSONObject userData = jsonArray.getJSONObject(0);

                                // Crear un objeto Usuario con los datos obtenidos
                                Usuario usuario = new Usuario(
                                        userData.getString("idempresa"),
                                        userData.getString("email"),
                                        userData.getString("nombre"),
                                        userData.getString("contrasena"),
                                        userData.getInt("edad"),
                                        userData.getString("genero"),
                                        userData.getBoolean("esadmin"),
                                        userData.getDouble("salario"),
                                        userData.getDouble("puntuacion")
                                );

                                // Llamar al método onUserFetched del callback con el objeto Usuario
                                handler.post(() -> callback.onUserFetched(usuario));
                            } else {
                                // No se encontró ningún usuario con las credenciales proporcionadas
                                handler.post(() -> callback.onUserFetchFailure("Credenciales incorrectas"));
                            }
                        } else {
                            Log.d("HTTP_DEBUG", "Solicitud HTTP fallida: " + response.code());
                            handler.post(() -> callback.onUserFetchFailure("Error de red: " + response.code()));
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    handler.post(() -> callback.onUserFetchFailure("Error interno: " + e.getMessage()));
                }
            }
        }).start();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //(Crear empresa 5/5) y (Unirse a empresa 3/3)Actualizar idempresa en la tabla usuario
    public static void actualizarIdEmpresa(String email, String contrasena, String nuevoIdEmpresa, UsuarioUpdateCallback callback) {
        handler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    json.put("idempresa", nuevoIdEmpresa);
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
                    Request request = new Request.Builder()
                            .url(supabaseClient.getSupabaseUrl() + "/rest/v1/usuario?email=eq." + email + "&contrasena=eq." + contrasena)
                            .addHeader("apikey", supabaseClient.getApiKey())
                            .patch(body)  // Cambiado a método patch
                            .build();

                    // Mensajes de depuración adicionales
                    Log.d("HTTP_DEBUG", "Enviando solicitud HTTP para actualizar ID de empresa...");
                    Log.d("HTTP_DEBUG", "URL de la solicitud: " + request.url().toString());
                    Log.d("HTTP_DEBUG", "Cuerpo de la solicitud JSON: " + json.toString());

                    try (Response response = client.newCall(request).execute()) {
                        Log.d("HTTP_DEBUG", "Código de estado de la respuesta: " + response.code());
                        if (response.isSuccessful()) {
                            // La actualización fue exitosa
                            handler.post(() -> callback.onUpdateCompleted(true));
                        } else {
                            // La actualización falló
                            handler.post(() -> callback.onUpdateCompleted(false));
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    handler.post(() -> callback.onUpdateCompleted(false));
                }
            }
        }).start();
    }



////////////////////////////////////////////////////////////////////////////////////////////////



}
