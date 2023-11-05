package comp5703.sydney.edu.au.learn.util;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import comp5703.sydney.edu.au.learn.DTO.UserSetting;
import okhttp3.*;

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";
    // AWS connect detail


    public static final String imageURL = "https://capstone-file-store.s3.amazonaws.com/";

    // local connect detail

    public static final String apiURL = "http://capstone-loadbalancer-2005125113.us-east-1.elb.amazonaws.com";
    public static final String websocketUrl = "ws://capstone-loadbalancer-2005125113.us-east-1.elb.amazonaws.com";
//    public static final String apiURL = "http://192.168.1.6:8082";
//    public static final String websocketUrl = "ws://192.168.1.6:8082";



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
                .url(apiURL + url)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void getWithParamsRequest(@Nullable Object object, String url, @Nullable String token, Callback callback) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiURL + url).newBuilder();


        if (object != null){
            // 拼接参数
            Map<String, String> queryParams = convertObjectToMap(object);

            // 添加查询参数
            if (queryParams != null) {
                for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }
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


    public static void getWithParamsRequest(Map<String, String> parameterMap, String url, @Nullable String token, Callback callback) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiURL + url).newBuilder();

        // 添加查询参数
        if (parameterMap != null) {
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
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


    public static void updateUserSetting( SharedPreferences sharedPreferences) {
        String token = sharedPreferences.getString("token", "null");
        UserSetting userSetting = new UserSetting();
        userSetting.setUserId(sharedPreferences.getInt("userId", 9999));
        userSetting.setChooseTone(sharedPreferences.getString("chooseTone", "Elegant"));

        if (sharedPreferences.getBoolean("notificationOpen", false)){
            userSetting.setNotificationOpen(1);
        }else {
            userSetting.setNotificationOpen(0);
        }

        if (sharedPreferences.getBoolean("messageReceived", false)){
            userSetting.setMessageReceived(1);
        }else {
            userSetting.setMessageReceived(0);
        }

        if (sharedPreferences.getBoolean("messageToneOpen", false)){
            userSetting.setMessageToneOpen(1);
        }else {
            userSetting.setMessageToneOpen(0);
        }

        NetworkUtils.postJsonRequest(userSetting, "/normal/updateUserSetting", token, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseBody = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(responseBody);
                int code = jsonObject.getIntValue("code");
                Log.d("更新成功", String.valueOf(code));
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("用户设置更新失败", e.toString());
            }
        });

    }

}
