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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Offer;
import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.R;

public class MyOfferListAdapter extends RecyclerView.Adapter<MyOfferListAdapter.LinearViewHolder>{

    private Context mcontext;
    private OnItemClickListener mlistener;
    private List<Offer> offerList;


    public MyOfferListAdapter(Context context, List<Offer> offerList, OnItemClickListener listener){
        this.mcontext = context;
        this.mlistener = listener;
        this.offerList = offerList;
    }
    @NonNull
    @Override
    public MyOfferListAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyOfferListAdapter.LinearViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.layout_myoffer_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // 初始化请求数据还没回来的时候list是空的
        if (!offerList.isEmpty()){

            // 获取Product object
            Product product = offerList.get(position).getProduct();

            // 获取offer的提交时间
            Long timeStamp = offerList.get(position).getTimestamp() * 1000;  // 将时间戳转换为毫秒

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 使用 SimpleDateFormat 对象将时间戳转换为字符串
            String formattedDate = sdf.format(new Date(timeStamp));

            holder.itemName.setText(product.getProductName());
            holder.itemPrice.setText(Double.toString(product.getProductPrice()));
            holder.myOfferTime.setText(formattedDate);
            Picasso.get()
                    .load(product.getProductImage()) // 网络图片的URL
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
        return offerList.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView itemName;

        private TextView itemPrice;

        private ImageView itemImage;

        private TextView myOfferTime;

        public LinearViewHolder(View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImage = itemView.findViewById(R.id.ItemImage);
            myOfferTime = itemView.findViewById(R.id.myOfferTime);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos, Integer itemId);
    }

    public List<Offer> getRecordList() {
        return offerList;
    }

    public void setRecordList(List<Offer> offerList) {
        this.offerList = offerList;
    }
}
