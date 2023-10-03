package comp5703.sydney.edu.au.learn.util;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import okhttp3.*;

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";
    // replace with your computer ip address
    public static final String apiURL = "http://192.168.1.101:8082";
    public static final String imageURL = "http://192.168.1.101:8083/";
    private static OkHttpClient client = new OkHttpClient();

    public static void postJsonRequest(Object object, String url,@Nullable String token, Callback callback) {
        String jsonBody = JSON.toJSONString(object);
        MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, jsonBody);
        String useUrl = apiURL + url;
        // 创建Request的构建器
        Request.Builder requestBuilder = new Request.Builder()
                .url(useUrl)
                .post(requestBody);

        // 如果token不为空，添加请求头
        if (token != null && !token.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + token);
        }

        // 构建请求
        Request request = requestBuilder.build();
        client.newCall(request).enqueue(callback);
    }

    public static void postFormDataRequest(File file , String url, Callback callback) {

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file))
                .build();


        Request request = new Request.Builder()
                .url(apiURL + "/public/product/uploadImage")  // 上传URL
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

    public static void getWithParamsRequest(Object object, String url, @Nullable String token, Callback callback) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiURL + url).newBuilder();
        // 拼接参数
        Map<String, String> queryParams = convertObjectToMap(object);
        // 添加查询参数
        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        String useUrl = urlBuilder.build().toString();

        Request.Builder requestBuilder = new Request.Builder()
                .url(useUrl);

        // 检查token是否不为空，然后添加到请求头
        if (token != null && !token.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + token);
        }

        Request request = requestBuilder.build();

        client.newCall(request).enqueue(callback);
    }




    public static void postRequest(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(null, new byte[0]))
                .build();

        client.newCall(request).enqueue(callback);
    }

    private static Map<String, String> convertObjectToMap(Object obj) {
        if (obj == null) {
            return null;
        }

        Map<String, String> paramMap = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = field.get(obj);
                if (fieldValue != null) {
                    paramMap.put(fieldName, fieldValue.toString());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return paramMap;
    }

}
