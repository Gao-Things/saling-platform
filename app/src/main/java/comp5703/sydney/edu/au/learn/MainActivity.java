package comp5703.sydney.edu.au.learn;

import static android.content.ContentValues.TAG;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.recaptcha.Recaptcha;
import com.google.android.recaptcha.RecaptchaAction;
import com.google.android.recaptcha.RecaptchaTasksClient;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import comp5703.sydney.edu.au.learn.VO.LoginParameter;
import comp5703.sydney.edu.au.learn.VO.RecaptchaParameter;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    @Nullable
    private RecaptchaTasksClient recaptchaTasksClient = null;

    private Button loginBtn;
    private Button registerBtn;
    private EditText userName;
    private EditText password;
    private FirebaseAuth auth;
    private LinearLayout login_container;
    private OkHttpClient client;

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

        // 创建 OkHttpClient 实例
        client = new OkHttpClient();
        initializeRecaptchaClient();
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

        verityLoginAction();

    }


    private void initializeRecaptchaClient() {
        Recaptcha
                .getTasksClient(getApplication(), "6Lf8Y7onAAAAACsaI8NLwElJ1d_Z9pB9CQGUlEO6")
                .addOnSuccessListener(
                        this,
                        new OnSuccessListener<RecaptchaTasksClient>() {
                            @Override
                            public void onSuccess(RecaptchaTasksClient client) {
                                MainActivity.this.recaptchaTasksClient = client;
                                // Execute reCAPTCHA verification

                            }
                        })
                .addOnFailureListener(
                        this,
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle communication errors ...
                                // See "Handle communication errors" section
                            }
                        });

    }

    private void verityLoginAction() {
        assert recaptchaTasksClient != null;
        recaptchaTasksClient
                .executeTask(RecaptchaAction.LOGIN)
                .addOnSuccessListener(
                        this,
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String token) {
                                System.out.println(token);
                                // Handle success ...
                                // See "What's next" section for instructions
                                // send token to the backend
                                RecaptchaParameter recaptchaParameter = new RecaptchaParameter();
                                recaptchaParameter.setExpectedAction("login");
                                recaptchaParameter.setToken(token);
                                NetworkUtils.postJsonRequest(recaptchaParameter, "/user/reCAPTCHA", new Callback() {
                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        handleVerityResponse(response);
                                    }

                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        handleFailure(e);
                                    }
                                });

                            }
                        })
                .addOnFailureListener(
                        this,
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle communication errors ...
                                // See "Handle communication errors" section
                            }
                        });

    }

    private void handleVerityResponse(Response response) throws IOException {
        String responseBody = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");
        Object dataValue = jsonObject.get("data");
        if (code == 200) {
            Login();
        } else {
            showErrorDialog("Your are robot");
        }
    }

    private void Login() {
        String email = userName.getText().toString();
        String passwordUse = password.getText().toString();
        LoginParameter loginParameter = new LoginParameter();
        loginParameter.setEmail(email);
        loginParameter.setPassword(passwordUse);

        NetworkUtils.postJsonRequest(loginParameter, "/user/login", new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleResponse(response);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                handleFailure(e);
            }
        });
    }

    private void handleResponse(Response response) throws IOException {
        String responseBody = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");
        Object dataValue = jsonObject.get("data");

        if (code == 200) {
            String token = jsonObject.getString("token"); // 根据实际 JSON 键获取 Token
            Log.d(TAG, "LoginToken: " + token);
            startActivity(new Intent(MainActivity.this, Home.class));
        } else {
            showErrorDialog(dataValue.toString());
        }
    }

    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }

    private void showErrorDialog(String errorMessage) {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Login failed");
            builder.setMessage(errorMessage);
            builder.setPositiveButton("OK", (dialog, which) -> {
                // 处理确定按钮点击事件
            });
            builder.create().show();
        });
    }




}