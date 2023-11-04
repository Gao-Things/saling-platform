package comp5703.sydney.edu.au.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import io.noties.markwon.Markwon;

public class vertifyEmailShow extends AppCompatActivity {

    private TextView mentionText;
    private MaterialButton backBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertify_email_show);

        mentionText = findViewById(R.id.mentionText);
        backBtn = findViewById(R.id.backButton);
        Markwon markwon = Markwon.create(vertifyEmailShow.this);

        String text = "- **A verification email has been sent.**\n" +
                "- Please click the link included to **verify your email address**.\n";

        markwon.setMarkdown(mentionText, text);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(vertifyEmailShow.this, MainActivity.class));
            }
        });
    }
}