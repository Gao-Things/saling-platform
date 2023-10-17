package comp5703.sydney.edu.au.learn.Home.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.DTO.UserMessage;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.util.TimeCalculateUtil;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.LinearViewHolder>{

    private Context mcontext;
    private OnItemClickListener mlistener;
    private List<UserMessage> userMessageList;
    private Integer userId;


    public MessageListAdapter(Context context, List<UserMessage> userMessageList,Integer userId, OnItemClickListener listener){
        this.mcontext = context;
        this.mlistener = listener;
        this.userMessageList = userMessageList;
        this.userId = userId;
    }
    @NonNull
    @Override
    public MessageListAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageListAdapter.LinearViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.layout_linear_messagelist, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // 初始化请求数据还没回来的时候list是空的
        if (!userMessageList.isEmpty()){
            UserMessage userMessage = userMessageList.get(position);
            holder.messageContent.setText(userMessage.getPostMessageContent());
            holder.messageTitle.setText(userMessage.getName());

            holder.messageSendTime.setText(TimeCalculateUtil.getTimeElapsed(userMessage.getPostTime()));


            // 绑定点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Integer remoteUserId;

                    if (userMessage.getFromUserId() == userId){
                        remoteUserId = userMessage.getToUserId();
                    }else {
                        remoteUserId = userMessage.getFromUserId();
                    }

                    mlistener.onClick(position, remoteUserId);
                }
            });
        }



    }



    @Override
    public int getItemCount() {
        return userMessageList.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{


        private TextView messageContent;

        private TextView messageTitle;

        private ImageView userAvatar;

        private TextView messageSendTime;

        public LinearViewHolder(View itemView){
            super(itemView);
            messageContent = itemView.findViewById(R.id.messageContent);
            messageTitle = itemView.findViewById(R.id.messageTitle);
            userAvatar = itemView.findViewById(R.id.userAvatar);
            messageSendTime = itemView.findViewById(R.id.messageDate);

        }
    }

    public interface OnItemClickListener{
        void onClick(int pos, Integer itemId);
    }

    public List<UserMessage> getRecordList() {
        return userMessageList;
    }

    public void setRecordList(List<UserMessage> userMessageList) {
        this.userMessageList = userMessageList;
    }
}
