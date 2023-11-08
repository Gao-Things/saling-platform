package comp5703.sydney.edu.au.learn.Home.Adapter;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import comp5703.sydney.edu.au.learn.Common.CircleTransform;
import comp5703.sydney.edu.au.learn.Common.RoundedCornersTransformation;
import comp5703.sydney.edu.au.learn.DTO.MessageFormat;
import comp5703.sydney.edu.au.learn.DTO.UserMessage;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.userAndRemoteUserIdVO;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import comp5703.sydney.edu.au.learn.util.TimeCalculateUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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

            /**
             * 对收到对消息内容做一个拆装，原本为JSON String
             */
            // 将JSON字符串转换回MessageFormat对象
            MessageFormat messageFormat = JSON.parseObject(userMessage.getPostMessageContent(), MessageFormat.class);

            if (messageFormat.getMessageType() == 1){
                holder.messageContent.setText(messageFormat.getMessageText());
            }else if (messageFormat.getMessageType() == 2){
                holder.messageContent.setText(messageFormat.getCardDescription());
            }




            holder.messageTitle.setText(userMessage.getName());

            holder.messageSendTime.setText(TimeCalculateUtil.getTimeElapsed(userMessage.getPostTime()));


            Picasso.get()
                    .load(imageURL + userMessage.getAvatarUrl())
                    .transform(new CircleTransform())
                    .error(R.drawable.img_5)  // error_image为加载失败时显示的图片
                    .into(holder.userAvatar);

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

    public void deleteItem(int position, String token, Integer userId) {
        // 删除数据
        userAndRemoteUserIdVO userAndRemoteUserIdVO = new userAndRemoteUserIdVO();
        userAndRemoteUserIdVO.setUserId(userId);

        if (userId != userMessageList.get(position).getFromUserId()){
            userAndRemoteUserIdVO.setRemoteUserId(userMessageList.get(position).getFromUserId());
        }else {
            userAndRemoteUserIdVO.setRemoteUserId(userMessageList.get(position).getToUserId());
        }

        NetworkUtils.getWithParamsRequest( userAndRemoteUserIdVO, "/normal/message/deleteMessageHistoryByUserIdAndRemoteUserId",token, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }

            @Override
            public void onFailure(Call call, IOException e) {
            }
        });

        userMessageList.remove(position);
        notifyItemRemoved(position);

    }




}
