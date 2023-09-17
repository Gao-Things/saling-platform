package comp5703.sydney.edu.au.learn.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import comp5703.sydney.edu.au.learn.Common.HeaderFragment;
import comp5703.sydney.edu.au.learn.Home.tempory.ItemDetailFragment;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.service.MyService;

public class HomeUseActivity extends AppCompatActivity implements ItemDetailFragment.IOMessageClick{
    private HeaderFragment headerFragment;
    private ItemListFragment itemListFragment;
    private ItemListEditFragment itemListEditFragment;
    private static final int REQUEST_CODE_OVERLAY_PERMISSION = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_use);


        // initial item list fragment
        headerFragment = new HeaderFragment();
        itemListFragment = new ItemListFragment();

        // fragment添加到Activity中
        getSupportFragmentManager().beginTransaction().add(R.id.fl_header, headerFragment, "header").commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(new HomeFragment());
                    return true;
                case R.id.navigation_dashboard:
                    loadFragment(new ItemListEditFragment());
                    return true;
                case R.id.navigation_notifications:
                    loadFragment(new ItemDetailFragment());
                    return true;
            }
            return false;
        });

        // 默认加载一个Fragment
        loadFragment(new HomeFragment());

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }


    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION);
        }else {
            Intent serviceIntent = new Intent(this, MyService.class);
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

    // when the item detail click , trigger this function
    @Override
    public void onClick(String text) {
        Handler handler = new Handler();
        // 设置延迟五秒推送消息，欺骗客户
        int delayMillis = 3000; // 3 秒
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 创建通知
                createNotification();
            }
        }, delayMillis);
    }
}