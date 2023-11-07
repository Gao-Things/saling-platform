package comp5703.sydney.edu.au.learn.Home.Adapter;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
            holder.buyerUsername.setText(productOffer.getBuyerName());

            holder.itemPrice.setText("$" + productOffer.getOfferPrice());
            holder.myOfferTime.setText(formattedDate);

            // 把图片链接字符串转回list
            String[] items = productOffer.getProductImage().substring(1, productOffer.getProductImage().length() - 1).split(", ");

            List<String> imageUrlList = new ArrayList<>();
            for (String item : items) {
                imageUrlList.add(item);
            }

            Picasso.get()
                    .load(imageURL+imageUrlList.get(0)) // 网络图片的URL,加载第一张图片
                    .into(holder.itemImage);

            // 绑定点击事件
            holder.AcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mlistener.onClick(position, productOffer.getOfferId(), productOffer.getOfferPrice());
                }
            });
            // 绑定拒绝事件
            holder.rejectClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelClickListener.onClick(position, productOffer.getOfferId(), productOffer.getProductPrice());
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

        // 绑定状态按钮
        private MaterialButton AcceptButton;

        private MaterialButton rejectClick;

        private TextView contactClick;

        private TextView buyerUsername;



        @SuppressLint("CutPasteId")
        public LinearViewHolder(View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImage = itemView.findViewById(R.id.ItemImage);
            myOfferTime = itemView.findViewById(R.id.myOfferTime);

            AcceptButton = itemView.findViewById(R.id.AcceptButton);
            rejectClick = itemView.findViewById(R.id.rejectClick);

            buyerUsername = itemView.findViewById(R.id.buyerUsername);

        }
    }

    public interface OnItemClickListener{
        void onClick(int pos, Integer itemId, double productPrice);
    }

    public interface OnCancelClickListener{
        void onClick(int pos, Integer itemId, double productPrice);
    }

    public List<ProductOffer> getRecordList() {
        return offerList;
    }

    public void setRecordList(List<ProductOffer> offerList) {
        this.offerList = offerList;
    }
}
