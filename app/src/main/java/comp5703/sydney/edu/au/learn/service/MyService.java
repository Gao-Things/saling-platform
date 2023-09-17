package comp5703.sydney.edu.au.learn.service;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.R;

public class MyService extends Service {

    private int number = 0; // 记录 AlertDialog 出现次数
    public static final String CHANNEL_ID = "your_channel_id"; // 替换为您自己的通知渠道ID
    private WindowManager windowManager;
    private View floatingView;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showBubbleDialog();
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (number == 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("Service", "executed at " + System.currentTimeMillis());
                    mHandler.sendEmptyMessage(1);
                }
            }).start();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // send notification
        createNotification();
        initializeBubbleView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void initializeBubbleView() {

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
        params.y = -statusBarHeight; // 将 params.y 设置为负的状态栏高度
        // 使用布局填充器加载自定义布局文件
        LayoutInflater inflater = LayoutInflater.from(this);
        floatingView = inflater.inflate(R.layout.popup_windows, null);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // 添加悬浮窗口到 WindowManager
        windowManager.addView(floatingView, params);

        floatingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle bubble click
                hideBubbleDialog();
            }
        });

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
        stopSelf();

    }

    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


   private void createNotification(){
       // 创建通知渠道
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           CharSequence channelName = "Your Channel Name";
           String channelDescription = "Your Channel Description";
           int importance = NotificationManager.IMPORTANCE_DEFAULT;

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
               .setSmallIcon(R.drawable.img_5)
               .setColor(ContextCompat.getColor(this, R.color.red))
               .setContentTitle("Hello Chloe, your item....")
               .setContentText("Item 3 has reached the setting maximum price!")
               .setPriority(NotificationCompat.PRIORITY_HIGH)
               .setFullScreenIntent(fullScreenPendingIntent, true);

       // 构建通知
       Notification notification = builder.build();

       // 使用一个唯一的通知 ID
       int notificationId = (int) System.currentTimeMillis();

       // 显示通知
       NotificationManager notificationManager = getSystemService(NotificationManager.class);
       notificationManager.notify(notificationId, notification);
   }

}


