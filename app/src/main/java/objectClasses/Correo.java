package objectClasses;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import connections.Conexion;
import connections.InsertCorreoCallback;
import connections.ObtenerCorreosCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Correo implements Parcelable {
    static Conexion supabaseClient = new Conexion();
    private static Handler handler;

    private String idcorreo;
    private String correoemisor;
    private String correoreceptor;
    private String mensaje;
    private String titulomensaje;

    public Correo(String correoemisor, String correoreceptor, String mensaje, String titulomensaje) {
        this.correoemisor = correoemisor;
        this.correoreceptor = correoreceptor;
        this.mensaje = mensaje;
        this.titulomensaje = titulomensaje;
    }

    protected Correo(Parcel in) {
        correoemisor = in.readString();
        correoreceptor = in.readString();
        mensaje = in.readString();
        titulomensaje = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(correoemisor);
        dest.writeString(correoreceptor);
        dest.writeString(mensaje);
        dest.writeString(titulomensaje);
    }

    // Campo CREATOR que implementa la interfaz Parcelable.Creator
    public static final Creator<Correo> CREATOR = new Creator<Correo>() {
        @Override
        public Correo createFromParcel(Parcel in) {
            return new Correo(in);
        }

        @Override
        public Correo[] newArray(int size) {
            return new Correo[size];
        }
    };

    public String getIdcorreo() {
        return idcorreo;
    }

    public void setIdcorreo(String idcorreo) {
        this.idcorreo = idcorreo;
    }

    public String getCorreoemisor() {
        return correoemisor;
    }

    public void setCorreoemisor(String correoemisor) {
        this.correoemisor = correoemisor;
    }

    public String getCorreoreceptor() {
        return correoreceptor;
    }

    public void setCorreoreceptor(String correoreceptor) {
        this.correoreceptor = correoreceptor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTitulomensaje() {
        return titulomensaje;
    }

    public void setTitulomensaje(String titulomensaje) {
        this.titulomensaje = titulomensaje;
    }

    @Override
    public String toString() {
        return "Correo{" +
                "idcorreo='" + idcorreo + '\'' +
                ", correoemisor='" + correoemisor + '\'' +
                ", correoreceptor='" + correoreceptor + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", titulomensaje='" + titulomensaje + '\'' +
                '}';
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //insertar un correo en la bbdd
    public static void insertarCorreo(Correo correo, InsertCorreoCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Construir el JSON con los datos del correo
                    JSONObject json = new JSONObject();
                    json.put("correoemisor", correo.getCorreoemisor());
                    json.put("correoreceptor", correo.getCorreoreceptor());
                    json.put("mensaje", correo.getMensaje());
                    json.put("titulomensaje", correo.getTitulomensaje());

                    String requestBody = json.toString();

                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), requestBody);
                    Request request = new Request.Builder()
                            .url(supabaseClient.getSupabaseUrl() + "/rest/v1/correo")
                            .addHeader("apikey", supabaseClient.getApiKey())
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Prefer", "return=representation")
                            .post(body)
                            .build();

                    Log.d("HTTP_DEBUG", "Enviando solicitud HTTP para insertar correo...");

                    try (Response response = client.newCall(request).execute()) {
                        boolean success = response.isSuccessful();

                        if (success) {
                            Log.d("HTTP_DEBUG", "Solicitud HTTP exitosa: " + response.code());
                        } else {
                            Log.d("HTTP_DEBUG", "Solicitud HTTP fallida: " + response.code());
                            Log.d("HTTP_DEBUG", "Mensaje de error: " + response.message());
                            Log.d("HTTP_DEBUG", "Cuerpo de respuesta: " + response.body().string());
                        }

                        // Crear un nuevo Handler para manejar las operaciones de UI
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(() -> callback.onCorreoInsertCompleted(success));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Crear un nuevo Handler para manejar las operaciones de UI
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> callback.onCorreoInsertCompleted(false));
                }
            }
        }).start();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //obtener lista de correos segun email del receptor
    // Obtener lista de todos los correos y filtrar por correo receptor
    public static void obtenerCorreosPorReceptor(String correoreceptor, ObtenerCorreosCallback callback) {
        Log.d("Correo", "Solicitando correos para el receptor: " + correoreceptor);

        OkHttpClient client = new OkHttpClient();

        // Inicializar el handler antes de usarlo
        Handler handler = new Handler(Looper.getMainLooper());

        Request request = new Request.Builder()
                .url(supabaseClient.getSupabaseUrl() + "/rest/v1/correo")
                .addHeader("apikey", supabaseClient.getApiKey())
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Manejar el error
                handler.post(() -> callback.onCorreosObtenidos(null, e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);
                        ArrayList<Correo> correos = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject correoObject = jsonArray.getJSONObject(i);
                            String correoEmisor = correoObject.getString("correoemisor");
                            String correoReceptor = correoObject.getString("correoreceptor");
                            String mensaje = correoObject.getString("mensaje");
                            String tituloMensaje = correoObject.getString("titulomensaje");
                            if (correoReceptor.equals(correoreceptor)) {
                                correos.add(new Correo(correoEmisor, correoReceptor, mensaje, tituloMensaje));
                            }
                        }
                        Log.d("CorreoActivity", "Tamaño de la lista de correos: " + correos.size());

                        // Utilizar el handler inicializado para realizar operaciones en el hilo principal
                        handler.post(() -> callback.onCorreosObtenidos(correos, null));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        handler.post(() -> callback.onCorreosObtenidos(null, e.getMessage()));
                    }
                } else {
                    handler.post(() -> callback.onCorreosObtenidos(null, "Error: " + response.code()));
                }
            }
        });
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

}
