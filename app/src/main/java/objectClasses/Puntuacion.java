package objectClasses;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import connections.Conexion;
import connections.PuntuacionInsertCallback;
import connections.PuntuacionUpdateCallback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Puntuacion {
    static Conexion supabaseClient = new Conexion();
    private static Handler handler;

    private String idpuntuacion;
    private String email;
    private int puntuacion;
    private String comentario;
    private String idempresa;

    public Puntuacion(String email, int puntuacion, String comentario, String idempresa) {
        this.email = email;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.idempresa = idempresa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(String idempresa) {
        this.idempresa = idempresa;
    }

    @Override
    public String toString() {
        return "Puntuacion{" +
                "email='" + email + '\'' +
                ", puntuacion=" + puntuacion +
                ", comentario='" + comentario + '\'' +
                ", idempresa='" + idempresa + '\'' +
                '}';
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Insertar puntuacion
    public static void insertarPuntuacionEnBaseDeDatos(Puntuacion puntuacion, PuntuacionInsertCallback callback) {
        handler = new Handler(Looper.getMainLooper());

        new Thread(() -> {
            final String TAG = "HTTP_DEBUG";
            final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
            final String HEADER_API_KEY = "apikey";
            final String HEADER_CONTENT_TYPE = "Content-Type";
            final String HEADER_PREFER = "Prefer";
            final String PREFER_RETURN_REPRESENTATION = "return=representation";

            try {
                JSONObject json = new JSONObject();
                json.put("email", puntuacion.getEmail());
                json.put("puntuacion", puntuacion.getPuntuacion());
                json.put("comentario", puntuacion.getComentario());
                json.put("idempresa", puntuacion.getIdempresa());

                String requestBody = json.toString();

                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.get(CONTENT_TYPE_JSON), requestBody);
                Request request = new Request.Builder()
                        .url(supabaseClient.getSupabaseUrl() + "/rest/v1/puntuacion")
                        .addHeader(HEADER_API_KEY, supabaseClient.getApiKey())
                        .addHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON)
                        .addHeader(HEADER_PREFER, PREFER_RETURN_REPRESENTATION)
                        .post(body)
                        .build();

                Log.d(TAG, "Enviando solicitud HTTP para insertar puntuacion...");

                try (Response response = client.newCall(request).execute()) {
                    boolean success = response.isSuccessful();

                    if (success) {
                        Log.d(TAG, "Solicitud HTTP exitosa: " + response.code());
                    } else {
                        Log.d(TAG, "Solicitud HTTP fallida: " + response.code());
                    }

                    handler.post(() -> callback.onCompleted(success));
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                handler.post(() -> callback.onCompleted(false));
            }
        }).start();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //-actualizar puntuacion
    public static void actualizarPuntuacionEnBaseDeDatos(Puntuacion puntuacion, PuntuacionUpdateCallback callback) {
        handler = new Handler(Looper.getMainLooper());

        new Thread(() -> {
            final String TAG = "HTTP_DEBUG";
            final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
            final String HEADER_API_KEY = "apikey";
            final String HEADER_CONTENT_TYPE = "Content-Type";

            try {
                JSONObject json = new JSONObject();
                json.put("puntuacion", puntuacion.getPuntuacion());
                json.put("comentario", puntuacion.getComentario());

                String requestBody = json.toString();

                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.get(CONTENT_TYPE_JSON), requestBody);
                Request request = new Request.Builder()
                        .url(supabaseClient.getSupabaseUrl() + "/rest/v1/puntuacion?idempresa=eq." + puntuacion.getIdempresa() + "&email=eq." + puntuacion.getEmail())
                        .addHeader(HEADER_API_KEY, supabaseClient.getApiKey())
                        .addHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON)
                        .patch(body)  // Usamos el método patch para actualizar
                        .build();

                Log.d(TAG, "Enviando solicitud HTTP para actualizar puntuación...");

                try (Response response = client.newCall(request).execute()) {
                    boolean success = response.isSuccessful();

                    if (success) {
                        Log.d(TAG, "Solicitud HTTP exitosa: " + response.code());
                    } else {
                        Log.d(TAG, "Solicitud HTTP fallida: " + response.code());
                    }

                    handler.post(() -> callback.onUpdateCompleted(success));
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                handler.post(() -> callback.onUpdateCompleted(false));
            }
        }).start();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
