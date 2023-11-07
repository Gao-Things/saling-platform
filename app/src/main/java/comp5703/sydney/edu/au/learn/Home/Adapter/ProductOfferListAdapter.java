package comp5703.sydney.edu.au.learn.Home.Adapter;

import static comp5703.sydney.edu.au.learn.util.TimeCalculateUtil.getTimeElapsed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Offer;
import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.R;

public class ProductOfferListAdapter extends RecyclerView.Adapter<ProductOfferListAdapter.LinearViewHolder>{

    private Context mcontext;
    private OnItemClickListener mlistener;
    private List<Offer> recordList;


    public ProductOfferListAdapter(Context context, List<Offer> recordList, OnItemClickListener listener){
        this.mcontext = context;
        this.mlistener = listener;
        this.recordList = recordList;
    }
    @NonNull
    @Override
    public ProductOfferListAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductOfferListAdapter.LinearViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.layout_productoffer_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // 初始化请求数据还没回来的时候list是空的
        if (!recordList.isEmpty()){

            // 获取Product object
            Product product = recordList.get(position).getProduct();

            // get offer status
            Integer offerStatus = recordList.get(position).getOfferStatus();

            // set offer price
            holder.offerPrice.setText(String.valueOf(recordList.get(position).getPrice()));

            Long offerSentTime = recordList.get(position).getTimestamp();
            Date date = new Date(offerSentTime);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            String formattedDate = sdf.format(date);

            holder.offerTime.setText(formattedDate);

            holder.buyerName.setText(recordList.get(position).getBuyer().getName());

            // 绑定点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mlistener.onClick(position, product.getId());
                }
            });



            if (offerStatus==0){
                holder.offerStatus.setText("Pending");
                holder.offerStatus.setTextColor(mcontext.getResources().getColor(R.color.yellow));
                holder.sellerOfferCard.setCardBackgroundColor(mcontext.getResources().getColor(R.color.yellow));

            }
            if (offerStatus == 1){
                holder.offerStatus.setText("Accepted");
                holder.offerStatus.setTextColor(mcontext.getResources().getColor(R.color.Green));
                holder.sellerOfferCard.setCardBackgroundColor(mcontext.getResources().getColor(R.color.Green));
            }

            if (offerStatus==2){
                holder.offerStatus.setText("Rejected");
                holder.offerStatus.setTextColor(mcontext.getResources().getColor(R.color.red));
                holder.sellerOfferCard.setCardBackgroundColor(mcontext.getResources().getColor(R.color.red));
            }

            if (offerStatus==3){
                holder.offerStatus.setText("Cancelled");
                holder.offerStatus.setTextColor(mcontext.getResources().getColor(R.color.black));
                holder.sellerOfferCard.setCardBackgroundColor(mcontext.getResources().getColor(R.color.black));
            }
        }


    }



    @Override
    public int getItemCount() {
        return recordList.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView offerPrice;

        private TextView offerStatus;

        private TextView offerTime;

        private MaterialCardView sellerOfferCard;

        private TextView buyerName;


        public LinearViewHolder(View itemView){
            super(itemView);
            offerPrice = itemView.findViewById(R.id.offerPrice);
            offerStatus = itemView.findViewById(R.id.offerStatus);
            offerTime = itemView.findViewById(R.id.offerTime);
            sellerOfferCard = itemView.findViewById(R.id.sellerOfferCard);
            buyerName = itemView.findViewById(R.id.buyerName);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos, Integer itemId);
    }

    public List<Offer> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Offer> recordList) {
        this.recordList = recordList;
    }
}
