package comp5703.sydney.edu.au.learn.Common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import comp5703.sydney.edu.au.learn.R;


public class DialogFragment extends androidx.fragment.app.DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {



        // 使用布局填充器获取自定义布局
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);

        // 创建自定义对话框
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(dialogView);
        // 获取屏幕宽度
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        // 计算对话框宽度为父容器宽度的70%
        int dialogWidth = (int) (screenWidth * 0.85);

        // 设置对话框居中显示
        dialog.getWindow().setGravity(Gravity.CENTER);
        // 设置对话框的属性，例如宽度、高度和其他样式
        dialog.getWindow().setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
        // 设置其他属性，例如圆角等
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);

        return dialog;
    }


}
