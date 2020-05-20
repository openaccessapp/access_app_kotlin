package dahoum.wales.access_app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.BreakIterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2<RequestQueue, JsonObjectRequest> extends AppCompatActivity {

    private static final String TAG = MainActivity2.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RetrofitService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<List<JsonObject>> call = service.getAllPosts();
        call.enqueue(new Callback<List<JsonObject>>() {

            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                Toast.makeText(MainActivity2.this, response.body().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                Toast.makeText(MainActivity2.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void POSTRequest() throws IOException {

        final String POST_PARAMS = "{\n" + "\"userId\": 101,\r\n" +

                "    \"id\": 101,\r\n" +

                "    \"title\": \"Test Title\",\r\n" +

                "    \"body\": \"Test Body\"" + "\n}";

        System.out.println(POST_PARAMS);

        URL obj = new URL("https://jsonplaceholder.typicode.com/posts");

        HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("userId", "a1bcdefgh");
        postConnection.setRequestProperty("Content-Type", "application/json");
        postConnection.setDoOutput(true);

        OutputStream os = postConnection.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();

        int responseCode = postConnection.getResponseCode();

        System.out.println("POST Response Code :  " + responseCode);

        System.out.println("POST Response Message : " + postConnection.getResponseMessage());

        if (responseCode == HttpURLConnection.HTTP_CREATED) {

            BufferedReader in = new BufferedReader(new InputStreamReader(

                    postConnection.getInputStream()));

            String inputLine;

            StringBuilder response = new StringBuilder();

            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();
            System.out.println(response.toString());
        } else {
            System.out.println("POST NOT WORKED");

        }

    }
}


