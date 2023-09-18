package comp5703.sydney.edu.au.learn.Home.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import comp5703.sydney.edu.au.learn.R;


public class HomeFragment extends Fragment {
    private CardView cardCrypto;
    private CardView cardMetals;
    private CardView cardCrude;
    private CardView cardTest;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardCrypto = view.findViewById(R.id.cardCrypto);
        cardCrypto.setOnClickListener(this::onClick);
        cardMetals = view.findViewById(R.id.cardMetals);
        cardMetals.setOnClickListener(this::onClick);
        cardCrude = view.findViewById(R.id.cardCrude);
        cardCrude.setOnClickListener(this::onClick);
        cardTest = view.findViewById(R.id.cardTest);
        cardTest.setOnClickListener(this::onClick);

    }

    private void onClick(View view){
        String category = "";

        if (R.id.cardCrypto == view.getId()){
            category = "crypto";
        }

        if(R.id.cardMetals == view.getId()){
            category = "metal";
        }

        if(R.id.cardCrude == view.getId()){
            category = "crude";
        }

        if(R.id.cardTest == view.getId()){
            category = "test";
        }


        // 在 FragmentA 中
        ItemListFragment fragmentB = new ItemListFragment();

        // 准备要传递的数据
        Bundle args = new Bundle();
        args.putString("Category", category); // 这里的 "key" 是你传递数据的键名，"value" 是你要传递的值
        fragmentB.setArguments(args);

        // 执行 Fragment 跳转
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, fragmentB); // R.id.fragment_container 是用于放置 Fragment 的容器
        transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
        transaction.commit();


    }
}
