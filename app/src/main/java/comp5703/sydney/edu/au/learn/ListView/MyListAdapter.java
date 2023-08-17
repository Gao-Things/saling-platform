package comp5703.sydney.edu.au.learn.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import comp5703.sydney.edu.au.learn.R;

public class MyListAdapter extends BaseAdapter {
    private Context mcontext;
    private LayoutInflater mlayoutInflater;
    public MyListAdapter(Context context){
        this.mcontext = context;
        mlayoutInflater = LayoutInflater.from(context);
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

    static class viewHolder{
        public ImageView imageView;
        public TextView tvTitle, tvTime, tvContent ;

    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        viewHolder holder = null;
        if(convertView == null){
            convertView = mlayoutInflater.inflate(R.layout.layout_list_item,viewGroup, false);
            holder = new viewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.ivUse);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvTime = convertView.findViewById(R.id.tv_time);
            holder.tvContent = convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);

        }else {
            holder = (viewHolder) convertView.getTag();
        }

        //给控件赋值
        holder.tvTitle.setText("这是标题");
        holder.tvTime.setText("新的时间");
        holder.tvContent.setText("新的内容");
        String imageUrl = "https://img.freepik.com/free-vector/isolated-rose-flower-line-art-with-leaf-clipart_41066-2958.jpg?size=626&ext=jpg"; // 替换为您想要加载的网络图片URL

        // 使用Picasso加载网络图片并设置到ImageView中
        Picasso.get().load(imageUrl).into(holder.imageView);

        return convertView;
    }
}
