package comp5703.sydney.edu.au.learn;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import comp5703.sydney.edu.au.learn.VO.updatePasswordParameter;
import comp5703.sydney.edu.au.learn.util.FormValidator;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class setNewPassword extends AppCompatActivity {
    private EditText password;
    public TextInputLayout inputLayoutPassword;

    private EditText password2;
    public TextInputLayout inputLayoutPassword2;

    public String useEmail;

    private Button buttonUse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        Intent intent = getIntent();
        if (intent != null) {
            useEmail = intent.getStringExtra("email"); // 获取传递过来的参数

        }

        password = findViewById(R.id.etPassword);
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);

        password2 = findViewById(R.id.etPassword2);
        inputLayoutPassword2 = findViewById(R.id.inputLayoutPassword2);

        buttonUse = findViewById(R.id.btnSend);
        buttonUse.setOnClickListener(this::onSubmitClick);
    }

    private void onSubmitClick(View view){

        boolean isValid = FormValidator.validateRepeatPassword(inputLayoutPassword2, password.getText().toString(), password2.getText().toString())
                & FormValidator.validatePassword(inputLayoutPassword, password.getText().toString());

        updatePasswordParameter updatePasswordParameter = new updatePasswordParameter();
        updatePasswordParameter.setPassword(password.getText().toString());
        updatePasswordParameter.setPassword2(password2.getText().toString());
        updatePasswordParameter.setEmail(useEmail);

        if (isValid) {
            // 执行提交逻辑
            NetworkUtils.postJsonRequest(updatePasswordParameter, "/user/updatePassword",null, new Callback() {
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

    }

    private void handleResponse(Response response) throws IOException {
        String responseBody = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");
        Object dataValue = jsonObject.get("data");

        if (code == 200) {
            startActivity(new Intent(setNewPassword.this, successfulUpdatePassword.class));
        } else {
            showDialog("failed", (String) dataValue);
        }
    }

    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }

    private void showDialog(String title, String errorMessage) {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(setNewPassword.this);
            builder.setTitle(title);
            builder.setMessage(errorMessage);
            builder.setPositiveButton("OK", (dialog, which) -> {
                // 处理确定按钮点击事件
            });
            builder.create().show();
        });
    }

}