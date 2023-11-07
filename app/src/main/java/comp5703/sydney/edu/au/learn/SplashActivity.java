package comp5703.sydney.edu.au.learn;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class SplashActivity extends AppCompatActivity {

    private ImageView startPageLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        startPageLogo = findViewById(R.id.startPageLogo);

        Picasso.get()
                .load(imageURL + "logo1.png")
                .error(R.drawable.ic_baseline_settings_suggest_24)  // error_image为加载失败时显示的图片
                .into(startPageLogo);

        int SPLASH_DISPLAY_LENGTH = 2000; // 延迟一秒

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 在结束启动页后转到主活动（MainActivity）
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
