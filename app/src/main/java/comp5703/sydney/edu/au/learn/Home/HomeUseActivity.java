package comp5703.sydney.edu.au.learn.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import comp5703.sydney.edu.au.learn.Common.HeaderFragment;
import comp5703.sydney.edu.au.learn.R;

public class HomeUseActivity extends AppCompatActivity {
    private HeaderFragment headerFragment;
    private ItemListFragment itemListFragment;
    private ItemListEditFragment itemListEditFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_use);

        // initial item list fragment
        headerFragment = new HeaderFragment();
        itemListFragment = new ItemListFragment();

        // fragment添加到Activity中
        getSupportFragmentManager().beginTransaction().add(R.id.fl_header, headerFragment, "header").commitAllowingStateLoss();
//        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, itemListFragment, "itemList").commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(new HomeFragment());
                    return true;
                case R.id.navigation_dashboard:
                    loadFragment(new ItemListEditFragment());
                    return true;
                case R.id.navigation_notifications:
                    loadFragment(new ItemDetailFragment());
                    return true;
            }
            return false;
        });

        // 默认加载一个Fragment
        loadFragment(new HomeFragment());

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

}