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
import comp5703.sydney.edu.au.learn.DTO.ProductOffer;
import comp5703.sydney.edu.au.learn.R;

public class OfferReceivedListAdapter extends RecyclerView.Adapter<OfferReceivedListAdapter.LinearViewHolder>{

    private Context mcontext;
    private OnItemClickListener mlistener;
    private List<ProductOffer> offerList;
    private OnCancelClickListener cancelClickListener;

    public OfferReceivedListAdapter(Context context, List<ProductOffer> offerList, OnItemClickListener listener, OnCancelClickListener cancelClickListener){
        this.mcontext = context;
        this.mlistener = listener;
        this.offerList = offerList;
        this.cancelClickListener = cancelClickListener;
    }
    @NonNull
    @Override
    public OfferReceivedListAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OfferReceivedListAdapter.LinearViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.layout_receivedoffer_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // 初始化请求数据还没回来的时候list是空的
        if (!offerList.isEmpty()){

            // 获取Product object
            ProductOffer productOffer = offerList.get(position);

            // 获取offer的提交时间
            Long timeStamp = productOffer.getTimestamp();  // 将时间戳转换为毫秒

            Integer offerStatus = productOffer.getOfferStatus();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 使用 SimpleDateFormat 对象将时间戳转换为字符串
            String formattedDate = sdf.format(new Date(timeStamp));

            holder.itemName.setText(productOffer.getProductName());
            holder.itemPrice.setText(Double.toString(productOffer.getProductPrice()));
            holder.myOfferTime.setText(formattedDate);
            Picasso.get()
                    .load(imageURL+productOffer.getProductImage()) // 网络图片的URL
                    .into(holder.itemImage);



            // 绑定点击事件
            holder.AcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 1. resent click  0.reject click
                    mlistener.onClick(position, productOffer.getOfferId(), productOffer.getProductPrice());
                }
            });

//            holder.rejectClick.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // 1. resent click  0.reject click
//                    cancelClickListener.onClick(position, productOffer.getOfferId(), 1);
//                }
//            });


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

        // 绑定状态按钮
        private MaterialButton AcceptButton;

        private TextView rejectClick;

        private TextView contactClick;



        @SuppressLint("CutPasteId")
        public LinearViewHolder(View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImage = itemView.findViewById(R.id.ItemImage);
            myOfferTime = itemView.findViewById(R.id.myOfferTime);

            AcceptButton = itemView.findViewById(R.id.AcceptButton);
            rejectClick = itemView.findViewById(R.id.AcceptButton);
            contactClick = itemView.findViewById(R.id.AcceptButton);


        }
    }

    public interface OnItemClickListener{
        void onClick(int pos, Integer itemId, double productPrice);
    }

    public interface OnCancelClickListener{
        void onClick(int pos, Integer itemId, int i);
    }

    public List<ProductOffer> getRecordList() {
        return offerList;
    }

    public void setRecordList(List<ProductOffer> offerList) {
        this.offerList = offerList;
    }
}
