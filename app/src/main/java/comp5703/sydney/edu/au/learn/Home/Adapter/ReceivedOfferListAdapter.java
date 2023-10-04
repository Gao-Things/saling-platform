package comp5703.sydney.edu.au.learn.Home.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.R;

public class ReceivedOfferListAdapter extends RecyclerView.Adapter<ReceivedOfferListAdapter.LinearViewHolder>{

    private Context mcontext;
    private OnItemClickListener mlistener;
    private List<Record> recordList;


    public ReceivedOfferListAdapter(Context context, List<Record> recordList, OnItemClickListener listener){
        this.mcontext = context;
        this.mlistener = listener;
        this.recordList = recordList;
    }
    @NonNull
    @Override
    public ReceivedOfferListAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReceivedOfferListAdapter.LinearViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.layout_linear_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // 初始化请求数据还没回来的时候list是空的
        if (!recordList.isEmpty()){

            // 获取Product object
            Product product = recordList.get(position).getProduct();
            holder.itemName.setText(product.getProductName());
            holder.itemWeight.setText(String.valueOf(product.getProductWeight()));

            Picasso.get()
                    .load(product.getProductImage()) // 网络图片的URL
                    .into(holder.itemImage);


            double productPrice = product.getProductPrice();
            double productExchangePrice = product.getProductExchangePrice();

            // 创建DecimalFormat对象，设置科学计数法格式
            DecimalFormat decimalFormat = new DecimalFormat("0.##E0");

            // 使用科学计数法格式化价格
            String formattedPrice = decimalFormat.format(productPrice);
            String formattedExchangePrice = decimalFormat.format(productExchangePrice);

            holder.itemPrice.setText(formattedPrice);


            System.out.println(product);
            // 获取productPriceHistory
            List<Double> productHistoryPrice = recordList.get(position).getPriceUpdateRecord();

            List<Long> productHistoryTime = recordList.get(position).getPriceUpdateTime();

            Integer setStatus = 2;
            if (product.getPriceStatus() !=null){
                setStatus = product.getPriceStatus();
            }

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

        private TextView itemPrice;

        private TextView itemWeight;

        private ImageView itemImage;

        public LinearViewHolder(View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemWeight = itemView.findViewById(R.id.itemWeight);
            itemImage = itemView.findViewById(R.id.ItemImage);

        }
    }

    public interface OnItemClickListener{
        void onClick(int pos, Integer itemId);
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }
}
