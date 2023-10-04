package comp5703.sydney.edu.au.learn;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import comp5703.sydney.edu.au.learn.ListView.ListViewActivity;
import comp5703.sydney.edu.au.learn.gridview.GridViewActivity;
import comp5703.sydney.edu.au.learn.recyclerView.RecyclerViewActivity;


public class function extends AppCompatActivity {

    // 以下为学习内容
    private Button mBtnListView;

    private Button mBtnGridView;

    private Button mBtnRecyclerView;

    private LineChart lineChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        lineChart = findViewById(R.id.line_chart);

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
        entries.add(new Entry(1, 10));
        entries.add(new Entry(2, 20));
        entries.add(new Entry(3, 30));
        entries.add(new Entry(4, 25));
        entries.add(new Entry(5, 10));
        entries.add(new Entry(6, 20));
        entries.add(new Entry(7, 30));
        entries.add(new Entry(8, 25));


        LineDataSet dataSet = new LineDataSet(entries, "折线图数据"); // 数据集的标签
        dataSet.setColor(Color.RED); // 折线的颜色
        dataSet.setLineWidth(5f); // 折线的宽度
       dataSet.setDrawValues(false); // 禁用数据点上的标签显示
        dataSet.setDrawCircles(false); // 禁用绘制节点圆圈
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 使用立方贝塞尔模式绘制平滑的曲线


        dataSet.setDrawFilled(true); // 启用填充
        dataSet.setFillAlpha(100); // 设置填充颜色的透明度
        dataSet.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.gradient_red)); // 设置填充区域的Drawable资源

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



        // 学习部分
        mBtnListView = (Button) findViewById(R.id.btn_listview);
        mBtnListView.setOnClickListener(this::onClick);

        mBtnGridView = findViewById(R.id.btn_gridview);
        mBtnGridView.setOnClickListener(this::onClick);

        mBtnRecyclerView = findViewById(R.id.btn_recyclerView);
        mBtnRecyclerView.setOnClickListener(this::onClick);
    }


    private void onClick(View view){
        Intent intent = null;
        System.out.println(view.getId());
        Log.d(TAG, String.valueOf(view.getId()));
        if(R.id.btn_listview == view.getId()){
            intent = new Intent(function.this, ListViewActivity.class);
            startActivity(intent); // Start the activity associated with the intent
        }
        if(R.id.btn_gridview == view.getId()){
            intent = new Intent(function.this, GridViewActivity.class);
            startActivity(intent); // Start the activity associated with the intent
        }
        if(R.id.btn_recyclerView == view.getId()){
            intent = new Intent(function.this, RecyclerViewActivity.class);
            startActivity(intent); // Start the activity associated with the intent
        }

    }

}