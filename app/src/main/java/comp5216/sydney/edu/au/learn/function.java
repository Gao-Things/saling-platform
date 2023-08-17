package comp5216.sydney.edu.au.learn;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

import comp5216.sydney.edu.au.learn.ListView.ListViewActivity;
import comp5216.sydney.edu.au.learn.gridview.GridViewActivity;
import comp5216.sydney.edu.au.learn.recyclerView.RecyclerViewActivity;


public class function extends AppCompatActivity {

    // 以下为学习内容
    private Button mBtnListView;

    private Button mBtnGridView;

    private Button mBtnRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);


        // 学习部分
        mBtnListView = (Button) findViewById(R.id.btn_listview);
        mBtnListView.setOnClickListener(this::onClick);

        mBtnGridView = findViewById(R.id.btn_gridview);
        mBtnGridView.setOnClickListener(this::onClick);

        mBtnRecyclerView = findViewById(R.id.btn_recyclerView);
        mBtnRecyclerView.setOnClickListener(this::onClick);
    }


    private void onClick(View view){
        Intent intent = null;
        System.out.println(view.getId());
        Log.d(TAG, String.valueOf(view.getId()));
        if(R.id.btn_listview == view.getId()){
            intent = new Intent(function.this, ListViewActivity.class);
            startActivity(intent); // Start the activity associated with the intent
        }
        if(R.id.btn_gridview == view.getId()){
            intent = new Intent(function.this, GridViewActivity.class);
            startActivity(intent); // Start the activity associated with the intent
        }
        if(R.id.btn_recyclerView == view.getId()){
            intent = new Intent(function.this, RecyclerViewActivity.class);
            startActivity(intent); // Start the activity associated with the intent
        }

    }

}