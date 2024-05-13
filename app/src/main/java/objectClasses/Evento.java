package objectClasses;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import connections.Conexion;
import connections.EventoDeleteCallback;
import connections.EventoFetchCallback;
import connections.EventoInsertCallback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Evento {
    static Conexion supabaseClient = new Conexion();
    private String idEvento;
    private String nombreEvento;
    private String detallesEvento;
    private String fecha;
    private String idEmpresa;

    public Evento(String nombreEvento, String detallesEvento, String fecha, String idEmpresa) {
        this.nombreEvento = nombreEvento;
        this.detallesEvento = detallesEvento;
        this.fecha = fecha;
        this.idEmpresa = idEmpresa;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getDetallesEvento() {
        return detallesEvento;
    }

    public void setDetallesEvento(String detallesEvento) {
        this.detallesEvento = detallesEvento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //metodo añadir evento
    public static void insertarEvento(Evento evento, EventoInsertCallback callback) {
        Handler handler = new Handler(Looper.getMainLooper());

        new Thread(() -> {
            try {
                // Construir el JSON con los datos del evento
                JSONObject json = new JSONObject();
                json.put("nombre", evento.getNombreEvento());
                json.put("detalles", evento.getDetallesEvento());
                json.put("fecha", evento.getFecha());
                json.put("idEmpresa", evento.getIdEmpresa());

                String requestBody = json.toString();

                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), requestBody);
                Request request = new Request.Builder()
                        .url(supabaseClient.getSupabaseUrl() + "/rest/v1/evento")
                        .addHeader("apikey", supabaseClient.getApiKey())
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Prefer", "return=representation")
                        .post(body)
                        .build();

                Log.d("HTTP_DEBUG", "Enviando solicitud HTTP para insertar evento...");

                try (Response response = client.newCall(request).execute()) {
                    boolean success = response.isSuccessful();

                    if (success) {
                        Log.d("HTTP_DEBUG", "Solicitud HTTP exitosa: " + response.code());
                    } else {

                        Log.d("HTTP_DEBUG", "Solicitud HTTP fallida: " + response.code() + " - " + response.body().string());

                    }

                    handler.post(() -> callback.onEventoInserted(success));
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> callback.onEventoInserted(false));
            }
        }).start();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////
    //metodo obtenerEventos
    public static void obtenerEventosPorEmpresa(String idEmpresa, EventoFetchCallback callback) {
        List<Evento> eventos = new ArrayList<>();

        Handler handler = new Handler(Looper.getMainLooper());

        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(supabaseClient.getSupabaseUrl() + "/rest/v1/evento?idEmpresa=eq." + idEmpresa)
                        .addHeader("apikey", supabaseClient.getApiKey())
                        .build();

                Log.d("HTTP_DEBUG", "Enviando solicitud HTTP para obtener eventos por idEmpresa...");

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject eventoData = jsonArray.getJSONObject(i);
                            String nombreEvento = eventoData.getString("nombre");
                            String detallesEvento = eventoData.getString("detalles");
                            String fecha = eventoData.getString("fecha"); // Obtener la fecha como String
                            String idEmpresaEvento = eventoData.getString("idEmpresa");

                            Evento evento = new Evento(nombreEvento, detallesEvento, fecha, idEmpresaEvento);
                            eventos.add(evento);
                        }

                        handler.post(() -> callback.onEventosFetched(eventos));
                    } else {
                        Log.d("HTTP_DEBUG", "Solicitud HTTP fallida: " + response.code());
                        handler.post(() -> callback.onEventosFetchFailure("Error de red: " + response.code()));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                handler.post(() -> callback.onEventosFetchFailure("Error interno: " + e.getMessage()));
            }
        }).start();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////
    //metodo para borrar evento por nombre y por id de empresa
    public static void borrarEventoPorNombreYEmpresa(String nombreEvento, String idEmpresa, EventoDeleteCallback callback) {
        Handler handler = new Handler(Looper.getMainLooper());

        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(supabaseClient.getSupabaseUrl() + "/rest/v1/evento?nombre=eq." + nombreEvento + "&idEmpresa=eq." + idEmpresa)
                        .addHeader("apikey", supabaseClient.getApiKey())
                        .delete()
                        .build();

                Log.d("HTTP_DEBUG", "Enviando solicitud HTTP para borrar evento por nombre e idEmpresa...");

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        Log.d("HTTP_DEBUG", "Solicitud HTTP exitosa: " + response.code());
                        handler.post(() -> callback.onEventoDeleted(true));
                    } else {
                        Log.d("HTTP_DEBUG", "Solicitud HTTP fallida: " + response.code());
                        handler.post(() -> callback.onEventoDeleted(false));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> callback.onEventoDeleted(false));
            }
        }).start();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

}
