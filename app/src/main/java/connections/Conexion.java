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
    private final String supabaseUrl = "https://vlbsmlsjguviymvrzeqe.supabase.co";
    private final String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZsYnNtbHNqZ3V2aXltdnJ6ZXFlIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcxMDUzMDU3NywiZXhwIjoyMDI2MTA2NTc3fQ.SbdvAkaVbxXUgH7txb0x5Cnci4wMpyfSK6zqTq_Dqz4";

    public Conexion() {
        String supabaseUrl = "https://vlbsmlsjguviymvrzeqe.supabase.co";
        String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZsYnNtbHNqZ3V2aXltdnJ6ZXFlIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcxMDUzMDU3NywiZXhwIjoyMDI2MTA2NTc3fQ.SbdvAkaVbxXUgH7txb0x5Cnci4wMpyfSK6zqTq_Dqz4";
    }

    public String getSupabaseUrl() {
        return supabaseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }




}
