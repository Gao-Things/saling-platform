package comp5703.sydney.edu.au.learn.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import comp5703.sydney.edu.au.learn.Home.Adapter.FragmentOfferAdapter;
import comp5703.sydney.edu.au.learn.Home.Fragment.ItemDetailFragment;
import comp5703.sydney.edu.au.learn.Home.Fragment.ItemListEditFragment;
import comp5703.sydney.edu.au.learn.Home.Fragment.ItemListFragment;
import comp5703.sydney.edu.au.learn.Home.Fragment.MyOfferFragment;
import comp5703.sydney.edu.au.learn.Home.Fragment.OfferContainerFragment;
import comp5703.sydney.edu.au.learn.Home.Fragment.ProfileFragment;
import comp5703.sydney.edu.au.learn.Home.Fragment.SellFragment;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.service.MyService;

public class HomeUseActivity extends AppCompatActivity implements ItemDetailFragment.IOMessageClick{
    private static final int REQUEST_CODE_OVERLAY_PERMISSION = 123;
    private Integer userId;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_use);


        SharedPreferences sharedPreferences = this.getSharedPreferences("comp5703", Context.MODE_PRIVATE);

        // get global userID
        userId = sharedPreferences.getInt("userId", 9999);


        Toolbar toolbar = findViewById(R.id.simple_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 禁用默认的标题
        TextView toolbar_title = findViewById(R.id.toolbar_title);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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
                case R.id.navigation_notifications:
                    loadFragment(new ItemDetailFragment());
                    toolbar_title.setText("Notifications");  // 使用自定义标题
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

        // 默认加载一个Fragment
        loadFragment(new ItemListFragment());

        // 创建通知service
        createNotification();

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }


    // 开启websocket连接
    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION);
        }else {
            Intent serviceIntent = new Intent(this, MyService.class);
            serviceIntent.putExtra("userId", userId);
            startService(serviceIntent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
            // 在这里处理悬浮窗口权限请求的结果
            // 启动Service
            Intent serviceIntent = new Intent(this, MyService.class);
            startService(serviceIntent);
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
}