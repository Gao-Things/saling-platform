package comp5703.sydney.edu.au.learn.Home.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.R;

public class SearchResultListAdapter extends RecyclerView.Adapter<SearchResultListAdapter.LinearViewHolder>{

    private Context mcontext;
    private OnItemClickListener mlistener;
    private List<Product> productList;
    private String searchQuery;

    public SearchResultListAdapter(Context context, List<Product> productList, OnItemClickListener listener){
        this.mcontext = context;
        this.mlistener = listener;
        this.productList = productList;
    }
    @NonNull
    @Override
    public SearchResultListAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchResultListAdapter.LinearViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.layout_searchresult_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // 初始化请求数据还没回来的时候list是空的
        if (!productList.isEmpty()){

            Product product = productList.get(position);
            String productName = product.getProductName();
            String productDetail = product.getProductDescription();

            if (searchQuery != null && !searchQuery.isEmpty()) {
                int startIndex;
                int endIndex;
                String targetText;
                if (productName.toLowerCase().contains(searchQuery.toLowerCase())) {
                    startIndex = productName.toLowerCase().indexOf(searchQuery.toLowerCase());
                    endIndex = startIndex + searchQuery.length();
                    targetText = productName;
                } else {
                    startIndex = productDetail.toLowerCase().indexOf(searchQuery.toLowerCase());
                    endIndex = startIndex + searchQuery.length();
                    targetText = productDetail;
                }

                if (startIndex != -1) { // Make sure the search query exists in the string
                    SpannableString spannableString = new SpannableString(targetText);
                    ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                    spannableString.setSpan(redSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.searchResult.setText(spannableString);
                } else {
                    holder.searchResult.setText(targetText); // Just set the text without highlighting if search query not found
                }
            } else {
                // Handle case where searchQuery is null or empty if needed
                holder.searchResult.setText(productDetail); // Default behavior
            }




            // 绑定点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mlistener.onClick(position, productList.get(position).getId());
                }
            });
        }



    }


    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView searchResult;

        public LinearViewHolder(View itemView){
            super(itemView);
            searchResult = itemView.findViewById(R.id.searchResult);

        }
    }

    public interface OnItemClickListener{
        void onClick(int pos, Integer productId);
    }

    public List<Product> getRecordList() {
        return productList;
    }

    public void setRecordList(List<Product> productList) {
        this.productList = productList;
    }
}
