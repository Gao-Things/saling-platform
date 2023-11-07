package comp5703.sydney.edu.au.learn.Home.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kyleduo.switchbutton.SwitchButton;

import java.util.Objects;

import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.R;

import comp5703.sydney.edu.au.learn.service.MyService;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;

public class SettingFragment extends Fragment {
    private static final int REQUEST_CODE_OVERLAY_PERMISSION = 123;
    private View rootView;

    private Integer userId;

    private String token;

    private LinearLayout selectTone;
    
    private LinearLayout aboutUs;
    
    private SwitchButton switchPopup;

    private SwitchButton switchMessage;

    private SwitchButton switchMessageToneOpen;
    
    private TextView userSelectedTone;

    // 用户设置
    private boolean messageReceived;
    private boolean messageToneOpen;
    private boolean notificationOpen;
    private String chooseTone;

    private SharedPreferences sharedPreferences;

    private ImageView backClick;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        // get SharedPreferences instance
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);

        // get global userID
        userId = sharedPreferences.getInt("userId", 9999);
        token = sharedPreferences.getString("token", "null");

        messageReceived = sharedPreferences.getBoolean("messageReceived", false);
        messageToneOpen = sharedPreferences.getBoolean("messageToneOpen", false);
        notificationOpen = sharedPreferences.getBoolean("notificationOpen", false);
        chooseTone = sharedPreferences.getString("chooseTone", "Elegant");


        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectTone = view.findViewById(R.id.selectTone);

        switchMessage = view.findViewById(R.id.switchMessage);
        switchPopup = view.findViewById(R.id.switchPopup);
        switchMessageToneOpen = view.findViewById(R.id.switchMessageToneOpen);
        userSelectedTone = view.findViewById(R.id.userSelectedTone);
        aboutUs = view.findViewById(R.id.aboutUs);

        backClick = view.findViewById(R.id.backClick);

        backClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(); // 返回上一级
            }
        });

        // 获取用户设置状态
        setUserSetting(switchMessage, switchPopup, switchMessageToneOpen);

        // 设置监听器
        SwitchButton.OnCheckedChangeListener listener = new SwitchButton.OnCheckedChangeListener() {

            // 使用编辑器进行修改
            @SuppressLint("CommitPrefEdits")
            SharedPreferences.Editor editor = sharedPreferences.edit();


            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.switchMessage:
                        updateButtonColor(isChecked, switchMessage);
                        editor.putBoolean("messageReceived", isChecked);
                        editor.apply();

                        break;
                    case R.id.switchPopup:
                        // 处理switchPopup的状态变化
                        // 这里需求请求系统权限

                        if (modifyService(isChecked)){
                            updateButtonColor(isChecked, switchPopup);
                            editor.putBoolean("notificationOpen", isChecked);
                            editor.apply();
                        }


                        break;
                    case R.id.switchMessageToneOpen:
                        // 处理switchMessage2的状态变化
                        updateButtonColor(isChecked, switchMessageToneOpen);

                        editor.putBoolean("messageToneOpen", isChecked);
                        editor.apply();
                        break;
                }

                // 发送请求更新用户设置
                NetworkUtils.updateUserSetting(sharedPreferences);
            }
        };

        switchMessage.setOnCheckedChangeListener(listener);
        switchPopup.setOnCheckedChangeListener(listener);
        switchMessageToneOpen.setOnCheckedChangeListener(listener);
        

        selectTone.setOnClickListener(this::dumpToToneSelect);

        aboutUs.setOnClickListener(this::dumpToAboutUs);
        

    }

    private void dumpToAboutUs(View view) {

        // 在 FragmentA 中
        AboutUsFragment fragmentB = new AboutUsFragment();

        // 执行 Fragment 跳转
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, fragmentB); // R.id.fragment_container 是用于放置 Fragment 的容器
        transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
        transaction.commit();

    }

    private boolean modifyService(boolean isOpen) {
        // 如果是打开推送service，就要查看是否有系统权限

        HomeUseActivity activity = (HomeUseActivity) getActivity();
        assert activity != null;

        if (isOpen){
            // 开启推送service
            activity.createNotification(true);

        }else {
            // 关闭推送service
            activity.stopMyService();

        }




        return true;
    }


    private void setUserSetting(SwitchButton switchMessage, SwitchButton switchPopup, SwitchButton switchMessageToneOpen){
        switchMessage.setChecked(messageReceived);
        updateButtonColor(messageReceived, switchMessage);


        switchPopup.setChecked(notificationOpen);
        updateButtonColor(notificationOpen, switchPopup);

        switchMessageToneOpen.setChecked(messageToneOpen);
        updateButtonColor(messageToneOpen, switchMessageToneOpen);

        userSelectedTone.setText(chooseTone);

    }

    // 更新按钮颜色
    private void updateButtonColor(boolean isChecked, SwitchButton switchButton) {
        int backColor;
        if (isChecked) {
            backColor = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.generalGreen);
        } else {
            backColor = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.textColorSecondary);
        }
        switchButton.setBackColor(ColorStateList.valueOf(backColor));
    }
    private void dumpToToneSelect(View view) {

        // 在 FragmentA 中
        SelectToneFragment fragmentB = new SelectToneFragment();

        // 执行 Fragment 跳转
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, fragmentB); // R.id.fragment_container 是用于放置 Fragment 的容器
        transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 当Fragment重新变为活动状态时更新Toolbar
        ((HomeUseActivity) getActivity()).updateToolbar(false, "setting");
    }
}
