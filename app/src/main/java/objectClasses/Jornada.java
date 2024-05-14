package objectClasses;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import connections.Conexion;
import connections.JornadaInsertCallback;
import connections.JornadaUpdateCallback;
import connections.VerificacionCallback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Jornada {
    static Conexion supabaseClient = new Conexion();
    private String idjornada;
    private String idusuario;
    private String fecha;
    private String horainicio;
    private String horafinal;

    public Jornada(String idusuario, String fecha, String horainicio, String horafinal) {
        this.idusuario = idusuario;
        this.fecha = fecha;
        this.horainicio = horainicio;
        this.horafinal = horafinal;
    }

    public String getIdjornada() {
        return idjornada;
    }

    public void setIdjornada(String idjornada) {
        this.idjornada = idjornada;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(String horainicio) {
        this.horainicio = horainicio;
    }

    public String getHorafinal() {
        return horafinal;
    }

    public void setHorafinal(String horafinal) {
        this.horafinal = horafinal;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //metodo para insertar jornada
    public static void insertarJornada(Jornada jornada, JornadaInsertCallback callback) {
        Handler handler = new Handler(Looper.getMainLooper());

        new Thread(() -> {
            try {
                // Construir el JSON con los datos de la jornada
                JSONObject json = new JSONObject();
                json.put("idusuario", jornada.getIdusuario());
                json.put("fecha", jornada.getFecha());
                json.put("horainicio", jornada.getHorainicio());
                json.put("horafinal", jornada.getHorafinal());

                String requestBody = json.toString();

                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), requestBody);
                Request request = new Request.Builder()
                        .url(supabaseClient.getSupabaseUrl() + "/rest/v1/jornada")
                        .addHeader("apikey", supabaseClient.getApiKey())
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Prefer", "return=representation")
                        .post(body)
                        .build();

                Log.d("HTTP_DEBUG", "Enviando solicitud HTTP para insertar jornada...");

                try (Response response = client.newCall(request).execute()) {
                    boolean success = response.isSuccessful();

                    if (success) {
                        Log.d("HTTP_DEBUG", "Solicitud HTTP exitosa: " + response.code());
                    } else {
                        Log.d("HTTP_DEBUG", "Solicitud HTTP fallida: " + response.code() + " - " + response.body().string());
                    }

                    handler.post(() -> callback.onJornadaInserted(success));
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> callback.onJornadaInserted(false));
            }
        }).start();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////
    //registrar fin de jornada
    public static void registrarHoraFinal(String idUsuario, String fecha, String horaFinal, JornadaUpdateCallback callback) {
        Handler handler = new Handler(Looper.getMainLooper());

        new Thread(() -> {
            try {
                // Construir el JSON con los datos de la jornada
                JSONObject json = new JSONObject();
                json.put("horafinal", horaFinal);

                String requestBody = json.toString();

                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), requestBody);
                Request request = new Request.Builder()
                        .url(supabaseClient.getSupabaseUrl() + "/rest/v1/jornada?idusuario=eq." + idUsuario + "&fecha=eq." + fecha)
                        .addHeader("apikey", supabaseClient.getApiKey())
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Prefer", "return=representation")
                        .patch(body)  // Usamos PATCH para actualizar en lugar de POST
                        .build();

                Log.d("HTTP_DEBUG", "Enviando solicitud HTTP para actualizar hora final...");

                try (Response response = client.newCall(request).execute()) {
                    boolean success = response.isSuccessful();

                    if (success) {
                        Log.d("HTTP_DEBUG", "Solicitud HTTP exitosa: " + response.code());
                    } else {
                        Log.d("HTTP_DEBUG", "Solicitud HTTP fallida: " + response.code() + " - " + response.body().string());
                    }

                    handler.post(() -> callback.onJornadaUpdated(success));
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> callback.onJornadaUpdated(false));
            }
        }).start();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////
    //verificar hora final no registrada
    public static void verificarHoraFinalNoRegistrada(String idUsuario, String fecha, VerificacionCallback callback) {
        Handler handler = new Handler(Looper.getMainLooper());

        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(supabaseClient.getSupabaseUrl() + "/rest/v1/jornada?idusuario=eq." + idUsuario + "&fecha=eq." + fecha + "&horafinal=eq.not_registered")
                        .addHeader("apikey", supabaseClient.getApiKey())
                        .build();

                Log.d("HTTP_DEBUG", "Enviando solicitud HTTP para verificar hora final no registrada...");

                try (Response response = client.newCall(request).execute()) {
                    boolean success = response.isSuccessful();
                    boolean notRegistered;

                    if (success) {
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);

                        // Si la longitud de la matriz es mayor que 0, significa que hay registros con hora final "not_registered"
                        notRegistered = jsonArray.length() > 0;
                    } else {
                        notRegistered = false;
                        Log.d("HTTP_DEBUG", "Solicitud HTTP fallida: " + response.code());
                    }

                    handler.post(() -> callback.onVerificacionCompleted(notRegistered));
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> callback.onVerificacionCompleted(false));
            }
        }).start();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////





}
