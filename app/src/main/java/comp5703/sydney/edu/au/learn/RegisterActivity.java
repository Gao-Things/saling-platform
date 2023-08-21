package comp5703.sydney.edu.au.learn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.FirebaseAuth;


import comp5703.sydney.edu.au.learn.util.FormValidator;

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

                if(inputLayoutEmail.getEditText().getText().toString().trim().length()>15){
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
        }

    }


    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        sendVerificationEmail(user); // 发送验证邮件

                    } else {
                        // 注册失败
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RegisterActivity.this, "此邮箱已被注册", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void sendVerificationEmail(FirebaseUser user) {
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setTitle("Successful !!")
                                    .setMessage("The vertified Email has been send")
                                    .setPositiveButton("Back", (dialogInterface, i) -> {

                                    });

                            builder.create().show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "无法发送验证邮件，请稍后再试", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }


}