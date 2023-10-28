package comp5703.sydney.edu.au.learn.Home.Adapter;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.DTO.ProductUser;
import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.util.LineChartXAxisValueFormatter;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.LinearViewHolder>{

    private Context mcontext;
    private OnItemClickListener mlistener;
    private List<ProductUser> recordList;


    public ItemListAdapter(Context context, List<ProductUser> recordList, OnItemClickListener listener){
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
            ProductUser product = recordList.get(position);
            holder.itemName.setText(product.getProductName());
            holder.itemWeight.setText(  product.getProductWeight() + " g");
            holder.itemSellerName.setText( product.getName());

            // 把图片链接字符串转回list
            String[] items = product.getProductImage().substring(1, product.getProductImage().length() - 1).split(", ");

            List<String> imageUrlList = new ArrayList<>();
            for (String item : items) {
                imageUrlList.add(item);
            }

            Picasso.get()
                    .load(imageURL+imageUrlList.get(0)) // 网络图片的URL,加载第一张图片
                    .into(holder.itemImage);



            // 绑定点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mlistener.onClick(position, product.getId());
                }
            });
        }



    }



    @Override
    public int getItemCount() {
        return recordList.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView itemName;


        private TextView itemWeight;

        private ImageView itemImage;

        private TextView itemSellerName;

        public LinearViewHolder(View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemWeight = itemView.findViewById(R.id.itemWeight);
            itemImage = itemView.findViewById(R.id.ItemImage);
            itemSellerName = itemView.findViewById(R.id.itemSellerName);

        }
    }

    public interface OnItemClickListener{
        void onClick(int pos, Integer itemId);
    }

    public List<ProductUser> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<ProductUser> recordList) {
        this.recordList = recordList;
    }
}
