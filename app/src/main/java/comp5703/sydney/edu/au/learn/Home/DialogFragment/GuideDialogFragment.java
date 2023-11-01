package comp5703.sydney.edu.au.learn.Home.DialogFragment;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import comp5703.sydney.edu.au.learn.Common.OnSingleClickListener;
import comp5703.sydney.edu.au.learn.DTO.UserMessage;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.RejectMessage;
import comp5703.sydney.edu.au.learn.VO.userIdVO;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GuideDialogFragment extends DialogFragment {
    private Button lowPriceButton, notSellingButton, otherButton;

    private String cardTitle = "The seller did not give a reason";

    private String cardDescription;

    Integer offerId;

    Integer userId;

    private String token;

    private View rootView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // 使用LayoutInflater来加载弹窗的布局
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        rootView = inflater.inflate(R.layout.fragment_guide_dialog, null);

        // get SharedPreferences instance
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "null");

        // 获取参数
        Bundle arguments = getArguments();
        if (arguments != null) {
            offerId = arguments.getInt("offerId");
            userId = arguments.getInt("userId");
            // 使用reason参数来做你需要做的事情...
        }

        // 初始化布局中的组件
        ImageView imageView = rootView.findViewById(R.id.guide_image);

        lowPriceButton = rootView.findViewById(R.id.button_low_price);
        notSellingButton = rootView.findViewById(R.id.button_not_selling);
        otherButton = rootView.findViewById(R.id.button_other);


        EditText customReasonEditText = rootView.findViewById(R.id.edit_text_custom_reason);
        Button sendButton = rootView.findViewById(R.id.button_send);
        Button cancelButton = rootView.findViewById(R.id.button_cancel);


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

                // 为卡片设置title
                cardTitle = button.getText().toString();

            }
        };

        lowPriceButton.setOnClickListener(buttonClickListener);
        notSellingButton.setOnClickListener(buttonClickListener);
        otherButton.setOnClickListener(buttonClickListener);

        sendButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                // 发送用户理由
                cardDescription = customReasonEditText.getText().toString();

                // 处理发送逻辑
                saveAndSendCardMessage();
            }
        });

        // 取消按钮的监听器
        cancelButton.setOnClickListener(v -> {
            // 取消操作并关闭弹窗
            dismiss();
        });

        // 创建并返回一个使用Material Design风格的AlertDialog
        return new MaterialAlertDialogBuilder(requireActivity())
                .setView(rootView)
                .create();
    }

    private void saveAndSendCardMessage() {
        RejectMessage rejectMessage = new RejectMessage();

        rejectMessage.setCardTitle(cardTitle);
        rejectMessage.setUserId(userId);
        rejectMessage.setOfferId(offerId);
        if (cardDescription !=null){
          rejectMessage.setCardDescription(cardDescription);
        }

        // 调用后端接口

        NetworkUtils.postJsonRequest( rejectMessage, "/normal/message/postRejectMessage",token, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(responseBody);
                int code = jsonObject.getIntValue("code");

                if (code == 200) {
                    Snackbar.make(rootView, "Sending rejection message successfully", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    // Close the dialog
                    dismiss();
                } else {
                    Log.d(TAG, "errowwwwwww");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("插入卡片消息失败", e.getMessage());
            }
        });


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

