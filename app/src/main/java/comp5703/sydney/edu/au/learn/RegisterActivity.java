package comp5703.sydney.edu.au.learn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    public EditText email;

    public EditText password;

    private Button registerBtn;

    private FirebaseAuth mAuth;

    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // 找到控件
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
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

    }

    private void registerClick(View view) {
        String emailUse = email.getText().toString();
        String passwordUse = password.getText().toString();

        if (emailUse.isEmpty() || passwordUse.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
        } else {
            registerUser(emailUse, passwordUse);
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