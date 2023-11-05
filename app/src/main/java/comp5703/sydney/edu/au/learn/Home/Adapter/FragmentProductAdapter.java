package comp5703.sydney.edu.au.learn.Home.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import comp5703.sydney.edu.au.learn.Home.Fragment.MyProductOnSellFragment;
import comp5703.sydney.edu.au.learn.Home.Fragment.MyProductUnavaliableFragment;

public class FragmentProductAdapter extends FragmentStateAdapter {
    public FragmentProductAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new MyProductUnavaliableFragment();
            default:
                return new MyProductOnSellFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

