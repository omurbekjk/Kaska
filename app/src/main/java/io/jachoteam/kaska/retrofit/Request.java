package io.jachoteam.kaska.retrofit;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import io.jachoteam.kaska.models.FeedPost;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Request {

    @Headers({"Accept: application/json"})
    @POST("getPostListByUserid")
    Call<Map<String,List<FeedPost>>> getPostListByUserId(@Body JsonObject uid);

    @Headers({"Accept: application/json"})
    @POST("getPostListByTag")
    Call<Map<String, List<FeedPost>>> getPostListByTag(@Body JsonObject tag);

}
