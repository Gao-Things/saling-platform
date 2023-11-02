package comp5703.sydney.edu.au.learn.Home.DialogFragment;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.slider.RangeSlider;

import java.io.IOException;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.MinMaxWeight;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SortDialogFragment extends DialogFragment {


    private Integer sortByPurity = -1;

    private Integer sortByWeight = -1;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用AlertDialog.Builder构建弹窗
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        // 获取布局填充器
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // 弹窗使用的布局
        View filterView = inflater.inflate(R.layout.sort_layout, null);

        // 获取单选按钮组
        RadioGroup purityGroup = filterView.findViewById(R.id.sort_by_purity_group);
        RadioGroup priceGroup = filterView.findViewById(R.id.sort_by_price_group);




        purityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.sort_by_purity_asc) {
                    sortByPurity = 0; // 假设 0 代表升序
                } else if (checkedId == R.id.sort_by_purity_desc) {
                    sortByPurity = 1; // 假设 1 代表降序
                }
            }
        });

        // 为价格单选按钮组添加监听器
        priceGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.sort_by_price_asc) {
                    sortByWeight = 0; // 假设 0 代表从低到高
                } else if (checkedId == R.id.sort_by_price_desc) {
                    sortByWeight = 1; // 假设 1 代表从高到低
                }
            }
        });



        // 设置弹窗标题、视图和按钮
        builder.setView(filterView)
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // 用户确认筛选，处理筛选逻辑
                        applyFilter(sortByPurity, sortByWeight);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 用户取消筛选，关闭弹窗
                        SortDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }


    // 定义接口
    public interface SortDialogListener {
        void onSortClosed();
        void onSortApplied(Integer sortByPurity, Integer sortByWeight);
    }


    private SortDialogListener listener;

    // 设置监听器
    public void setSortDialogListener(SortDialogListener listener) {
        this.listener = listener;
    }

    private void applyFilter(Integer sortByPurity, Integer sortByWeight ) {

        if (listener != null) {
            listener.onSortApplied(sortByPurity, sortByWeight);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onSortClosed();
        }
    }
}
