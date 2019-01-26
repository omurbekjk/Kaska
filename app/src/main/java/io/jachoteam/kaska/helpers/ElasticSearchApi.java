package io.jachoteam.kaska.helpers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.jachoteam.kaska.models.Image;
import io.jachoteam.kaska.models.Post;
import io.jachoteam.kaska.models.UserJava;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by User on 30.12.2018.
 */

public class ElasticSearchApi {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static void getPostByUserId(String uid, final ElasticSearchPostListener listener) {

        OkHttpClient client = new OkHttpClient();
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("params", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParams));


        final Request request = new Request.Builder()
                .url("https://us-central1-kgkaska.cloudfunctions.net/getPostListByUserid")
                .addHeader("Content-Type","application/json")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Fail",call.toString()+"   "+e.getMessage());
                listener.onRequest(false,null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                Log.e("RESPONSE_Finish",json);

                listener.onRequest(true,fromJsonToPost(json));

            }
        });
    }

    public static void getPostByTag(String tag, final ElasticSearchPostListener listener) {

        OkHttpClient client = new OkHttpClient();
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("params", tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParams));


        final Request request = new Request.Builder()
                .url("https://us-central1-kgkaska.cloudfunctions.net/getPostListByTag")
                .addHeader("Content-Type","application/json")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Fail",call.toString()+"   "+e.getMessage());
                listener.onRequest(false,null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                Log.e("RESPONSE_Finish",json);

                listener.onRequest(true,fromJsonToPost(json));

            }
        });
    }


    public static void getUser(String tag, final ElasticSearchPeopleListener listener) {

        OkHttpClient client = new OkHttpClient();
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("params", tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, String.valueOf(jsonParams));


        final Request request = new Request.Builder()
                .url("https://us-central1-kgkaska.cloudfunctions.net/getUserList")
                .addHeader("Content-Type","application/json")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Fail",call.toString()+"   "+e.getMessage());
                listener.onRequest(false,null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                Log.e("RESPONSE_Finish",json);

                listener.onRequest(true,fromJsonToPeople(json));

            }
        });
    }


    public static ArrayList<Post> fromJsonToPost(String json){
        ArrayList<Post> posts = new ArrayList<>();
        try {
            JSONObject jsonObject = new  JSONObject(json);

            JSONArray message = jsonObject.getJSONArray("message");

            for (int j = 0; j < message.length(); j++) {

                JSONObject _source = message.getJSONObject(j).getJSONObject("_source");
                Post post = new Post();
                post.setId(message.getJSONObject(j).getString("_id"));
                post.setAddress(_source.getString("address"));
                post.setAudioUrl(_source.getString("audioUrl"));
                post.setCaption(_source.getString("caption"));
                post.setImage(_source.getString("image"));
                post.setLatitude(_source.getDouble("latitude"));
                post.setLongitude(_source.getDouble("longitude"));
                post.setUid(_source.getString("uid"));
                post.setUsername(_source.getString("username"));
                post.setTimestamp(_source.getLong("timestamp"));

                if (_source.has("tag")) {
                    JSONArray tags = _source.getJSONArray("tag");
                    String[] tag = new String[tags.length()];

                    for (int i = 0; i < tags.length(); i++) {
                        tag[i] = tags.getString(i);
                    }

                    post.setTag(tag);
                }

                JSONObject imagesJson = _source.getJSONObject("images");


                Map<String, Image> images = new HashMap<>();

                Log.e("Imageett",imagesJson.length()+"");


                for (int i = 0; i < imagesJson.length(); i++) {
                    JSONObject imageJson = imagesJson.getJSONObject("image"+i);
                    Image image = new Image(imageJson.getString("uid"), imageJson.getString("url"), imageJson.getInt("order"));
                    images.put("image" + i, image);
                }

                post.setImages(images);

                posts.add(post);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return posts;

    }


    public static ArrayList<UserJava> fromJsonToPeople(String json){
        ArrayList<UserJava> users = new ArrayList<>();
        try {
            JSONObject jsonObject = new  JSONObject(json);

            JSONArray message = jsonObject.getJSONArray("message");

            for (int j = 0; j < message.length(); j++) {

                JSONObject _source = message.getJSONObject(j).getJSONObject("_source");
                UserJava user = new UserJava();
                user.setId(message.getJSONObject(j).getString("_id"));
                user.setEmail(_source.getString("email"));
                user.setName(_source.getString("name"));
                user.setUsername(_source.getString("username"));

                users.add(user);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return users;

    }

}
