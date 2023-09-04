package comp5703.sydney.edu.au.learn.Common;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.util.ImageUtils;

public class HeaderFragment extends Fragment {

    private TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.tv_title);
        ImageView imageView = view.findViewById(R.id.profileImage);

// 获取要设置为圆形的图片资源
        Drawable originalDrawable = getResources().getDrawable(R.drawable.img_6);

// 创建一个圆形的BitmapDrawable
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();
        Bitmap circularBitmap = ImageUtils.getCircularBitmap(originalBitmap);
        Drawable circularDrawable = new BitmapDrawable(getResources(), circularBitmap);

// 设置圆形图片
        imageView.setImageDrawable(circularDrawable);

    }
}
