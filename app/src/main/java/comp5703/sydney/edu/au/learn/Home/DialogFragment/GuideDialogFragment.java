package comp5703.sydney.edu.au.learn.Home.DialogFragment;

import android.app.Dialog;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import comp5703.sydney.edu.au.learn.R;

public class GuideDialogFragment extends DialogFragment {
    private Button lowPriceButton, notSellingButton, otherButton;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // 使用LayoutInflater来加载弹窗的布局
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_guide_dialog, null);

        // 初始化布局中的组件
        ImageView imageView = view.findViewById(R.id.guide_image);

        lowPriceButton = view.findViewById(R.id.button_low_price);
        notSellingButton = view.findViewById(R.id.button_not_selling);
        otherButton = view.findViewById(R.id.button_other);


        EditText customReasonEditText = view.findViewById(R.id.edit_text_custom_reason);
        Button sendButton = view.findViewById(R.id.button_send);
        Button cancelButton = view.findViewById(R.id.button_cancel);


        // 设置按钮的点击监听器
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 首先将所有按钮设置为未选中状态
                resetButtons();
                // 将点击的按钮设置为选中状态
                v.setSelected(true);
                Button button = (Button) v;
                // 改变按钮背景颜色
                button.getBackground().setColorFilter(new PorterDuffColorFilter(
                        getResources().getColor(R.color.darkGreen), PorterDuff.Mode.SRC_IN));
                // 改变按钮文本颜色
                button.setTextColor(getResources().getColor(android.R.color.white));
            }
        };

        lowPriceButton.setOnClickListener(buttonClickListener);
        notSellingButton.setOnClickListener(buttonClickListener);
        otherButton.setOnClickListener(buttonClickListener);

        // 发送按钮的监听器
        sendButton.setOnClickListener(v -> {
            // 发送用户理由
            String customReason = customReasonEditText.getText().toString();
            // 处理发送逻辑
        });

        // 取消按钮的监听器
        cancelButton.setOnClickListener(v -> {
            // 取消操作并关闭弹窗
            dismiss();
        });

        // 创建并返回一个使用Material Design风格的AlertDialog
        return new MaterialAlertDialogBuilder(requireActivity())
                .setView(view)
                .create();
    }

    private void resetButtons() {
        // 重置所有按钮到未选中状态
        Button[] buttons = new Button[]{lowPriceButton, notSellingButton, otherButton};
        for (Button button : buttons) {
            button.setSelected(false);
            button.getBackground().clearColorFilter();
            // 重置按钮文本颜色到默认状态
            button.setTextColor(getResources().getColor(R.color.black));
        }
    }
}

