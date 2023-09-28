package comp5703.sydney.edu.au.learn.Home.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import comp5703.sydney.edu.au.learn.Home.Fragment.MyOfferFragment;
import comp5703.sydney.edu.au.learn.Home.Fragment.ReceivedOfferFragment;

public class FragmentOfferAdapter extends FragmentStateAdapter {
    public FragmentOfferAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyOfferFragment();
            case 1:
                return new ReceivedOfferFragment();
            default:
                return new MyOfferFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

