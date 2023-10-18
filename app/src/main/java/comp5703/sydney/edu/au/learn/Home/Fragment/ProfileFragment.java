package comp5703.sydney.edu.au.learn.Home.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import comp5703.sydney.edu.au.learn.MainActivity;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.service.MyService;
import comp5703.sydney.edu.au.learn.util.DialogUtil;

public class ProfileFragment extends Fragment {
    private View rootView;

    private Integer userId;

    private String token;

    private CardView myOfferCardView;
    
    private CardView myMessageCardView;

    private CardView logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        // get SharedPreferences instance
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);

        // get global userID
        userId = sharedPreferences.getInt("userId", 9999);
        token = sharedPreferences.getString("token", "null");

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myOfferCardView = view.findViewById(R.id.myOfferCardView);
        myMessageCardView = view.findViewById(R.id.myMessageCardView);
        logout = view.findViewById(R.id.logout);
        myOfferCardView.setOnClickListener(this::dumpToOfferContainer);
        myMessageCardView.setOnClickListener(this::dumpToMyMessage);
        logout.setOnClickListener(this::logout);

    }

    private void logout(View view) {

        DialogUtil.showCustomDialog(
                Objects.requireNonNull(getActivity()),
                getContext(),
                "Do you confirm to log out ?",
                v -> {
                    // Handle the logic for logging out
                    clearUserData();
                },null
                );

    }

    private void clearUserData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("comp5703", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userId");
        editor.remove("token");
        editor.apply();


        // 停止消息推送服务
        Intent serviceIntent = new Intent(getActivity(), MyService.class);
        getActivity().stopService(serviceIntent);


        // 重定向到登录页面
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // 关闭当前活动
        Objects.requireNonNull(getActivity()).finish();
    }



    private void dumpToMyMessage(View view) {

        MessagesFragment fragmentB = new MessagesFragment();

        // 准备要传递的数据
        Bundle args = new Bundle();
        args.putInt("userId", userId); // 这里的 "key" 是你传递数据的键名，"value" 是你要传递的值

        // 执行 Fragment 跳转
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, fragmentB); // R.id.fragment_container 是用于放置 Fragment 的容器
        transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
        transaction.commit();
    }

    private void dumpToOfferContainer(View view) {
        // 在 FragmentA 中
        OfferContainerFragment fragmentB = new OfferContainerFragment();

//        // 准备要传递的数据
//        Bundle args = new Bundle();
//        args.putString("Category", category); // 这里的 "key" 是你传递数据的键名，"value" 是你要传递的值

        // 执行 Fragment 跳转
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, fragmentB); // R.id.fragment_container 是用于放置 Fragment 的容器
        transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
        transaction.commit();
    }
}
