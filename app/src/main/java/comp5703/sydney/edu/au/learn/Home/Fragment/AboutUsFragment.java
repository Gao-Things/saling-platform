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
import comp5703.sydney.edu.au.learn.vertifyEmailShow;
import io.noties.markwon.Markwon;

public class AboutUsFragment extends Fragment {
    private View rootView;

    private Integer userId;

    private String token;

    SharedPreferences sharedPreferences;

    private ImageView backClick;

    private TextView aboutUs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_about_us, container, false);
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

            backClick = view.findViewById(R.id.backClick);
            aboutUs = view.findViewById(R.id.aboutUsText);

            backClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack(); // 返回上一级
                }
            });

            Markwon markwon = Markwon.create(Objects.requireNonNull(getContext()));

            String text = "# About Us\n" +
                    "\n" +
                    "Welcome to Metal Trading! \uD83C\uDF1F\n" +
                    "\n" +
                    "At Metal Trading, we specialize in facilitating the trade of rare metals, serving as a trusted platform for buyers and sellers alike. Our mission is to streamline the trading process, ensuring security, transparency, and satisfaction for every transaction.\n" +
                    "\n" +
                    "## Our Values\n" +
                    "\n" +
                    "- **Trust and Reliability**: We build trust with our clients through reliable and consistent service.\n" +
                    "- **Excellence in Service**: We strive to provide services that exceed expectations in the rare metals trading industry.\n" +
                    "- **Commitment to Innovation**: We embrace innovation to meet the evolving needs of the market.\n" +
                    "- **Ethical Practices**: We conduct our business according to the highest ethical and professional standards.\n" +
                    "- **Environmental Responsibility**: We are committed to environmental stewardship in all our trading activities.\n" +
                    "- **Empowerment through Knowledge**: We believe in empowering our clients with the knowledge to make informed trading decisions.\n" +
                    "\n" +
                    "## Our Journey\n" +
                    "\n" +
                    "Founded in Sydney, Metal Trading has quickly established itself as a leading platform in the niche market of rare metals. Since our inception, we've been dedicated to providing a seamless trading experience while adapting to the dynamic nature of the global market.\n" +
                    "\n" +
                    "## Meet the Team\n" +
                    "\n" +
                    "Our team at Metal Trading is comprised of industry experts with years of experience in the commodities trading sector. Each member brings a wealth of knowledge and a commitment to excellence that defines our company culture.\n" +
                    "\n" +
                    "## Get in Touch\n" +
                    "\n" +
                    "Connect with us to discover how we can facilitate your rare metals trading needs:\n" +
                    "\n" +
                    "- **Email**: contact@metaltrading.com\n" +
                    "- **Phone**: +61 (2) 1234-5678\n" +
                    "- **Address**: 123 Trade Hub, Sydney, Australia\n" +
                    "\n" +
                    "Thank you for considering Metal Trading as your partner in the rare metals market. We are eager to support your trading endeavors and contribute to your success!\n" +
                    "\n" +
                    "---\n" +
                    "\n" +
                    "© Metal Trading. All rights reserved.\n";

            markwon.setMarkdown(aboutUs, text);





        }




    @Override
    public void onResume() {
        super.onResume();
        // 当Fragment重新变为活动状态时更新Toolbar
        ((HomeUseActivity) getActivity()).updateToolbar(false, "setting");
    }



}
