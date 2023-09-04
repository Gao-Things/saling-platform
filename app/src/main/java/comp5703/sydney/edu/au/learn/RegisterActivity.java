package comp5703.sydney.edu.au.learn;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.FirebaseAuth;


import java.io.IOException;

import comp5703.sydney.edu.au.learn.VO.RegisterParameter;
import comp5703.sydney.edu.au.learn.util.FormValidator;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    public EditText email;
    public TextInputLayout inputLayoutEmail;

    public EditText firstname;
    public TextInputLayout inputLayoutFirstname;

    public EditText lastname;
    public TextInputLayout inputLayoutLastname;

    public EditText password;
    public TextInputLayout inputLayoutPassword;

    private Button registerBtn;

    private FirebaseAuth mAuth;

    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // 找到控件
        email = findViewById(R.id.etEmail);
        inputLayoutEmail = findViewById(R.id.inputLayoutEmail);

        firstname = findViewById(R.id.firstname);
        inputLayoutFirstname = findViewById(R.id.inputLayoutFirstname);

        lastname = findViewById(R.id.lastname);
        inputLayoutLastname = findViewById(R.id.inputLayoutLastname);

        password = findViewById(R.id.etPassword);
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);

        registerBtn = findViewById(R.id.registerBtn);
        loginTextView = findViewById(R.id.loginTextView);


        registerBtn.setOnClickListener(this::registerClick);
        mAuth = FirebaseAuth.getInstance();



        String text1 = "Login now";
        SpannableString spannableString1 = new SpannableString(text1);

        // 添加下划线
        spannableString1.setSpan(new UnderlineSpan(), 0, text1.length(), 0);

        // 添加蓝色字体颜色
        spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#006104")), 0, text1.length(), 0);

        // 将 SpannableString 设置到 TextView
        loginTextView.setText(spannableString1);

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
                    inputLayoutEmail.setError("Username length exceeds limit");
                }
                else{
                    inputLayoutEmail.setError(null);
                }
            }
        });

    }

    private void registerClick(View view) {
        boolean isValid = FormValidator.validateEmail(inputLayoutEmail, email.getText().toString())
                & FormValidator.validateTextInputLayout(inputLayoutFirstname, firstname.getText().toString(), "firstname can`t be empty")
                & FormValidator.validateTextInputLayout(inputLayoutLastname, lastname.getText().toString(), "firstname can`t be empty")
                & FormValidator.validatePassword(inputLayoutPassword, password.getText().toString());

        if (isValid) {
            // 执行提交逻辑
            registerUser(email.getText().toString(),
                    password.getText().toString(),
                    firstname.getText().toString(),
                    lastname.getText().toString());

        }

    }


    private void registerUser(String email, String password, String firstname, String lastname) {

        RegisterParameter registerParameter = new RegisterParameter();
        registerParameter.setEmail(email);
        registerParameter.setPassword(password);
        registerParameter.setFirstname(firstname);
        registerParameter.setLastname(lastname);


        NetworkUtils.postJsonRequest(registerParameter, "/public/registration", new Callback() {
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
            String msg = jsonObject.getString("msg"); // 根据实际 JSON 键获取 Token
            Log.d(TAG, "info: " + msg);
            startActivity(new Intent(RegisterActivity.this, vertifyEmailShow.class));
        } else {
            System.out.println("wdffffffffffffffffffffffffffffffffffff");
        }
    }

    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }



// firebase send email service

//    private void sendVerificationEmail(FirebaseUser user) {
//        if (user != null) {
//            user.sendEmailVerification()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//
//                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
//                            builder.setTitle("Successful !!")
//                                    .setMessage("The vertified Email has been send")
//                                    .setPositiveButton("Back", (dialogInterface, i) -> {
//
//                                    });
//
//                            builder.create().show();
//                        } else {
//                            Toast.makeText(RegisterActivity.this, "无法发送验证邮件，请稍后再试", Toast.LENGTH_LONG).show();
//                        }
//                    });
//        }
//    }


}