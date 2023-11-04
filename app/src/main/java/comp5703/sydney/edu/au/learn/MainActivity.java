package comp5703.sydney.edu.au.learn;

import static android.content.ContentValues.TAG;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.recaptcha.Recaptcha;
import com.google.android.recaptcha.RecaptchaAction;
import com.google.android.recaptcha.RecaptchaTasksClient;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.VO.LoginParameter;
import comp5703.sydney.edu.au.learn.VO.RecaptchaParameter;
import comp5703.sydney.edu.au.learn.fragment.ContainerActivity;
import comp5703.sydney.edu.au.learn.util.FormValidator;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    @Nullable
    private RecaptchaTasksClient recaptchaTasksClient = null;

    private Button loginBtn;
    private EditText userName;
    public TextInputLayout inputLayoutEmail;


    private EditText password;
    public TextInputLayout inputLayoutPassword;


    private FirebaseAuth auth;
    private OkHttpClient client;
    private TextView forgetText;
    private TextView registerText;
    private LinearLayout popContainer;
    private TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 找到控件
        loginBtn = findViewById(R.id.btnLogin);
        userName = findViewById(R.id.etUsername);
        inputLayoutEmail = findViewById(R.id.inputLayoutEmail);

        password = findViewById(R.id.etPassword);
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);

        registerText = findViewById(R.id.registerTextView);
        forgetText = findViewById(R.id.forgetPassword);
        popContainer = findViewById(R.id.popContainer);
        messageTextView = findViewById(R.id.messageTextView);

        String text = "Forget your password ?";
        SpannableString spannableString = new SpannableString(text);

        // 添加下划线
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);

        // 添加蓝色字体颜色
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#437D28")), 0, text.length(), 0);

        // 将 SpannableString 设置到 TextView
        forgetText.setText(spannableString);


        String text1 = "Register now";
        SpannableString spannableString1 = new SpannableString(text1);

        // 添加下划线
        spannableString1.setSpan(new UnderlineSpan(), 0, text1.length(), 0);

        // 添加蓝色字体颜色
        spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#437D28")), 0, text1.length(), 0);

        // 将 SpannableString 设置到 TextView
        registerText.setText(spannableString1);


        // 获取firebase的组件
        auth = FirebaseAuth.getInstance();

        // 创建 OkHttpClient 实例
        client = new OkHttpClient();
        initializeRecaptchaClient();

        forgetText.setOnClickListener(this::toForgetClick);
        loginBtn.setOnClickListener(this::onClick);
        registerText.setOnClickListener(this::toRegisterClick);

    }

    private void toForgetClick(View view) {
        startActivity(new Intent(MainActivity.this, forgotPassword.class));
    }

    private void toRegisterClick(View view) {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }

    private void onClick(View view) {

        boolean isValid = FormValidator.validateEmail(inputLayoutEmail, userName.getText().toString())
                & FormValidator.validatePassword(inputLayoutPassword, password.getText().toString());

        if (isValid) {
            // 执行提交逻辑
            Login();
//            verityLoginAction();
        }


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
//        assert recaptchaTasksClient != null;
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
                                NetworkUtils.postJsonRequest(recaptchaParameter, "/public/reCAPTCHA", null,new Callback() {
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
            showErrorDialog("error","Your are robot", MainActivity.this);
        }
    }

    private void Login() {
        String email = userName.getText().toString();
        String passwordUse = password.getText().toString();
        LoginParameter loginParameter = new LoginParameter();
        loginParameter.setEmail(email);
        loginParameter.setPassword(passwordUse);
        loginParameter.setUserRole(1);

        NetworkUtils.postJsonRequest(loginParameter, "/public/login", null,new Callback() {
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

        if (code == 200) {
            JSONObject data = jsonObject.getJSONObject("data");
            String token = jsonObject.getString("jwttoken"); // 根据实际 JSON 键获取 Token
            System.out.println(jsonObject);
            // 获取用户id
            Integer userId  = data.getInteger("id");
            Log.d(TAG, "userId: " + userId);

            // get SharedPreferences instance
            SharedPreferences sharedPreferences = getSharedPreferences("comp5703", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            // 设置全局的userId, token
            editor.putInt("userId", userId);
            editor.putString("token", token);
            editor.apply();

            startActivity(new Intent(MainActivity.this, HomeUseActivity.class));
        } else {
            showErrorDialog("Login failed", jsonObject.getString("data"), MainActivity.this);
        }
    }

    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }

    private void showErrorDialog(String title, String Message, Context context) {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(title);
            builder.setMessage(Message);
            builder.setPositiveButton("OK", (dialog, which) -> {
                // 处理确定按钮点击事件
            });
            builder.create().show();
        });
    }


}