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

public class FilterDialogFragment extends DialogFragment {

    private TextView priceRangeText;

    private Integer category = -1;

    private String purity = null;

    private Integer status = -1;

    private double minPrice = 0;

    private double maxPrice = 0;

    private TextView itemPurityText;

    RangeSlider rangeSlider;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用AlertDialog.Builder构建弹窗
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 获取布局填充器
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // 弹窗使用的布局
        View filterView = inflater.inflate(R.layout.filter_dialog, null);

        priceRangeText = filterView.findViewById(R.id.textview_price_range);
        itemPurityText = filterView.findViewById(R.id.itemPurityText);

        rangeSlider = filterView.findViewById(R.id.range_slider_price);



        RadioGroup categoryGroup = filterView.findViewById(R.id.category);
        RadioGroup goldTypeGroup = filterView.findViewById(R.id.goldType);
        RadioGroup sliverTypeGroup = filterView.findViewById(R.id.sliverType);
        RadioGroup statusGroup = filterView.findViewById(R.id.status);


        /**
         *
         * 从全局变量中清除筛选变量
         *
         */

        // 获取SharedPreferences.Editor对象以进行修改
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("FilterPreferences", Context.MODE_PRIVATE).edit();

        // 清除所有的数据
        editor.clear();

        // 提交更改
        editor.apply(); // 或者使用 editor.commit()


        // ... 其他初始化代码
        SharedPreferences preferences = getActivity().getSharedPreferences("FilterPreferences", Context.MODE_PRIVATE);
        int savedCategoryOld = preferences.getInt("category", -1); // 默认值为-1，表示未选中任何按钮

        String savedPurityOld = preferences.getString("purity", null);

        int saveStatusOld = preferences.getInt("status", -1); // 默认值为-1，表示未选中任何按钮
        double minPriceOld = preferences.getFloat("minPrice", 0f);
        double maxPriceOld = preferences.getFloat("maxPrice", 0f);

//        // 恢复RadioGroup状态
//        if (savedCategoryOld != -1) {
//            switch (savedCategoryOld) {
//                case 1:
//                    // 选项 1 被选中
//                    categoryGroup.check(R.id.radio_button_1);
//
//
//                    goldTypeGroup.setVisibility(View.VISIBLE);
//                    sliverTypeGroup.setVisibility(View.GONE);
//
//                    itemPurityText.setVisibility(View.VISIBLE);
//                    category = 1;
//                    break;
//                case 2:
//                    // 选项 2 被选中撒旦撒大苏打萨达萨达萨达萨达萨达萨达是
//                    categoryGroup.check(R.id.radio_button_2); 
//
//                    goldTypeGroup.setVisibility(View.GONE);
//                    sliverTypeGroup.setVisibility(View.VISIBLE);
//
//
//                    itemPurityText.setVisibility(View.VISIBLE);
//                    category = 2;
//                    break;
//            }
//        }


//        if (savedPurityOld != null) {
//            switch (savedPurityOld) {
//                case "24K":
//                    goldTypeGroup.check(R.id.gold24K);
//                    break;
//                case "21K":
//                    goldTypeGroup.check(R.id.gold21K);
//                    break;
//                case "18K":
//                    goldTypeGroup.check(R.id.gold18K);
//                    break;
//
//                case "999":
//                    sliverTypeGroup.check(R.id.sliver999);
//                    break;
//                case "925":
//                    sliverTypeGroup.check(R.id.sliver925);
//                    break;
//                case "990":
//                    sliverTypeGroup.check(R.id.sliverAlloy);
//                    break;
//            }
//        }
//
//
////        if (savedPurityOld != null && category == 2 ) {
////            switch (savedPurityOld) {
////                case "999":
////                    goldTypeGroup.check(R.id.sliver999);
////                    break;
////                case "925":
////                    goldTypeGroup.check(R.id.sliver925);
////                    break;
////                case "990":
////                    goldTypeGroup.check(R.id.sliverAlloy);
////                    break;
////            }
////        }
//
//
//
//        if (saveStatusOld != -1) {
//            switch (saveStatusOld) {
//                case 0:
//                    statusGroup.check(R.id.checkbox_selling);
//                    break;
//                case 1:
//                    statusGroup.check(R.id.checkbox_sold_out);
//                    break;
//                case 2:
//                    statusGroup.check(R.id.checkbox_close);
//                    break;
//            }
//        }
//
//
//        if (minPriceOld != 0 || maxPriceOld !=0){
//            priceRangeText.setText("Weight range: " + minPriceOld + "g" + " - " + maxPriceOld + "g");
//        }




        /**
         *
         *  筛选商品类型
         */

        categoryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 处理单选按钮的选择事件
                switch (checkedId) {
                    case R.id.radio_button_1:
                        // 选项 1 被选中
                        goldTypeGroup.setVisibility(View.VISIBLE);
                        sliverTypeGroup.setVisibility(View.GONE);

                        itemPurityText.setVisibility(View.VISIBLE);
                        category = 1;
                        break;
                    case R.id.radio_button_2:
                        // 选项 2 被选中
                        goldTypeGroup.setVisibility(View.GONE);
                        sliverTypeGroup.setVisibility(View.VISIBLE);


                        itemPurityText.setVisibility(View.VISIBLE);
                        category = 2;
                        break;

                    case R.id.radio_button_3:
                        // 全选按钮
                        goldTypeGroup.setVisibility(View.GONE);
                        sliverTypeGroup.setVisibility(View.GONE);


                        itemPurityText.setVisibility(View.GONE);

                        category = -1;
                        break;
                }
            }
        });


        /**
         *
         *  筛选金的类型
         */


        goldTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 处理单选按钮的选择事件
                switch (checkedId) {
                    case R.id.gold24K:
                        purity = "24k";
                        break;

                    case R.id.gold22K:
                        // 选项 2 被选中
                        purity = "22k";
                        break;
                    case R.id.gold21K:
                        // 选项 2 被选中
                        purity = "21K";
                        break;

                    case R.id.gold18K:
                        // 选项 2 被选中
                        purity = "18K";
                        break;

                    case R.id.goldAll:
                        // 选项 2 被选中
                        purity = null;
                        break;


                }
            }
        });

        /**
         *
         *  筛选银的类型
         */
        sliverTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 处理单选按钮的选择事件
                switch (checkedId) {
                    case R.id.sliver999:
                        purity = "999";
                        break;
                    case R.id.sliver995:
                        // 选项 2 被选中
                        purity = "995";
                        break;

                    case R.id.sliver925:
                        purity = "925";
                        break;
                    case R.id.sliver990:
                        // 选项 2 被选中
                        purity = "990";
                        break;
                    case R.id.sliverAll:
                        purity = null;

                        break;
                }
            }
        });

        statusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 处理单选按钮的选择事件
                switch (checkedId) {
                    case R.id.checkbox_selling:
                        status = 0;
                        break;
                    case R.id.checkbox_sold_out:
                        // 选项 2 被选中
                        status = 1;
                        break;
                    case R.id.checkbox_close:
                        status = 2;
                        break;

                    case R.id.checkbox_all:
                        status = -1;
                        break;
                }
            }
        });




        // 设置滑块的默认值
        // 获取商品最大最小的重量
        NetworkUtils.getWithParamsRequest(null,"/public/product/getMinMaxWeight",null, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleResponse(response);
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });



        // 设置弹窗标题、视图和按钮
        builder.setView(filterView)
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // 用户确认筛选，处理筛选逻辑
                        applyFilter();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 用户取消筛选，关闭弹窗
                        FilterDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void handleResponse(Response response) throws IOException {

        String responseBody = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");
        MinMaxWeight  minMaxWeight = jsonObject.getJSONObject("data").toJavaObject(MinMaxWeight.class);
        if (code == 200) {

            // 通知adapter数据更新
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rangeSlider.setValueFrom((float) minMaxWeight.getMinWeight());
                    rangeSlider.setValueTo((float) minMaxWeight.getMaxWeight());
                    rangeSlider.setValues((float) minMaxWeight.getMinWeight(), (float) minMaxWeight.getMaxWeight());

                    priceRangeText.setText("Weight range: " + (float) minMaxWeight.getMinWeight() + "g" + " - " + minMaxWeight.getMaxWeight() + "g");
                    rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
                        @Override
                        public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                            // 确保获取值之前列表中有两个元素
                            List<Float> values = slider.getValues();
                            if(values.size() >= 2) {
                                // 安全地使用values.get(0)和values.get(1)
                                priceRangeText.setText("Weight range: " + values.get(0) + "g" + " - " + values.get(1) + "g");
                                minPrice = values.get(0);
                                maxPrice = values.get(1);

                            } else {
                                // 处理错误或者列表中元素不足的情况
                                Log.e("FilterDialogFragment", "RangeSlider values list does not contain two elements.");
                            }
                        }
                    });
                }
            });


        } else {
            Log.d(TAG, "errowwwwwww");
        }


    }

    // 定义接口
    public interface FilterDialogListener {
        void onFilterClosed();
        void onFilterApplied(int category, String purity, int status, double minPrice, double maxPrice);
    }


    private FilterDialogListener listener;

    // 设置监听器
    public void setFilterDialogListener(FilterDialogListener listener) {
        this.listener = listener;
    }

    private void applyFilter() {

        if (listener != null) {
            listener.onFilterApplied(category, purity, status, minPrice, maxPrice);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onFilterClosed();
        }
    }
}
