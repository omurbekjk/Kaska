package io.jachoteam.kaska.helpers;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ElasticRestClient {

    private static final String BASE_URL = "https://us-central1-fir-elastic-ab0bd.cloudfunctions.net/"; //http://localhost:9200/
    private static final String CLASS_NAME = ElasticRestClient.class.getSimpleName();

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


    public static void postJ (String url, RequestParams params){

        post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("DDD",responseBody.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("DD",error.getMessage());
            }
        });

    }


    public void getHttpRequest() {
        try {


            ElasticRestClient.get("", null, new JsonHttpResponseHandler() { // instead of 'get' use twitter/tweet/1
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    Log.i(CLASS_NAME, "onSuccess: " + response.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.i(CLASS_NAME, "onSuccess: " + response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.e(CLASS_NAME, "onFailure");
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                }

                @Override
                public void onRetry(int retryNo) {
                    Log.i(CLASS_NAME, "onRetry " + retryNo);
                    // called when request is retried
                }
            });
        }
        catch (Exception e){
            Log.e(CLASS_NAME, e.getLocalizedMessage());
        }
    }
}