package comp5703.sydney.edu.au.learn.Home;

import static android.content.ContentValues.TAG;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import comp5703.sydney.edu.au.learn.DTO.UserSetting;
import comp5703.sydney.edu.au.learn.Home.Fragment.ItemDetailFragment;
import comp5703.sydney.edu.au.learn.Home.Fragment.ItemListFragment;
import comp5703.sydney.edu.au.learn.Home.Fragment.MessagesFragment;
import comp5703.sydney.edu.au.learn.Home.Fragment.ProfileFragment;
import comp5703.sydney.edu.au.learn.Home.Fragment.SellFragment;
import comp5703.sydney.edu.au.learn.Home.Fragment.SettingFragment;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.userIdVO;
import comp5703.sydney.edu.au.learn.service.MyService;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeUseActivity extends AppCompatActivity  implements ItemDetailFragment.IOMessageClick, BottomNavigationListener{
    private static final int REQUEST_CODE_OVERLAY_PERMISSION = 123;
    private Integer userId;
    private String token;
    SharedPreferences sharedPreferences;

    Toolbar toolbar;
    TextView toolbar_title;

    private ImageView header_logo;

    private BottomNavigationView bottomNavigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_use);

        sharedPreferences = this.getSharedPreferences("comp5703", Context.MODE_PRIVATE);

        // get global userID
        userId = sharedPreferences.getInt("userId", 9999);
        token = sharedPreferences.getString("token", null);

        toolbar = findViewById(R.id.simple_toolbar);



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 禁用默认的标题
        toolbar_title = findViewById(R.id.toolbar_title);

        header_logo = findViewById(R.id.header_logo);

        /**
         * 设置header logo
         *
         */

        Picasso.get()
                .load(imageURL + "logo2.png")
                .error(R.drawable.ic_baseline_settings_suggest_24)  // error_image为加载失败时显示的图片
                .into(header_logo);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(new ItemListFragment());
                    toolbar_title.setText("Home");  // 使用自定义标题
                    toolbar.setVisibility(View.VISIBLE); // 显示Toolbar
                    return true;
                case R.id.navigation_sell:
                    loadFragment(new SellFragment());
                    toolbar_title.setText("Selling An Item");  // 使用自定义标题
                    toolbar.setVisibility(View.VISIBLE); // 显示Toolbar
                    return true;
                case R.id.navigation_messages:
                    loadFragment(new MessagesFragment());
                    toolbar_title.setText("Message List");  // 使用自定义标题
                    toolbar.setVisibility(View.VISIBLE); // 显示Toolbar
                    return true;
                case R.id.profile:
                    loadFragment(new ProfileFragment());
                    toolbar_title.setText("My Profile");  // 使用自定义标题
                    toolbar.setVisibility(View.GONE); // 显示Toolbar
                    return true;

            }
            return false;
        });

        ImageView headerLogo = findViewById(R.id.toolbar_logo);
        // 给ImageView设置OnClickListener
        headerLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置底部导航栏的选中项为首页
                bottomNavigationView.setSelectedItemId(R.id.navigation_sell);
            }
        });

        // 默认加载一个Fragment
        loadFragment(new ItemListFragment());

        // 获取用户 是否开启声音，是否接收消息提示， 消息提示音选择
        getUserSetting();

        // 创建通知service
        createNotification(false);

    }


    /**
     * TODO 发送请求，拉取用户配置
     */
    private void getUserSetting() {
        userIdVO userIdVO = new userIdVO(userId);
        // 发送一份offer
        NetworkUtils.getWithParamsRequest(userIdVO,"/normal/getUserSetting",token, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleResponse(response);
            }

            @Override
            public void onFailure(Call call, IOException e) {
            }
        });

    }

    private void handleResponse(Response response) throws IOException {
        String responseBody = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");

        if (code == 200) {

            UserSetting userSetting = jsonObject.getJSONObject("data").toJavaObject(UserSetting.class);

            /**
             * 将用户设置存入全局变量中
             */

            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("messageReceived", userSetting.getMessageReceived() == 1);
            editor.putBoolean("messageToneOpen", userSetting.getMessageToneOpen() == 1);
            editor.putBoolean("notificationOpen", userSetting.getNotificationOpen() == 1);
            editor.putString("chooseTone", userSetting.getChooseTone());

            editor.apply();


        } else {
            Log.d(TAG, "error");
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }


    // 开启websocket连接
    public void createNotification(boolean bringOpenSetting) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION);
        }else {

           // 用户设置开启弹窗 bringOpenSetting是由setting page传过来的用户设置
            if (sharedPreferences.getBoolean("notificationOpen", false) || bringOpenSetting ){

                Intent serviceIntent = new Intent(this, MyService.class);
                serviceIntent.putExtra("userId", userId);
                startService(serviceIntent);
            }else {
                // 如果用户之前授权过，但用户自己设置关闭弹窗
                Snackbar.make(findViewById(android.R.id.content), "Your notification setting is closed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }

    }

    public void stopMyService() {
        Intent serviceIntent = new Intent(this, MyService.class);
        stopService(serviceIntent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {

            if (Settings.canDrawOverlays(this)) {
                // 权限被授予
                // 启动Service
                Intent serviceIntent = new Intent(this, MyService.class);
                startService(serviceIntent);

                /**
                 * 将用户设置存入全局变量中
                 */
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("notificationOpen", true);
                editor.apply();

                // TODO 发送请求更新用户设置
                NetworkUtils.updateUserSetting(sharedPreferences);

            } else {
                // 权限被拒绝
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("notificationOpen", false);
                editor.apply();
                NetworkUtils.updateUserSetting(sharedPreferences);
                loadFragment(new SettingFragment());

            }


        }
    }



    public int getContainerId() {
        return R.id.fl_container;
    }

    // when the item detail click , trigger this function
    @Override
    public void onClick(String text) {
        Handler handler = new Handler();
        // 设置延迟五秒推送消息，欺骗客户
        int delayMillis = 3000; // 3 秒
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, delayMillis);
    }

    @Override
    public void onBottomNavigationItemSelected(int itemId) {
        bottomNavigationView.setSelectedItemId(itemId);
    }

    public void updateToolbar(boolean isVisible, String title) {
        if (isVisible) {
            toolbar.setVisibility(View.VISIBLE);
            toolbar_title.setText(title);
        } else {
            toolbar.setVisibility(View.GONE);
        }
    }

}