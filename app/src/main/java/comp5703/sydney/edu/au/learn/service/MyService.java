package comp5703.sydney.edu.au.learn.service;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;
import static comp5703.sydney.edu.au.learn.util.NetworkUtils.websocketUrl;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import comp5703.sydney.edu.au.learn.DTO.MessageFormat;
import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.DTO.User;
import comp5703.sydney.edu.au.learn.DTO.WebSocketMessage;
import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.ResponseMessage;
import comp5703.sydney.edu.au.learn.VO.SendMessage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class MyService extends Service {

    private int number = 0; // 记录 AlertDialog 出现次数
    public static final String CHANNEL_ID = "your_channel_id"; // 通知渠道ID
    private WindowManager windowManager;
    private View floatingView;

    private final OkHttpClient client = new OkHttpClient();
    private WebSocket webSocket;

    private Integer userId;

    private SharedPreferences sharedPreferences;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Integer value = intent.getIntExtra("userId", -1); // 对应于之前设置的数据类型

            initWebSocket(value);

            userId = value;
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        initializeBubbleView("your offter has been accept", " message, ggbao is so good !!!!");

        // 获取SharedPreferences实例
        sharedPreferences = getSharedPreferences("comp5703", MODE_PRIVATE);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close(1000, "Service destroyed");
        }

    }

    private void initWebSocket(Integer userId) {
        Request request = new Request.Builder()
                .url(websocketUrl +"/notification/"+userId)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                // WebSocket connection opened
                Log.d("Websocket message","websocket连接已打开");
                startHeartbeat();
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                // Message received from server

                Log.d("Websocket message","服务器发送的消息："+ text);

                WebSocketMessage webSocketMessage = JSON.parseObject(text, WebSocketMessage.class);

                // 判断是否为心跳包消息
                if (webSocketMessage.getMessageType() == 998){
                    Log.d("Websocket notification心跳", webSocketMessage.getNotificationContent());

                }else {

                    if (webSocketMessage.getMessageType() != 999 ){

                        Product product =  webSocketMessage.getProduct();
                        // send notification后台
                        createNotification(webSocketMessage.getNotificationContent(), product.getProductName());

                        Log.d("系统消息通知", webSocketMessage.getNotificationContent());
                        // 将操作发送到主线程，前台弹窗
//                        new Handler(Looper.getMainLooper()).post(() -> initializeBubbleView(webSocketMessage.getNotificationContent(), product.getProductName(), null));

                        // Send a message back to the server
                        // 构建响应消息对象
                        ResponseMessage response = new ResponseMessage(1, webSocketMessage.getNotificationId());  // 这里的 1 表示消息已读
                        String responseMessage = JSON.toJSONString(response);

                        webSocket.send(responseMessage);
                    }else {

                        // 消息提示类型为未读的聊天消息

                        User user = webSocketMessage.getRemoteUser();
                        Log.d("用户消息通知", webSocketMessage.getNotificationContent());

                        /**
                         * 判断用户是否开启聊天页面
                         */

                        // 使用 Service 的上下文来获取 SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("comp5703", MODE_PRIVATE);

                        // 获取数据
                        boolean userIsInCheating = sharedPreferences.getBoolean("userIsInCheating", false);

                        if (!userIsInCheating){
                            // 将操作发送到主线程，前台弹窗

                            /**
                             * 对收到对消息内容做一个拆装，原本为JSON String
                             */
                            // 将JSON字符串转换回MessageFormat对象
                            MessageFormat messageFormat = JSON.parseObject(webSocketMessage.getNotificationContent(), MessageFormat.class);

                            if (messageFormat.getMessageType() == 1){
                                new Handler(Looper.getMainLooper()).post(() -> initializeBubbleView(user.getName(), messageFormat.getMessageText(), user.getAvatarUrl()));
                            }else if (messageFormat.getMessageType() == 2){
                                new Handler(Looper.getMainLooper()).post(() -> initializeBubbleView(user.getName(), messageFormat.getCardTitle(), user.getAvatarUrl()));
                            }


                        }else {
                            // 震动手机提示用户有其他消息
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                            if (vibrator != null) {
                                // New vibrate method for API Level 26 (Android O) and above
                                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                            }


                        }


                    }


                }



            }

            // Handle other WebSocket events like onClose, onFailure, ...

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                // Handle connection failures
                stopHeartbeat(); // WebSocket 关闭时停止发送心跳包
                Log.e("Websocket onFailure", "Connection failed: " + t.getMessage());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                // The remote peer initiated a graceful shutdown
                stopHeartbeat(); // WebSocket 关闭时停止发送心跳包
                Log.d("Websocket onClosing", "Remote peer initiated close with code: " + code);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                // WebSocket has been fully closed
                Log.d("Websocket onClosed", "WebSocket closed with code: " + code);
            }

        });

//        client.dispatcher().executorService().shutdown();
    }







    private void initializeBubbleView(String title, String contentUse, @Nullable  String userAvatarUrl) {

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
        );
        params.windowAnimations = android.R.style.Animation_Translucent;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = 300;
        // 获取状态栏高度
        int statusBarHeight = getStatusBarHeight();
        params.gravity = Gravity.TOP | Gravity.FILL_HORIZONTAL; // 将窗口置于屏幕顶部并充满水平方向
        params.x = 0;
//        params.y = -statusBarHeight; // 将 params.y 设置为负的状态栏高度
        // 使用布局填充器加载自定义布局文件
        LayoutInflater inflater = LayoutInflater.from(this);
        floatingView = inflater.inflate(R.layout.popup_windows, null);

        // 设置推送消息的内容
        ImageView logoImageView = floatingView.findViewById(R.id.logoImageView);
        TextView notificationTitle = floatingView.findViewById(R.id.notificationTitle);
        TextView content = floatingView.findViewById(R.id.content);

        ImageView addImage1 = floatingView.findViewById(R.id.addImage1);
        ImageView addImage2 = floatingView.findViewById(R.id.addImage2);

        notificationTitle.setText(title);
        content.setText(contentUse);

        if (userAvatarUrl != null){

            addImage1.setVisibility(View.GONE);
            addImage2.setVisibility(View.GONE);

            Picasso.get()
                    .load(imageURL + userAvatarUrl)
                    .error(R.drawable.img_20)  // error_image为加载失败时显示的图片
                    .into(logoImageView);
        }

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // 添加悬浮窗口到 WindowManager
        windowManager.addView(floatingView, params);

        // 三秒后触发 hideBubbleDialog
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideBubbleDialog();
            }
        }, 3000); // 3000 毫秒（3 秒）


    }

    private void showBubbleDialog() {
        if (!isBubbleDialogShown()) {
            floatingView.setVisibility(View.VISIBLE);
            number++;
        }
    }

    private boolean isBubbleDialogShown() {
        return floatingView.getVisibility() == View.VISIBLE;
    }

    private void hideBubbleDialog() {
        if (floatingView != null) {
            floatingView.setVisibility(View.GONE);
        }

    }

    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


   private void createNotification(String title, String content){
       // 创建通知渠道
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           CharSequence channelName = "Your Channel Name";
           String channelDescription = "Your Channel Description";
           int importance = NotificationManager.IMPORTANCE_HIGH;

           NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
           channel.setDescription(channelDescription);

           NotificationManager notificationManager = getSystemService(NotificationManager.class);
           notificationManager.createNotificationChannel(channel);
       }

       // 创建点击通知后打开的 Intent
       Intent fullScreenIntent = new Intent(this, HomeUseActivity.class);
       PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
               fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


       // 创建通知
       NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
               .setSmallIcon(R.drawable.logo2)
               .setColor(ContextCompat.getColor(this, R.color.darkGreen))
               .setContentTitle(title)
               .setContentText(content)
               .setPriority(NotificationCompat.PRIORITY_HIGH)
               .setFullScreenIntent(fullScreenPendingIntent, true);


       // 自定义的提示音资源文件
       builder.setSound(getUserSettingTone());

       // 构建通知
       Notification notification = builder.build();

       // 使用一个唯一的通知 ID
       int notificationId = (int) System.currentTimeMillis();

       // 显示通知
       NotificationManager notificationManager = getSystemService(NotificationManager.class);
       notificationManager.notify(notificationId, notification);
   }

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private void startHeartbeat() {
        final Runnable heartbeatSender = new Runnable() {
            public void run() { sendHeartbeat(); }
        };

        // 定时任务，每30秒发送一次心跳
        scheduler.scheduleAtFixedRate(heartbeatSender, 30, 30, TimeUnit.SECONDS);
    }

    private void stopHeartbeat() {
        scheduler.shutdown();
    }

    private void sendHeartbeat() {
        if (webSocket != null) {
            // 发送心跳包 "ping"

            // Send a message back to the server
            // 构建响应消息对象
            ResponseMessage response = new ResponseMessage(2, 0);  // 这里的 2 表示为一条心跳包消息
            String responseMessage = JSON.toJSONString(response);

            webSocket.send(responseMessage);
        }
    }




    private Uri getUserSettingTone() {

        String toneChoice = sharedPreferences.getString("chooseTone", null);

        Uri customSound;

        switch (toneChoice) {
            // 获取系统默认的通知音频的 URI
            case "playful":
                customSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.y831);
                break;

            case "crisp":
                customSound = Uri.parse("android.resource://" + getPackageName() + "/"  + R.raw.y1878);
                break;

            case "electronic":
                customSound =  Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.y2170);
                break;

            case "rock":
                customSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.y2225);
                break;

            default:
                customSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                break;
        }


        return customSound;
    }

}


