package comp5703.sydney.edu.au.learn.Home;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.productDetailParameter;
import comp5703.sydney.edu.au.learn.util.LineChartXAxisValueFormatter;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ItemDetailFragment extends Fragment {
    private TextView itemDetailPrice;
    private LineChart itemDetailLineChart;
    private EditText maximumPrice;
    private EditText minimumPrice;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemDetailPrice = view.findViewById(R.id.itemDetailPrice);
        itemDetailLineChart = view.findViewById(R.id.itemDetail_line_chart);
        maximumPrice = view.findViewById(R.id.maximumPrice);
        minimumPrice = view.findViewById(R.id.minimumPrice);
        // 在 ItemDetailFragment 中获取传递的整数值
        Bundle args = getArguments();
        if (args != null) {
            int productId = args.getInt("productId");
            // 在这里可以使用 productId 进行操作
            Toast.makeText(getContext(), "click"+ productId, Toast.LENGTH_SHORT).show();

            productDetailParameter productDetailParameter = new productDetailParameter();
            productDetailParameter.setProductId(productId);
            // send request to backend
            NetworkUtils.getWithParamsRequest( productDetailParameter, "/public/product/getProductDetail", new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    handleResponse(response);
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    handleFailure(e);
                }
            });

        }
    }


    private void handleResponse(Response response) {
        try {
            if (!response.isSuccessful()) {
                Log.d(TAG, "Request not successful");
                return;
            }

            String responseBody = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(responseBody);
            int code = jsonObject.getIntValue("code");

            if (code == 200) {
                Product product = jsonObject.getJSONObject("data").getJSONObject("product").toJavaObject(Product.class);

                JSONArray priceUpdateTimeArray = jsonObject.getJSONObject("data").getJSONArray("priceUpdateTime");
                List<Long> priceUpdateTimeList = priceUpdateTimeArray.toJavaList(Long.class);

                JSONArray priceUpdateRecord = jsonObject.getJSONObject("data").getJSONArray("priceUpdateRecord");
                List<Double> priceUpdateRecordList = priceUpdateRecord.toJavaList(Double.class);

                // 在主线程中更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemDetailPrice.setText(Double.toString(product.getProductPrice()));
                        // 设置图表
                        initialLineChart(itemDetailLineChart, priceUpdateRecordList, priceUpdateTimeList, product.getPriceStatus());
                    }
                });
            } else {
                Log.d(TAG, "Error response code: " + code);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, "JSONException: " + e.getMessage());
        }
    }

    private void runOnUiThread(Runnable runnable) {
        // 使用Activity或Fragment的runOnUiThread方法来确保在主线程上运行代码
        getActivity().runOnUiThread(runnable);
    }


    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }



    private void initialLineChart(LineChart lineChart, List<Double> YList, List<Long> XList, Integer priceStatus){

        YAxis yAxis = lineChart.getAxisLeft(); // 获取Y轴
        XAxis xAxis = lineChart.getXAxis();    // 获取X轴

        yAxis.setEnabled(false); // 禁用 Y 轴
        xAxis.setEnabled(false); // 禁用 X 轴


        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getXAxis().setDrawGridLines(true);

        // 禁用触摸交互
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);

        // 移除描述
        lineChart.getDescription().setEnabled(false);

        // 创建一个数据集并添加数据
        ArrayList<Entry> entries = new ArrayList<>();
        // 配置X轴为时间轴
        xAxis.setValueFormatter(new LineChartXAxisValueFormatter());

        for (int i = 0; i<YList.size(); i++){
            float xValue =  XList.get(i);
            float yValue = (float)YList.get(i).doubleValue();
            entries.add(new Entry(i, yValue));
        }

        LineDataSet dataSet = new LineDataSet(entries, "折线图数据"); // 数据集的标签
        dataSet.setLineWidth(3f); // 折线的宽度
        dataSet.setDrawValues(false); // 禁用数据点上的标签显示
        dataSet.setDrawCircles(false); // 禁用绘制节点圆圈
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 使用立方贝塞尔模式绘制平滑的曲线


        dataSet.setDrawFilled(true); // 启用填充
        dataSet.setFillAlpha(100); // 设置填充颜色的透明度

        if(priceStatus == 0){
            dataSet.setColor(Color.RED); // 折线的颜色
            dataSet.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.gradient_red)); // 设置填充区域的Drawable资源
        }else {
            dataSet.setColor(Color.GREEN); // 折线的颜色
            dataSet.setFillDrawable(ContextCompat.getDrawable(getContext(), R.drawable.gradient_green)); // 设置填充区域的Drawable资源
        }


        // 创建一个数据对象并将数据集添加到其中
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData lineData = new LineData(dataSets);

        // 将数据对象设置给LineChart
        lineChart.setData(lineData);

        // 设置图表描述
//        Description description = new Description();
//        description.setText("折线图示例");
//        lineChart.setDescription(description);

        // 隐藏图例
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        // 刷新图表
        lineChart.invalidate();
    }
}
