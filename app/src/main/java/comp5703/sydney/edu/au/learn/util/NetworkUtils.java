package comp5703.sydney.edu.au.learn.util;

import com.alibaba.fastjson.JSON;

import okhttp3.*;

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";
    // replace with your computer ip address
    public static final String apiURL = "http://172.16.31.14:8082";
    private static OkHttpClient client = new OkHttpClient();

    public static void postJsonRequest(Object object, String url, Callback callback) {
        String jsonBody = JSON.toJSONString(object);
        MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, jsonBody);
        String useUrl = apiURL + url;
        Request request = new Request.Builder()
                .url(useUrl)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void getRequest(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void postRequest(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(null, new byte[0]))
                .build();

        client.newCall(request).enqueue(callback);
    }
}
