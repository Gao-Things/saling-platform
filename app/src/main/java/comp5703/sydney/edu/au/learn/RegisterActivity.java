package comp5703.sydney.edu.au.learn;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.FirebaseAuth;


import java.io.IOException;

import comp5703.sydney.edu.au.learn.Common.OnSingleClickListener;
import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
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

    private TextView warningText;
    private WebView webView;

    private CheckBox privatePolicyCheckBox;

    private LinearLayout backClick;


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
        warningText = findViewById(R.id.warningText);

        registerBtn.setOnClickListener(this::registerClick);
        mAuth = FirebaseAuth.getInstance();

        privatePolicyCheckBox = findViewById(R.id.privatePolicyCheckBox);
        backClick = findViewById(R.id.backClick);

        backClick.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        String text1 = "Login now";
        SpannableString spannableString1 = new SpannableString(text1);

        // 添加下划线
        spannableString1.setSpan(new UnderlineSpan(), 0, text1.length(), 0);
        

        // 添加蓝色字体颜色
        spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#006104")), 0, text1.length(), 0);




        String text2 = "Terms & Conditions";
        SpannableString spannableString2 = new SpannableString(text2);

        // 添加下划线
        spannableString2.setSpan(new UnderlineSpan(), 0, text2.length(), 0);

        // 添加蓝色字体颜色
        spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#006104")), 0, text1.length(), 0);

        // 将 SpannableString 设置到 TextView
        loginTextView.setText(spannableString1);

        loginTextView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        warningText.setText(spannableString2);
        // 绑定用户隐私策略的按钮
        warningText.setOnClickListener(this::getUserConfirm);

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

    private void getUserConfirm(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_Learn);
        LayoutInflater inflater = getLayoutInflater();
        View viewUse = inflater.inflate(R.layout.dialog_webview, null);

        WebView webView = viewUse.findViewById(R.id.webView);
        Button closeButton = viewUse.findViewById(R.id.closeButton);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(false);

        String content = "<h1>Terms and conditions</h1>"
                + "<h2>Conditions of use</h2>"
                + "<p>By using this website, you certify that you have read and reviewed this Agreement and that you agree to comply with its terms. If you do not want to be bound by the terms of this Agreement, you are advised to stop using the website accordingly. Gold trading only grants use and access of this website, its products, and its services to those who have accepted its terms.</p>"
                + "<h2>Age restriction</h2>"
                + "<p>You must be at least 18 (eighteen) years of age before you can use this website. By using this website, you warrant that you are at least 18 years of age and you may legally adhere to this Agreement. Gold trading assumes no responsibility for liabilities related to age misrepresentation.</p>";

        String formattedHtml = getFormattedHtml(content);
        webView.loadData(content, "text/html", "UTF-8");

        builder.setView(viewUse);
        AlertDialog dialog = builder.create();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void registerClick(View view) {
        boolean isValid = FormValidator.validateEmail(inputLayoutEmail, email.getText().toString())
                & FormValidator.validateTextInputLayout(inputLayoutFirstname, firstname.getText().toString(), "firstname can`t be empty")
                & FormValidator.validateTextInputLayout(inputLayoutLastname, lastname.getText().toString(), "firstname can`t be empty")
                & FormValidator.validatePassword(inputLayoutPassword, password.getText().toString());

        if (isValid) {
            // 用户没有确认隐私策略
            if (!privatePolicyCheckBox.isChecked()){

                Snackbar.make(view, "You need to confirm the user privacy policy", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }

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


        NetworkUtils.postJsonRequest(registerParameter, "/public/registration", null,new Callback() {
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
            String msg = jsonObject.getString("msg"); // 根据实际 JSON 键获取 Token
            Log.d(TAG, "info: " + msg);
            startActivity(new Intent(RegisterActivity.this, vertifyEmailShow.class));
        } else {
            showErrorDialog("Register failed", jsonObject.getString("msg"), RegisterActivity.this);
        }
    }

    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }


    private void showErrorDialog(String title, String Message, Context context) {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setTitle(title);
            builder.setMessage(Message);
            builder.setPositiveButton("OK", (dialog, which) -> {
                // 处理确定按钮点击事件
            });
            builder.create().show();
        });
    }

    private String getFormattedHtml(String content) {
        return "<html><head>"
                + "<style type=\"text/css\">body{font-family: Arial, sans-serif; padding: 5px;}</style>"
                + "</head><body>"
                + content
                + "</body></html>";
    }




}
