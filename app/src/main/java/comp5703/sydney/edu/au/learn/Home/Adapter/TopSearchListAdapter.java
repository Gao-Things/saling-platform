package comp5703.sydney.edu.au.learn.Home.Adapter;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.DTO.TopSearch;
import comp5703.sydney.edu.au.learn.R;

public class TopSearchListAdapter extends RecyclerView.Adapter<TopSearchListAdapter.LinearViewHolder>{

    private Context mcontext;
    private OnItemClickListener mlistener;
    private List<TopSearch> recordList;


    public TopSearchListAdapter(Context context, List<TopSearch> recordList, OnItemClickListener listener){
        this.mcontext = context;
        this.mlistener = listener;
        this.recordList = recordList;
    }
    @NonNull
    @Override
    public TopSearchListAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopSearchListAdapter.LinearViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.layout_topsearch_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // 初始化请求数据还没回来的时候list是空的
        if (!recordList.isEmpty()){

            // 排序，由小到大
            recordList.sort((o1, o2) -> o2.getSearchCount().compareTo(o1.getSearchCount()));


            TopSearch topSearch = recordList.get(position);
            holder.topContent.setText(topSearch.getContent());
            holder.hotNumber.setText(String.valueOf(topSearch.getSearchCount()));

            Drawable[] drawables = holder.topContent.getCompoundDrawables();
            Drawable leftDrawable = drawables[0];  // 0代表左侧的drawable


            if (leftDrawable != null) {
                int color;

                // 根据position选择颜色
                switch (position) {
                    case 0:
                        color = Color.RED;
                        holder.hotImage.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        color = Color.YELLOW;
                        holder.hotImage.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        color = Color.BLUE;
                        holder.hotImage.setVisibility(View.VISIBLE);
                        break;
                    default:
                        color = Color.GRAY;
                        break;
                }

                leftDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
                holder.topContent.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
            }


            // 绑定点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mlistener.onClick(position, recordList.get(position).getProductId(), recordList.get(position).getContent());
                }
            });
        }



    }



    @Override
    public int getItemCount() {
        return recordList.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView topContent;

        private TextView hotNumber;

        private ImageView hotImage;


        public LinearViewHolder(View itemView){
            super(itemView);
            topContent = itemView.findViewById(R.id.topContent);
            hotNumber = itemView.findViewById(R.id.hotNumber);
            hotImage = itemView.findViewById(R.id.hotImage);

        }
    }

    public interface OnItemClickListener{
        void onClick(int pos, Integer topProductId, String productTitle);
    }

    public List<TopSearch> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<TopSearch> recordList) {
        this.recordList = recordList;
    }
}
