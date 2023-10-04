package comp5703.sydney.edu.au.learn.gridview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import comp5703.sydney.edu.au.learn.R;


public class GridViewActivity extends AppCompatActivity {
    private GridView mGV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        mGV = (GridView) findViewById(R.id.gv_view);
        mGV.setAdapter(new myGridViewAdapter(GridViewActivity.this));
        mGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(GridViewActivity.this, "点击"+i, Toast.LENGTH_SHORT).show();
            }
        });
        mGV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(GridViewActivity.this, "长按"+i, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}