package comp5703.sydney.edu.au.learn.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.util.LineChartXAxisValueFormatter;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.LinearViewHolder>{

    private Context mcontext;
    private OnItemClickListener mlistener;
    private List<Record> recordList;


    public ItemListAdapter(Context context, List<Record> recordList, OnItemClickListener listener){
        this.mcontext = context;
        this.mlistener = listener;
        this.recordList = recordList;
    }
    @NonNull
    @Override
    public ItemListAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemListAdapter.LinearViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.layout_linear_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // 初始化请求数据还没回来的时候list是空的
        if (!recordList.isEmpty()){

            // 获取Product object
            Product product = recordList.get(position).getProduct();
            holder.itemName.setText(product.getProductName());
            holder.itemPrice.setText(Double.toString(product.getProductPrice()));
            holder.itemTransferPrice.setText(Double.toString(product.getProductExchangePrice()));
            System.out.println(product);
            // 获取productPriceHistory
            List<Double> productHistoryPrice = recordList.get(position).getPriceUpdateRecord();

            List<Long> productHistoryTime = recordList.get(position).getPriceUpdateTime();

            Integer setStatus = 2;
            if (product.getPriceStatus() !=null){
                setStatus = product.getPriceStatus();
            }
            // 设置图表
            initialLineChart(holder.lineChart, productHistoryPrice, productHistoryTime, setStatus);
        }



        holder.lineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mlistener.onClick(position);
            }
        });

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

        if(priceStatus == 1){
            dataSet.setColor(Color.RED); // 折线的颜色
            dataSet.setFillDrawable(ContextCompat.getDrawable(mcontext, R.drawable.gradient_red)); // 设置填充区域的Drawable资源
        }else {
            dataSet.setColor(Color.GREEN); // 折线的颜色
            dataSet.setFillDrawable(ContextCompat.getDrawable(mcontext, R.drawable.gradient_green)); // 设置填充区域的Drawable资源
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


    @Override
    public int getItemCount() {
        return recordList.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView itemName;

        private TextView itemPrice;

        private TextView itemTransferPrice;

        private LineChart lineChart;

        public LinearViewHolder(View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemTransferPrice = itemView.findViewById(R.id.itemTransferPrice);
            lineChart = itemView.findViewById(R.id.line_chart);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }
}
