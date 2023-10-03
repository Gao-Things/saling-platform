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

    @SuppressLint("SetTextI18n")
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
            holder.itemPrice.setText(Double.toString(product.getProductPrice()));
            holder.myOfferTime.setText(formattedDate);
            Picasso.get()
                    .load(imageURL+product.getProductImage()) // 网络图片的URL
                    .into(holder.itemImage);

            if (offerStatus == 1){
                holder.offerAccept.setVisibility(View.VISIBLE);

                holder.offerIsProcessing.setVisibility(View.GONE);
                holder.offerReject.setVisibility(View.GONE);
            }
            if (offerStatus==0){
                holder.offerIsProcessing.setVisibility(View.VISIBLE);

                holder.offerAccept.setVisibility(View.GONE);
                holder.offerReject.setVisibility(View.GONE);
            }

            if (offerStatus==2){
                holder.offerReject.setVisibility(View.VISIBLE );

                holder.offerAccept.setVisibility(View.GONE);
                holder.offerIsProcessing.setVisibility(View.GONE);
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

        private ImageView offerAccept;

        private LinearLayout offerIsProcessing;

        private ImageView offerReject;

        private MaterialButton resentButton;

        private TextView cancelClick;

        public LinearViewHolder(View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImage = itemView.findViewById(R.id.ItemImage);
            myOfferTime = itemView.findViewById(R.id.myOfferTime);
            offerAccept = itemView.findViewById(R.id.offerAccept);
            offerIsProcessing = itemView.findViewById(R.id.offerIsProcessing);
            offerReject = itemView.findViewById(R.id.offerReject);

            resentButton = itemView.findViewById(R.id.resentButton);
            cancelClick = itemView.findViewById(R.id.cancelClick);
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
