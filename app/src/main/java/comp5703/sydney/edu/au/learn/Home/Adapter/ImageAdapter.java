package comp5703.sydney.edu.au.learn.Home.Adapter;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import comp5703.sydney.edu.au.learn.R;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_ADD = 1;
    private List<String> imagePaths;
    private OnAddImageClickListener listener;

    public ImageAdapter(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
    public void setOnAddImageClickListener(OnAddImageClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addImageUrl(String imageUrl) {
        imagePaths.add(imageUrl);
        notifyDataSetChanged(); // 通知数据已经改变，以便刷新RecyclerView
    }


    @Override
    public int getItemViewType(int position) {
        if (position == imagePaths.size()) {
            return TYPE_ADD;
        }
        return TYPE_IMAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ADD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_icon_layout, parent, false);
            return new AddViewHolder(view, listener);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
            return new ImageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageViewHolder) {
            // Load the image from the path
            // Here, I'm using Glide as an example
            Picasso.get()
                    .load(imagePaths.get(position)) // 网络图片的URL
                    .into(((ImageViewHolder) holder).imageView);

        }
    }

    @Override
    public int getItemCount() {
        return imagePaths.size() + 1;  // +1 for the add icon
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    static class AddViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        OnAddImageClickListener localListener;
        AddViewHolder(@NonNull View itemView, OnAddImageClickListener listener) {
            super(itemView);
            this.localListener = listener;
            imageView = itemView.findViewById(R.id.addImageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onAddImageClick();
                    }
                }
            });
        }
    }

    public interface OnAddImageClickListener {
        void onAddImageClick();
    }

}
