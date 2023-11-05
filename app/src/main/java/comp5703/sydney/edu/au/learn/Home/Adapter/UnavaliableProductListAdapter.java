package comp5703.sydney.edu.au.learn.Home.Adapter;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Offer;
import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.ItemVO;
import comp5703.sydney.edu.au.learn.VO.ProductIDVO;
import comp5703.sydney.edu.au.learn.VO.userAndRemoteUserIdVO;
import comp5703.sydney.edu.au.learn.forgotPassword;
import comp5703.sydney.edu.au.learn.setNewPassword;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UnavaliableProductListAdapter extends RecyclerView.Adapter<UnavaliableProductListAdapter.LinearViewHolder>{

    private Context mcontext;
    private OnItemClickListener mlistener;
    private List<Product> productList;


    public UnavaliableProductListAdapter(Context context, List<Product> productList, OnItemClickListener listener){
        this.mcontext = context;
        this.mlistener = listener;
        this.productList = productList;
    }
    @NonNull
    @Override
    public UnavaliableProductListAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UnavaliableProductListAdapter.LinearViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.layout_unavaliable_product_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // 初始化请求数据还没回来的时候list是空的
        if (!productList.isEmpty()){

            // 获取Product object
            Product product = productList.get(position);

            List<Offer> offerList = product.getOffers();

            if (product.getProductStatus() == 1){
                holder.productUnavailableReason.setText("Product Is Closed");
                holder.productUnavailableReason.setTextColor(Color.GRAY);
            }

            if (product.getProductStatus() == 2){
                holder.productUnavailableReason.setText("Product Is Sold");
                holder.productUnavailableReason.setTextColor(mcontext.getResources().getColor(R.color.Green));
            }

            // 获取offer的提交时间
            Long timeStamp = product.getProductUpdateTime();  // 将时间戳转换为毫秒

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 使用 SimpleDateFormat 对象将时间戳转换为字符串
            String formattedDate = sdf.format(new Date(timeStamp));
            holder.productUnpadteTime.setText(formattedDate);
            // 把图片链接字符串转回list
            String[] items = product.getProductImage().substring(1, product.getProductImage().length() - 1).split(", ");

            List<String> imageUrlList = new ArrayList<>();
            for (String item : items) {
                imageUrlList.add(item);
            }

            Picasso.get()
                    .load(imageURL+imageUrlList.get(0)) // 网络图片的URL,加载第一张图片
                    .into(holder.itemImage);


            holder.itemName.setText(product.getProductName());

            holder.productViews.setText("\uD83D\uDD25 " + product.getSearchCount());


            holder.productOfferNumber.setText("Received Offer :" + offerList.size());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.onClick(position,product.getId());
                }
            });

        }


    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{


        private TextView productUnavailableReason;

        private ImageView itemImage;

        private TextView itemName;

        private TextView productViews;

        private TextView productOfferNumber;

        private TextView productUnpadteTime;



        @SuppressLint("CutPasteId")
        public LinearViewHolder(View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemImage = itemView.findViewById(R.id.ItemImage);
            productViews = itemView.findViewById(R.id.productViews);
            productOfferNumber = itemView.findViewById(R.id.productOfferNumber);
            productUnavailableReason = itemView.findViewById(R.id.productUnavailableReason);
            productUnpadteTime = itemView.findViewById(R.id.productUnpadteTime);
        }
    }

    public void deleteItem(int position, String token, Integer userId) {
        // 删除数据
        ProductIDVO itemVO = new ProductIDVO();
        itemVO.setProductID(productList.get(position).getId());

        NetworkUtils.getWithParamsRequest( itemVO, "/normal/deleteProduct",token, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(responseBody);
                int code = jsonObject.getIntValue("code");

                if (code == 200) {

                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
            }
        });

        productList.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickListener{
        void onClick(int pos, Integer itemId);
    }


    public List<Product> getRecordList() {
        return productList;
    }

    public void setRecordList(List<Product> productList) {
        this.productList = productList;
    }
}
