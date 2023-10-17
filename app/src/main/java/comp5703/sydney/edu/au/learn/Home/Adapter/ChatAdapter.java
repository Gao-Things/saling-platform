package comp5703.sydney.edu.au.learn.Home.Adapter;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Message;
import comp5703.sydney.edu.au.learn.R;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SENT = 0;
    private static final int RECEIVED = 1;
    private List<Message> messages;

    public ChatAdapter(Context context, List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType() == Message.MessageType.SENT ? SENT : RECEIVED;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sent_message, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received_message, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder.getItemViewType() == SENT) {
            ((SentViewHolder) holder).bind(message);
        } else {
            ((ReceivedViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        ImageView userAvatar;
        public SentViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            userAvatar = itemView.findViewById(R.id.userAvatar);

        }

        public void bind(Message message) {
            tvMessage.setText(message.getContent());
            Log.d("avatar" ,imageURL + message.getAvatarUrl());
            Picasso.get()
                    .load(imageURL + message.getAvatarUrl())
                    .error(R.drawable.img_5)  // error_image为加载失败时显示的图片
                    .into(userAvatar);

        }
    }

    public class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        ImageView userAvatar;

        public ReceivedViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            userAvatar = itemView.findViewById(R.id.userAvatar);
        }

        public void bind(Message message) {
            tvMessage.setText(message.getContent());
            Log.d("avatar" ,imageURL + message.getAvatarUrl());
            Picasso.get()
                    .load(imageURL + message.getAvatarUrl())
                    .error(R.drawable.img_5)  // error_image为加载失败时显示的图片
                    .into(userAvatar);
        }
    }

    public void setMessages(List<Message> messageList) {
        this.messages = messageList;
    }

    // 当发送，接收到新消息时
    public void addMessage(Message message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

}
