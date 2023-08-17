package comp5703.sydney.edu.au.learn.ListView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;

import comp5703.sydney.edu.au.learn.R;

public class ListViewActivity extends Activity {

    private ListView mlv1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liststudy);
        mlv1 = (ListView)findViewById(R.id.lv_1);
        mlv1.setAdapter(new MyListAdapter(ListViewActivity.this));

    }
}
