package comp5703.sydney.edu.au.learn.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.R;

public class ItemEditAdapter extends RecyclerView.Adapter<ItemEditAdapter.LinearViewHolder>{

    private Context mcontext;
    private OnItemClickListener mlistener;
    private List<Record> recordList;


    public ItemEditAdapter(Context context, List<Record> recordList, OnItemClickListener listener){
        this.mcontext = context;
        this.mlistener = listener;
        this.recordList = recordList;
    }
    @NonNull
    @Override
    public ItemEditAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemEditAdapter.LinearViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.layout_linear_edit, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // 初始化请求数据还没回来的时候list是空的
        if (!recordList.isEmpty()){

            // 获取Product object
            Product product = recordList.get(position).getProduct();
            holder.itemName.setText(product.getProductName());
            holder.itemPrice.setText(Double.toString(product.getProductPrice()));

            System.out.println(product);

        }



        holder.itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mlistener.onClick(position);
            }
        });

    }



    @Override
    public int getItemCount() {
        return recordList.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView itemName;

        private TextView itemPrice;

        public LinearViewHolder(View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }
}
