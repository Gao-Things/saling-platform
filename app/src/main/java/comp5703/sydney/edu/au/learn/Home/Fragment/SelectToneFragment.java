package comp5703.sydney.edu.au.learn.Home.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;

public class SelectToneFragment extends Fragment {
    private View rootView;

    private Integer userId;

    private String token;


    private CardView elegant;
    private CardView Playful;
    private CardView Crisp;
    private CardView electronic;
    private CardView rock;
    SharedPreferences sharedPreferences;

    ImageView elegantImage, playfulImage, crispImage, electronicImage, rockImage;
    private ImageView backClick;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_toneselect, container, false);
        // get SharedPreferences instance
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);

        // get global userID
        userId = sharedPreferences.getInt("userId", 9999);
        token = sharedPreferences.getString("token", "null");

        return rootView;
    }


        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            elegant = view.findViewById(R.id.elegant);
            Playful = view.findViewById(R.id.Playful);
            Crisp = view.findViewById(R.id.Crisp);
            electronic = view.findViewById(R.id.electronic);
            rock = view.findViewById(R.id.rock);
            backClick = view.findViewById(R.id.backClick);

            elegantImage = elegant.findViewById(R.id.chooseImg);
            playfulImage = Playful.findViewById(R.id.chooseImg2);
            crispImage = Crisp.findViewById(R.id.chooseImg3);
            electronicImage = electronic.findViewById(R.id.chooseImg4);
            rockImage = rock.findViewById(R.id.chooseImg5);


            elegant.setOnClickListener(this::selectTone);
            Playful.setOnClickListener(this::selectTone);
            Crisp.setOnClickListener(this::selectTone);
            electronic.setOnClickListener(this::selectTone);
            rock.setOnClickListener(this::selectTone);

            if (sharedPreferences.getString("chooseTone",null) == null){
                elegantImage.setVisibility(View.VISIBLE);
            }else {
                switch (sharedPreferences.getString("chooseTone",null)){
                    case "elegant":
                        elegantImage.setVisibility(View.VISIBLE);
                        break;
                    case "playful":
                        playfulImage.setVisibility(View.VISIBLE);
                        break;
                    case "Crisp":
                        crispImage.setVisibility(View.VISIBLE);
                        break;
                    case "electronic":
                        electronicImage.setVisibility(View.VISIBLE);
                        break;
                    case "rock":
                        rockImage.setVisibility(View.VISIBLE);
                        break;
                }
            }

            backClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack(); // 返回上一级
                }
            });
        }

        private void selectTone(View view) {
            // 首先隐藏所有ImageView
            elegantImage.setVisibility(View.GONE);
            playfulImage.setVisibility(View.GONE);
            crispImage.setVisibility(View.GONE);
            electronicImage.setVisibility(View.GONE);
            rockImage.setVisibility(View.GONE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            switch (view.getId()) {
                case R.id.elegant:
                    elegantImage.setVisibility(View.VISIBLE);
                    // 处理elegant音效的逻辑
                    // 获取系统默认的通知音频的 URI
                    Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), notificationSound);
                    ringtone.play();

                    // 默认音效
                    editor.putString("chooseTone", "elegant");
                    editor.apply();
                    break;

                case R.id.Playful:
                    playfulImage.setVisibility(View.VISIBLE);
                    // 处理Playful音效的逻辑
                    Uri customSound2 = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.y831);
                    Ringtone ringtone3 = RingtoneManager.getRingtone(getActivity(), customSound2);
                    ringtone3.play();

                    editor.putString("chooseTone", "playful");
                    editor.apply();

                    break;

                case R.id.Crisp:
                    crispImage.setVisibility(View.VISIBLE);
                    // 处理Crisp音效的逻辑
                    Uri customSound = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.y1878);
                    Ringtone ringtone2 = RingtoneManager.getRingtone(getActivity(), customSound);
                    ringtone2.play();

                    editor.putString("chooseTone", "crisp");
                    editor.apply();


                    break;

                case R.id.electronic:
                    electronicImage.setVisibility(View.VISIBLE);
                    // 处理electronic音效的逻辑
                    Uri customSound3 = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.y2170);
                    Ringtone ringtone4 = RingtoneManager.getRingtone(getActivity(), customSound3);
                    ringtone4.play();

                    editor.putString("chooseTone", "electronic");
                    editor.apply();
                    break;

                case R.id.rock:
                    rockImage.setVisibility(View.VISIBLE);
                    // 处理rock音效的逻辑
                    // 处理electronic音效的逻辑
                    Uri customSound4 = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.y2225);
                    Ringtone ringtone5 = RingtoneManager.getRingtone(getActivity(), customSound4);
                    ringtone5.play();

                    editor.putString("chooseTone", "rock");
                    editor.apply();
                    break;

                default:
                    break;


            }

            NetworkUtils.updateUserSetting(sharedPreferences);

        }


    @Override
    public void onResume() {
        super.onResume();
        // 当Fragment重新变为活动状态时更新Toolbar
        ((HomeUseActivity) getActivity()).updateToolbar(false, "setting");
    }



}
