package comp5703.sydney.edu.au.learn.Home.Adapter;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Message;
import comp5703.sydney.edu.au.learn.R;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SENT = 0;
    private static final int RECEIVED = 1;
    private static final int SENT_CARD = 2;
    private static final int RECEIVED_CARD = 3;
    private List<Message> messages;
    private OnItemClickListener listener;

    // Adapter的构造函数

    public ChatAdapter(Context context, List<Message> messages, OnItemClickListener listener) {
        this.listener = listener;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        switch (message.getType()) {
            case SENT:
                return SENT;
            case RECEIVED:
                return RECEIVED;
            case SENTCARD:
                return SENT_CARD;
            case RECEIVEDCARD:
                return RECEIVED_CARD;
            default:
                return -1; // For unknown cases
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case SENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sent_message, parent, false);
                return new SentViewHolder(view);
            case RECEIVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received_message, parent, false);
                return new ReceivedViewHolder(view);
            case SENT_CARD:
                // Replace with your card layout
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sent_card, parent, false);
                return new SentCardViewHolder(view);
            case RECEIVED_CARD:
                // Replace with your card layout
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received_card, parent, false);
                return new ReceivedCardViewHolder(view);
            default:
                throw new IllegalArgumentException("Unknown view type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        switch (holder.getItemViewType()) {
            case SENT:
                ((SentViewHolder) holder).bind(message);
                break;
            case RECEIVED:
                ((ReceivedViewHolder) holder).bind(message);
                break;
            case SENT_CARD:
                ((SentCardViewHolder) holder).bind(message);
                break;
            case RECEIVED_CARD:
                ((ReceivedCardViewHolder) holder).bind(message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        ImageView userAvatar;
        TextView tvTime;



        public SentViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            userAvatar = itemView.findViewById(R.id.userAvatar);
            tvTime = itemView.findViewById(R.id.tvTime);


        }

        public void bind(Message message) {
            tvMessage.setText(message.getContent());
            Log.d("avatar" ,imageURL + message.getAvatarUrl());
            Picasso.get()
                    .load(imageURL + message.getAvatarUrl())
                    .error(R.drawable.img_5)  // error_image为加载失败时显示的图片
                    .into(userAvatar);
            tvTime.setText(message.getPostTime());

        }
    }

    public class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        ImageView userAvatar;
        TextView tvTime;


        public ReceivedViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            userAvatar = itemView.findViewById(R.id.userAvatar);
            tvTime = itemView.findViewById(R.id.tvTime);
        }

        public void bind(Message message) {
            tvMessage.setText(message.getContent());
            Log.d("avatar" ,imageURL + message.getAvatarUrl());
            Picasso.get()
                    .load(imageURL + message.getAvatarUrl())
                    .error(R.drawable.img_5)  // error_image为加载失败时显示的图片
                    .into(userAvatar);

            tvTime.setText(message.getPostTime());
        }
    }



    public class SentCardViewHolder extends RecyclerView.ViewHolder {
        // Define your card view components here
        ImageView cardImage;
        TextView cardTitle;
        TextView cardDescription;
        ImageView userAvatar;
        TextView tvTime;
        // 添加成员变量来存储当前消息或其产品ID
        private Integer currentProductId;
        public SentCardViewHolder(View itemView) {
            super(itemView);
            // Initialize your card view components
            cardTitle = itemView.findViewById(R.id.tvCardTitle);
            cardDescription = itemView.findViewById(R.id.cardDescription);
            cardImage = itemView.findViewById(R.id.tvCardImage);
            userAvatar = itemView.findViewById(R.id.userAvatar);
            tvTime = itemView.findViewById(R.id.tvTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(currentProductId);
                    }
                }
            });
        }

        public void bind(Message message) {
            // Bind your card data
            // 在绑定时更新成员变量
            currentProductId = message.getProductId();

            cardTitle.setText(message.getCardTitle());
            cardDescription.setText(message.getCardDescription());


            Picasso.get()
                    .load(imageURL + message.getCardImageUrl())
                    .error(R.drawable.img_5)  // error_image为加载失败时显示的图片
                    .into(cardImage);
            // Bind other card components...

            Picasso.get()
                    .load(imageURL + message.getAvatarUrl())
                    .error(R.drawable.img_5)  // error_image为加载失败时显示的图片
                    .into(userAvatar);

            tvTime.setText(message.getPostTime());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(message.getProductId());
                    }
                }
            });


        }
    }

    public class ReceivedCardViewHolder extends RecyclerView.ViewHolder {
        // Define your card view components here
        ImageView cardImage;
        TextView cardTitle;
        TextView cardDescription;
        ImageView userAvatar;
        TextView tvTime;

        // 添加成员变量来存储当前消息或其产品ID
        private Integer currentProductId;

        public ReceivedCardViewHolder(View itemView) {
            super(itemView);
            // Initialize your card view components
            cardImage = itemView.findViewById(R.id.tvCardImage);
            cardTitle = itemView.findViewById(R.id.tvCardTitle);
            cardDescription = itemView.findViewById(R.id.cardDescription);
            userAvatar = itemView.findViewById(R.id.userAvatar);
            tvTime = itemView.findViewById(R.id.tvTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(currentProductId);
                    }
                }
            });

        }

        public void bind(Message message) {
            // Bind your card data
            // 在绑定时更新成员变量
            currentProductId = message.getProductId();

            cardTitle.setText(message.getCardTitle());
            cardDescription.setText(message.getCardDescription());


            Picasso.get()
                    .load(imageURL + message.getCardImageUrl())
                    .error(R.drawable.img_5)  // error_image为加载失败时显示的图片
                    .into(cardImage);
            // Bind other card components...

            Picasso.get()
                    .load(imageURL + message.getAvatarUrl())
                    .error(R.drawable.img_5)  // error_image为加载失败时显示的图片
                    .into(userAvatar);

            tvTime.setText(message.getPostTime());



        }
    }


    public interface OnItemClickListener {
        void onItemClick(Integer productId);
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
