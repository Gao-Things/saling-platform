package comp5703.sydney.edu.au.learn.Home.Fragment;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.websocketUrl;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Message;
import comp5703.sydney.edu.au.learn.Home.Adapter.ChatAdapter;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.ReceivedMessage;
import comp5703.sydney.edu.au.learn.VO.ResponseMessage;
import comp5703.sydney.edu.au.learn.VO.SendMessage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;

    private ImageButton sentBtn;
    private EditText sendContent;

    private final OkHttpClient client = new OkHttpClient();
    private WebSocket webSocket;

    private Integer userId;

    private Integer receiverId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 在 ItemDetailFragment 中获取传递的整数值
        Bundle args = getArguments();
        if (args != null) {
            userId = args.getInt("userId");
            receiverId = args.getInt("receiverId");
            initWebSocket(userId);
        }


        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        sentBtn = view.findViewById(R.id.sentBtn);
        sendContent = view.findViewById(R.id.myEditText);

        // 创建并设置RecyclerView的LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatRecyclerView.setLayoutManager(layoutManager);


        // 创建并设置RecyclerView的Adapter
        chatAdapter = new ChatAdapter(getContext(),new ArrayList<Message>());
        chatRecyclerView.setAdapter(chatAdapter);


        sentBtn.setOnClickListener(this::sendMessage);

        List<Message> chatData = new ArrayList<>();

//        chatData.add(new Message("你好！", Message.MessageType.SENT));
//        chatData.add(new Message("你好，有什么可以帮助你的吗？", Message.MessageType.RECEIVED));
//        chatData.add(new Message("我想知道这附近有什么好吃的餐厅。", Message.MessageType.SENT));
//        chatData.add(new Message("附近的 XYZ 餐厅非常不错，推荐尝试！", Message.MessageType.RECEIVED));
//        chatData.add(new Message("谢谢，我去试试看！", Message.MessageType.SENT));
//        chatData.add(new Message("不用谢，希望你吃得愉快！", Message.MessageType.RECEIVED));

        chatAdapter.setMessages(chatData);
        chatAdapter.notifyDataSetChanged();


    }

    private void sendMessage(View view) {


        String sentString = sendContent.getText().toString();
        SendMessage sendMessage = new SendMessage();

        // 设置发送目标的ID
        sendMessage.setTo(receiverId);
        sendMessage.setText(sentString);

        String jsonString = JSON.toJSONString(sendMessage);

        // 发送消息到服务器
        webSocket.send(jsonString);

        sendContent.setText("");
        // set send message
        Message newSentMessage = new Message(sentString,"avatar3.png" ,Message.MessageType.SENT);
        chatAdapter.addMessage(newSentMessage);
        chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1); // 滚动到最新的消息


    }


    private void initWebSocket(Integer userId) {
        Request request = new Request.Builder()
                .url(websocketUrl + "/imserver/" + userId)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                // WebSocket connection opened
                Log.d("Websocket message", "websocket连接已打开");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                // Message received from server
                Log.d("Websocket message", "服务器发送的消息：" + text);

                ReceivedMessage receivedMessage = JSON.parseObject(text, ReceivedMessage.class);
                // set send message
                Message newSentMessage = new Message(receivedMessage.getText(),"avatar2.png", Message.MessageType.RECEIVED);


                // 通知adapter数据更新
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 更新Adapter的数据
                        chatAdapter.addMessage(newSentMessage);
                        chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1); // 滚动到最新的消息
                    }
                });


            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                // Handle connection failures
                Log.e("Websocket onFailure", "Connection failed: " + t.getMessage());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                // The remote peer initiated a graceful shutdown
                Log.d("Websocket onClosing", "Remote peer initiated close with code: " + code);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                // WebSocket has been fully closed
                Log.d("Websocket onClosed", "WebSocket closed with code: " + code);
            }
        });
    }

}
