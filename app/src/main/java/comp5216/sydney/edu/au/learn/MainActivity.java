package comp5216.sydney.edu.au.learn;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import comp5216.sydney.edu.au.learn.util.toastUtil;

public class MainActivity extends AppCompatActivity {

    private Button loginBtn;
    private Button registerBtn;
    private EditText userName;
    private EditText password;
    private FirebaseAuth auth;
    private LinearLayout login_container;

    private View imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 找到控件
        loginBtn = findViewById(R.id.btnLogin);
        userName = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
        login_container = findViewById(R.id.buttonView);
        imageView = findViewById(R.id.imageView);
        registerBtn = findViewById(R.id.register);
        // 初始化页面时，先隐藏表单内容
        login_container.setVisibility(View.GONE);

        // 启动动画
        fadeInLogo();
        // 获取firebase的组件
        auth = FirebaseAuth.getInstance();

        // 实现跳转
        loginBtn.setOnClickListener(this::onClick);

        registerBtn.setOnClickListener(this::toRegisterClick);
    }

    private void fadeInLogo() {
        // 创建一个淡入动画
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);
        fadeIn.setDuration(1000); // 动画持续时间

        fadeIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 当动画结束后，显示登录表单
                login_container.setVisibility(View.VISIBLE);
                // 在此处可以添加表单内容的其他动画效果
            }
        });

        // 启动动画
        fadeIn.start();
    }
    private void toRegisterClick(View view){
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }
    private void onClick(View view){
       String userNameUse = userName.getText().toString();
       String passwordUse = password.getText().toString();

       // 设置弹窗内容
        String ok = "login successed";
        String fail = "wrong password or username";

        if(!userNameUse.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(userNameUse).matches()){
            if(!passwordUse.isEmpty()){
                auth.signInWithEmailAndPassword(userNameUse, passwordUse)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                toastUtil.showToast(MainActivity.this, ok);
                                startActivity(new Intent(MainActivity.this, Home.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Login failed")
                                        .setMessage("Wrong password or account number")
                                        .setPositiveButton("confirm", (dialogInterface, i) -> {

                                        });

                                builder.create().show();

                            }
                        });
            }else {
                password.setError("password can`t be null");
            }
        } else if (userNameUse.isEmpty()) {
            // 测试环境，跳过验证过程
//            startActivity(new Intent(MainActivity.this, function.class));
//            finish();
            userName.setError("Email can`t be empty");
        }else {
            userName.setError("Email is not valid");
        }

    }
}