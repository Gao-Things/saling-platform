package comp5703.sydney.edu.au.learn;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import comp5703.sydney.edu.au.learn.R;

public class MyProfileActivity extends AppCompatActivity{

    // 定义组件
    private TextView textViewMyProfile;
    private ImageView imageViewIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile); // 请确保此处的 layout 名称与您的 XML 布局文件名匹配

        // 初始化组件
        textViewMyProfile = findViewById(R.id.textView);
        imageViewIcon = findViewById(R.id.iconImageView);


        // 获取到TextView
        TextView usernameTextView = findViewById(R.id.usernameTextView);

        // 从其他地方获取用户名，比如从Intent或SharedPreferences
        String username = "用户的用户名";

        // 设置TextView的文本
        usernameTextView.setText(username);

        // 可选: 为组件设置事件监听器 (例如点击监听器)
        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 当图标被点击时执行的操作
                // 比如：启动一个新的 Activity 或显示一个 Toast
            }
        });
    }
}

