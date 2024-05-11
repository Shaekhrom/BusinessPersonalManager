package objectClasses;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import connections.Conexion;
import connections.EmpresaFetchCallback;
import connections.EmpresaIdCallback;
import connections.EmpresaInsertCallback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Empresa {
    static Conexion supabaseClient = new Conexion();
    //atributos de objeto
    private String id;
    private String nombre;
    private String contrasenaEmpresa;
    private String sector;
    private String detalles;
    private static Handler handler;

    public Empresa(String id, String nombre, String contrasenaEmpresa, String sector, String detalles) {
        this.id = id;
        this.nombre = nombre;
        this.contrasenaEmpresa = contrasenaEmpresa;
        this.sector = sector;
        this.detalles = detalles;
    }

    public Empresa(String nombre, String contrasenaEmpresa, String sector, String detalles) {
        this.nombre = nombre;
        this.contrasenaEmpresa = contrasenaEmpresa;
        this.sector = sector;
        this.detalles = detalles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenaEmpresa() {
        return contrasenaEmpresa;
    }

    public void setContrasenaEmpresa(String contrasenaEmpresa) {
        this.contrasenaEmpresa = contrasenaEmpresa;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////(Crear empresa 3/5)-Insertar empresa
    public static void insertarEmpresa(Empresa empresa, EmpresaInsertCallback callback) {
        handler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Construir el JSON con los datos de la empresa
                    JSONObject json = new JSONObject();
                    json.put("nombreempresa", empresa.getNombre());
                    json.put("contrasenaempresa", empresa.getContrasenaEmpresa());
                    json.put("sector", empresa.getSector());
                    json.put("detalles", empresa.getDetalles());

                    String requestBody = json.toString();

                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), requestBody);
                    Request request = new Request.Builder()
                            .url(supabaseClient.getSupabaseUrl() + "/rest/v1/empresa")
                            .addHeader("apikey", supabaseClient.getApiKey())
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Prefer", "return=representation")
                            .post(body)
                            .build();

                    Log.d("HTTP_DEBUG", "Enviando solicitud HTTP para insertar empresa...");

                    try (Response response = client.newCall(request).execute()) {
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
    //(Crear empresa 4/5)obtener id empresa
    public static void obtenerIdEmpresa(String nombreEmpresa, String contrasegnaEmpresa, EmpresaIdCallback callback) {
        handler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(supabaseClient.getSupabaseUrl() + "/rest/v1/empresa?idempresa&nombreempresa=eq." + nombreEmpresa + "&contrasenaempresa=eq." + contrasegnaEmpresa)
                            .addHeader("apikey", supabaseClient.getApiKey())
                            .build();

                    Log.d("HTTP_DEBUG", "Enviando solicitud HTTP para obtener ID de empresa...");

                    try (Response response = client.newCall(request).execute()) {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JSONArray jsonArray = new JSONArray(responseData);

                            if (jsonArray.length() > 0) {
                                // Se encontró una empresa con el nombre y la contraseña proporcionados
                                // Obtener el ID de la primera empresa en el JSONArray
                                JSONObject empresaData = jsonArray.getJSONObject(0);
                                String idEmpresa = empresaData.getString("idempresa");

                                // Llamar al método onIdFetched del callback con el ID de empresa
                                handler.post(() -> callback.onIdFetched(idEmpresa));
                            } else {
                                // No se encontró ninguna empresa con el nombre y la contraseña proporcionados
                                handler.post(() -> callback.onIdFetchFailure("Credenciales incorrectas"));
                            }
                        } else {
                            Log.d("HTTP_DEBUG", "Solicitud HTTP fallida: " + response.code());
                            handler.post(() -> callback.onIdFetchFailure("Error de red: " + response.code()));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.post(() -> callback.onIdFetchFailure("Error interno: " + e.getMessage()));
                }
            }
        }).start();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //(Unirse a empresa 2/3)obtener objeto empresa por id
    public static void obtenerDatosEmpresa(String nombreEmpresa, String contrasegnaEmpresa, EmpresaFetchCallback callback) {
        handler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(supabaseClient.getSupabaseUrl() + "/rest/v1/empresa?nombreempresa=eq." + nombreEmpresa + "&contrasenaempresa=eq." + contrasegnaEmpresa)
                            .addHeader("apikey", supabaseClient.getApiKey())
                            .build();

                    Log.d("HTTP_DEBUG", "Enviando solicitud HTTP para obtener datos de empresa...");

                    try (Response response = client.newCall(request).execute()) {
                        if (response.isSuccessful()) {
                            String responseData = response.body().string();
                            JSONArray jsonArray = new JSONArray(responseData);

                            if (jsonArray.length() > 0) {
                                // Se encontró una empresa con el nombre y la contraseña proporcionados
                                // Obtener los datos de la primera empresa en el JSONArray
                                JSONObject empresaData = jsonArray.getJSONObject(0);
                                String idEmpresa = empresaData.getString("idempresa");
                                String nombreEmpresa = empresaData.getString("nombreempresa");
                                String contrasenaEmpresa = empresaData.getString("contrasenaempresa");
                                String sector = empresaData.getString("sector");
                                String detalles = empresaData.getString("detalles");

                                // Crear un objeto Empresa con los datos obtenidos
                                Empresa empresa = new Empresa(idEmpresa, nombreEmpresa, contrasenaEmpresa, sector, detalles);

                                // Llamar al método onEmpresaFetched del callback con el objeto Empresa
                                handler.post(() -> callback.onEmpresaFetched(empresa));
                            } else {
                                // No se encontró ninguna empresa con el nombre y la contraseña proporcionados
                                handler.post(() -> callback.onEmpresaFetchFailure("Credenciales incorrectas"));
                            }
                        } else {
                            Log.d("HTTP_DEBUG", "Solicitud HTTP fallida: " + response.code());
                            handler.post(() -> callback.onEmpresaFetchFailure("Error de red: " + response.code()));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.post(() -> callback.onEmpresaFetchFailure("Error interno: " + e.getMessage()));
                }
            }
        }).start();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

}
