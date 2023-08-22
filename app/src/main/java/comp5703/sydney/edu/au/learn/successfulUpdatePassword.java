package comp5703.sydney.edu.au.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class successfulUpdatePassword extends AppCompatActivity {
    public Button btnBackUse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_update_password);

        btnBackUse = findViewById(R.id.btnBackUse);
        btnBackUse.setOnClickListener(this::onClick);

    }


    private void onClick(View view){
        startActivity(new Intent(successfulUpdatePassword.this, MainActivity.class));
    }
}