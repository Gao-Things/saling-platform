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
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import comp5703.sydney.edu.au.learn.R;

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

            elegant.setOnClickListener(this::selectTone);
            Playful.setOnClickListener(this::selectTone);
            Crisp.setOnClickListener(this::selectTone);
            electronic.setOnClickListener(this::selectTone);
            rock.setOnClickListener(this::selectTone);
        }

        private void selectTone(View view) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            switch (view.getId()) {
                case R.id.elegant:
                    // 处理elegant音效的逻辑
                    // 获取系统默认的通知音频的 URI
                    Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), notificationSound);
                    ringtone.play();
                    break;

                case R.id.Playful:
                    // 处理Playful音效的逻辑
                    Uri customSound2 = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.meng);
                    Ringtone ringtone3 = RingtoneManager.getRingtone(getActivity(), customSound2);
                    ringtone3.play();

                    editor.putInt("selectTone", R.raw.meng);

                    break;

                case R.id.Crisp:
                    // 处理Crisp音效的逻辑
                    Uri customSound = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.y831);
                    Ringtone ringtone2 = RingtoneManager.getRingtone(getActivity(), customSound);
                    ringtone2.play();

                    editor.putInt("selectTone", R.raw.y831);



                    break;

                case R.id.electronic:
                    // 处理electronic音效的逻辑

                    break;

                case R.id.rock:
                    // 处理rock音效的逻辑

                    break;

                default:
                    break;

            }   switch (view.getId()) {
                case R.id.elegant:
                    // 处理elegant音效的逻辑

                    break;

                case R.id.Playful:
                    // 处理Playful音效的逻辑

                    break;

                case R.id.Crisp:
                    // 处理Crisp音效的逻辑

                    break;

                case R.id.electronic:
                    // 处理electronic音效的逻辑

                    break;

                case R.id.rock:
                    // 处理rock音效的逻辑

                    break;

                default:
                    break;

            }

        }


}
