package comp5703.sydney.edu.au.learn;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import comp5703.sydney.edu.au.learn.VO.EmailAddress;
import comp5703.sydney.edu.au.learn.util.FormValidator;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import comp5703.sydney.edu.au.learn.util.toastUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class forgotPassword extends AppCompatActivity {
    public EditText email;
    public TextInputLayout inputLayoutEmail;
    public ProgressBar progressBar;
    public TextView progressText;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    public Button sendBtn;

    private TextView hiddenMention;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // 找到控件
        email = findViewById(R.id.etEmail);
        inputLayoutEmail = findViewById(R.id.inputLayoutEmail);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);

        sendBtn = findViewById(R.id.btnSend);
        hiddenMention = findViewById(R.id.hiddenMention);

        inputLayoutEmail.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 文本发生变化后调用

                if(inputLayoutEmail.getEditText().getText().toString().trim().length()>45){
                    inputLayoutEmail.setError("Email length exceeds limit");
                }
                else{
                    inputLayoutEmail.setError(null);
                }
            }
        });

        sendBtn.setOnClickListener(this::sendOnClick);

    }

    private void sendOnClick(View view) {

        boolean isValid = FormValidator.validateEmail(inputLayoutEmail, email.getText().toString());

        if (isValid) {
            runOnUiThread(() -> {
                sendBtn.setEnabled(false);
                sendBtn.setBackgroundResource(R.drawable.custom_button_background4);
            });

            // 执行提交逻辑
            forgetPassword(email.getText().toString());

        }
    }

    private void forgetPassword(String emailAddress){
        EmailAddress emailUse = new EmailAddress();
        emailUse.setEmailAddress(emailAddress);
        NetworkUtils.postJsonRequest(emailUse, "/public/forgetPassword",null, new Callback() {
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

            runOnUiThread(() -> {
                hiddenMention.setVisibility(View.VISIBLE);
            });

        } else {
            showDialog("failed", (String) dataValue);
        }
    }

    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }

    private void polling(){
        Map<String, String> queryParamsUse = new HashMap<>();
        queryParamsUse.put("email", email.getText().toString());
        // 创建轮询的 Runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                // 在这里执行你想要进行的操作
                NetworkUtils.getWithParamsRequest(queryParamsUse, "/public/pollingResult",null, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        handleResponse2(response);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        handleFailure(e);
                    }
                });


                // 再次调度下一次操作
                handler.postDelayed(this, 2000); // 延迟 1000 毫秒，即 1 秒
            }
        };

        // 启动轮询
        handler.post(runnable);

    }



    private void handleResponse2(Response response) throws IOException {
        String responseBody = response.body().string();

        Log.d("polling", responseBody);
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");

        if (code == 200) {
            handler.removeCallbacks(runnable);
            Intent intent = new Intent(forgotPassword.this, setNewPassword.class);
            intent.putExtra("email",email.getText().toString()); // 传递参数
            startActivity(intent);

        }
    }


    private void showDialog(String title, String errorMessage) {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(forgotPassword.this);
            builder.setTitle(title);
            builder.setMessage(errorMessage);
            builder.setPositiveButton("OK", (dialog, which) -> {
                // 处理确定按钮点击事件
            });
            builder.create().show();
        });
    }




}