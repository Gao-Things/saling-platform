package comp5703.sydney.edu.au.learn.gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import comp5703.sydney.edu.au.learn.R;

public class myGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public myGridViewAdapter(Context context){
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    static class ViewHolder{
        public ImageView imageView;
        public TextView textView;

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            view = mLayoutInflater.inflate(R.layout.layout_grid_iten, null);
            holder = new ViewHolder();
            holder.imageView = view.findViewById(R.id.iv_grid);
            holder.textView = view.findViewById(R.id.tv_title);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        // 赋值
        holder.textView.setText("梦梦");

        String imageUrl = "https://img.freepik.com/free-vector/isolated-rose-flower-line-art-with-leaf-clipart_41066-2958.jpg?size=626&ext=jpg"; // 替换为您想要加载的网络图片URL

        // 使用Picasso加载网络图片并设置到ImageView中
        Picasso.get().load(imageUrl).into(holder.imageView);

        return view;
    }
}
