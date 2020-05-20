package dahoum.wales.access_app;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {

    @GET("/posts")
    Call<List<JsonObject>> getAllPosts();
}