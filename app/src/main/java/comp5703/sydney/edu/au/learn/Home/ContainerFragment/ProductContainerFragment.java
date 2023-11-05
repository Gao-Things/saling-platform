package comp5703.sydney.edu.au.learn.Home.ContainerFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.card.MaterialCardView;

import comp5703.sydney.edu.au.learn.Home.Adapter.FragmentOfferAdapter;
import comp5703.sydney.edu.au.learn.Home.Adapter.FragmentProductAdapter;
import comp5703.sydney.edu.au.learn.R;

public class ProductContainerFragment extends Fragment {

    private TextView sendOfferTitle;
    private MaterialCardView myOfferBox;

    private TextView receiveOfferTitle;
    private MaterialCardView receiveOfferBox;
    public ProductContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_container, container, false);

        sendOfferTitle = view.findViewById(R.id.sendOfferTitle);
        myOfferBox = view.findViewById(R.id.myOfferBox);

        receiveOfferTitle = view.findViewById(R.id.receiveOfferTitle);
        receiveOfferBox = view.findViewById(R.id.receiveOfferBox);


        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        FragmentProductAdapter adapter = new FragmentProductAdapter(this);
        viewPager.setAdapter(adapter);

        // Set a PageChangeCallback to update the header based on the current page
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateHeader(position);
            }
        });

        // Initial header setup
        updateHeader(viewPager.getCurrentItem());

        return view;
    }

    private void updateHeader(int position) {
        if (position == 0) {
            sendOfferTitle.setTextColor(getResources().getColor(R.color.black));
            myOfferBox.setBackgroundColor(getResources().getColor(R.color.yellow));

            receiveOfferTitle.setTextColor(getResources().getColor(R.color.cardViewWithoutSelected));
            receiveOfferBox.setBackgroundColor(getResources().getColor(R.color.cardViewWithoutSelected));
        } else if (position == 1) {
            receiveOfferTitle.setTextColor(getResources().getColor(R.color.black));
            receiveOfferBox.setBackgroundColor(getResources().getColor(R.color.yellow));

            sendOfferTitle.setTextColor(getResources().getColor(R.color.cardViewWithoutSelected));
            myOfferBox.setBackgroundColor(getResources().getColor(R.color.cardViewWithoutSelected));

        }
    }
}

