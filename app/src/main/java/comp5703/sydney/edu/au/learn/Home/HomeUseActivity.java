package comp5703.sydney.edu.au.learn.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import comp5703.sydney.edu.au.learn.Common.HeaderFragment;
import comp5703.sydney.edu.au.learn.R;

public class HomeUseActivity extends AppCompatActivity {
    private HeaderFragment headerFragment;
    private ItemListFragment itemListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_use);

        // initial item list fragment
        headerFragment = new HeaderFragment();
        itemListFragment = new ItemListFragment();

        // fragment添加到Activity中
        getSupportFragmentManager().beginTransaction().add(R.id.fl_header, headerFragment).commitAllowingStateLoss();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, itemListFragment).commitAllowingStateLoss();

    }
}