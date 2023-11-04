package comp5703.sydney.edu.au.learn.Home.Adapter;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Offer;
import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.R;

public class MyOfferListAdapter extends RecyclerView.Adapter<MyOfferListAdapter.LinearViewHolder>{

    private Context mcontext;
    private OnItemClickListener mlistener;
    private List<Offer> offerList;
    private OnCancelClickListener cancelClickListener;

    public MyOfferListAdapter(Context context, List<Offer> offerList, OnItemClickListener listener, OnCancelClickListener cancelClickListener){
        this.mcontext = context;
        this.mlistener = listener;
        this.offerList = offerList;
        this.cancelClickListener = cancelClickListener;
    }
    @NonNull
    @Override
    public MyOfferListAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyOfferListAdapter.LinearViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.layout_myoffer_item, parent, false));
    }

    @SuppressLint({"SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // 初始化请求数据还没回来的时候list是空的
        if (!offerList.isEmpty()){

            // 获取Product object
            Product product = offerList.get(position).getProduct();

            // 获取offer的提交时间
            Long timeStamp = offerList.get(position).getTimestamp();

            Integer offerStatus = offerList.get(position).getOfferStatus();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 使用 SimpleDateFormat 对象将时间戳转换为字符串
            String formattedDate = sdf.format(new Date(timeStamp));

            holder.itemName.setText(product.getProductName());
            holder.itemPrice.setText(Double.toString(offerList.get(position).getPrice()));
            holder.myOfferTime.setText(formattedDate);

            // 把图片链接字符串转回list
            String[] items = product.getProductImage().substring(1, product.getProductImage().length() - 1).split(", ");

            List<String> imageUrlList = new ArrayList<>();
            for (String item : items) {
                imageUrlList.add(item);
            }

            Picasso.get()
                    .load(imageURL+imageUrlList.get(0)) // 网络图片的URL,加载第一张图片
                    .into(holder.itemImage);


            if (offerStatus==0){
                holder.offerStatus.setText("Pending");
                holder.offerStatus.setTextColor(mcontext.getResources().getColor(R.color.yellow));
                holder.resentButton.setVisibility(View.VISIBLE);
                holder.cancelClick.setVisibility(View.VISIBLE);

            }
            if (offerStatus == 1){
                holder.offerStatus.setText("Accepted");
                holder.offerStatus.setTextColor(mcontext.getResources().getColor(R.color.Green));
                holder.resentButton.setVisibility(View.INVISIBLE);
                holder.cancelClick.setVisibility(View.INVISIBLE);

            }

            if (offerStatus==2){
                holder.offerStatus.setText("Rejected");
                holder.offerStatus.setTextColor(mcontext.getResources().getColor(R.color.red));
                holder.resentButton.setVisibility(View.VISIBLE);
                holder.cancelClick.setVisibility(View.INVISIBLE);

            }

            if (offerStatus==3){
                holder.offerStatus.setText("Cancelled");
                holder.offerStatus.setTextColor(mcontext.getResources().getColor(R.color.black));
                holder.resentButton.setVisibility(View.INVISIBLE);
                holder.cancelClick.setVisibility(View.INVISIBLE);

            }


            if (offerStatus==4){
                holder.offerStatus.setText("Expired");
                holder.offerStatus.setTextColor(mcontext.getResources().getColor(R.color.textColorSecondary));

            }
            // 绑定点击事件
            holder.resentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 1. resent click  0.reject click
                    mlistener.onClick(position, offerList.get(position).getId(), product.getId());
                }
            });

            holder.cancelClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 1. resent click
                    cancelClickListener.onClick(position, offerList.get(position).getId(), 1, product.getId());
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



        private MaterialButton resentButton;

        private TextView cancelClick;

        private TextView offerStatus;

        public LinearViewHolder(View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImage = itemView.findViewById(R.id.ItemImage);
            myOfferTime = itemView.findViewById(R.id.myOfferTime);

            resentButton = itemView.findViewById(R.id.resentButton);
            cancelClick = itemView.findViewById(R.id.cancelClick);

            offerStatus = itemView.findViewById(R.id.offerStatus);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos, Integer itemId, Integer productId);
    }

    public interface OnCancelClickListener{
        void onClick(int pos, Integer itemId, int i, Integer productId);
    }

    public List<Offer> getRecordList() {
        return offerList;
    }

    public void setRecordList(List<Offer> offerList) {
        this.offerList = offerList;
    }
}
