package comp5703.sydney.edu.au.learn.Home.Fragment;

import static android.content.ContentValues.TAG;
import static comp5703.sydney.edu.au.learn.DTO.Message.MessageType.RECEIVED;
import static comp5703.sydney.edu.au.learn.DTO.Message.MessageType.RECEIVEDCARD;
import static comp5703.sydney.edu.au.learn.DTO.Message.MessageType.SENT;
import static comp5703.sydney.edu.au.learn.DTO.Message.MessageType.SENTCARD;
import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;
import static comp5703.sydney.edu.au.learn.util.NetworkUtils.websocketUrl;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import comp5703.sydney.edu.au.learn.DTO.Message;
import comp5703.sydney.edu.au.learn.DTO.MessageFormat;
import comp5703.sydney.edu.au.learn.DTO.MessageHistory;
import comp5703.sydney.edu.au.learn.DTO.User;
import comp5703.sydney.edu.au.learn.Home.Adapter.ChatAdapter;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.ReceivedMessage;
import comp5703.sydney.edu.au.learn.VO.SendMessage;
import comp5703.sydney.edu.au.learn.VO.userAndRemoteUserIdVO;
import comp5703.sydney.edu.au.learn.VO.userIdVO;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
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

    private String token;

    private ImageView remoteUserAvatar;
    private TextView remoteUserName;

    private String userAvatarUrl;
    private String remoteUserAvatarUrl;

    SharedPreferences sharedPreferences;

    private ImageView backClick;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_view, container, false);
        // get SharedPreferences instance
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 隐藏header
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();


        // 获取底部导航栏的引用
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
        // 隐藏底部导航栏
        bottomNavigationView.setVisibility(View.GONE);

        // 在 ItemDetailFragment 中获取传递的整数值
        Bundle args = getArguments();
        if (args != null) {
            userId = args.getInt("userId");
            receiverId = args.getInt("receiverId");
            token = args.getString("token");
            initWebSocket(userId);
        }

        /**
         *
         * 进入这个页面的时候，禁止消息弹窗推送,设置全局变量通知service
         */
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("userIsInCheating", true);
        editor.apply();



        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        sentBtn = view.findViewById(R.id.sentBtn);
        sendContent = view.findViewById(R.id.myEditText);

        sendContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    sendContent.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (chatAdapter.getItemCount() >1){
                                chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                            }
                        }
                    }, 100);
                }
            }
        });



        backClick = view.findViewById(R.id.backClick);

        backClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(); // 返回上一级
            }
        });


        remoteUserAvatar = view.findViewById(R.id.userAvatar);
        remoteUserName = view.findViewById(R.id.userName);

        // 创建并设置RecyclerView的LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatRecyclerView.setLayoutManager(layoutManager);


        // 创建并设置RecyclerView的Adapter
        chatAdapter = new ChatAdapter(getContext(),new ArrayList<Message>());
        chatRecyclerView.setAdapter(chatAdapter);


        sentBtn.setOnClickListener(this::sendMessage);

        List<Message> chatData = new ArrayList<>();

        chatAdapter.setMessages(chatData);

        getChatHistoryData();

        getUserInfoById(userId, true);
        getUserInfoById(receiverId, false);

        chatAdapter.notifyDataSetChanged();


    }

    // 发送请求获取用户头像和姓名
    private void getUserInfoById(Integer userId, boolean isLocalUser){
        userIdVO userIdVO = new userIdVO();
        userIdVO.setUserId(userId);
        NetworkUtils.getWithParamsRequest( userIdVO, "/normal/message/getUserInfoById",token, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleResponse2(response, isLocalUser);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                handleFailure(e);
            }
        });
    }

    private void handleResponse2(Response response, boolean isLocalUser) throws IOException {

        String responseBody = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");

        // 提取 "records" 数组并转换为List
        JSONObject userJson = jsonObject.getJSONObject("data");

        User user = userJson.toJavaObject(User.class);


        if (code == 200) {

            // 通知adapter数据更新
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (isLocalUser){
                        // 更新本地user的头像和姓名
                        userAvatarUrl = user.getAvatarUrl();
                    }else {
                        // 设置远程用户的头像,姓名
                        remoteUserAvatarUrl = user.getAvatarUrl();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                remoteUserName.setText(user.getName());
                                Picasso.get()
                                        .load(imageURL + remoteUserAvatarUrl)
                                        .error(R.drawable.img_5)  // error_image为加载失败时显示的图片
                                        .into(remoteUserAvatar);
                            }
                        });
                    }



                }
            });


        } else {
            Log.d(TAG, "errowwwwwww");
        }
    }


    // 发送请求获取聊天记录
    private void getChatHistoryData() {
        userAndRemoteUserIdVO userAndRemoteUserIdVO = new userAndRemoteUserIdVO();
        userAndRemoteUserIdVO.setUserId(userId);
        userAndRemoteUserIdVO.setRemoteUserId(receiverId);
        NetworkUtils.getWithParamsRequest( userAndRemoteUserIdVO, "/normal/message/getMessageListByUserIdAndRemoteUserId",token, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleResponse(response);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                handleFailure(e);
            }
        });
    }

    private void handleFailure(IOException e) {
        System.out.println(e);
    }


    private void handleResponse(Response response) throws IOException {

        String responseBody = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");

        if (code == 200) {
            // 提取 "records" 数组并转换为List
            JSONArray messageHistoryList = jsonObject.getJSONArray("data");

            List<MessageHistory> recordsListUse = messageHistoryList.toJavaList(MessageHistory.class);

            List<Message> messageList = new ArrayList<>();


            for (MessageHistory messageHistory : recordsListUse){
                // 如果当前数据本地用户是发信人的话，这条消息就渲染为sent,否则就渲染为RECEIVED
                Message message;

                /**
                 * 对收到对消息内容做一个拆装，原本为JSON String
                 */
                // 将JSON字符串转换回MessageFormat对象
                MessageFormat messageFormat = JSON.parseObject(messageHistory.getPostMessageContent(), MessageFormat.class);

                if (messageHistory.getFromUserId() == userId){

                    if (messageFormat.getMessageType() == 1){
                        message = new Message(messageFormat.getMessageText(), messageHistory.getFromUserAvatar(), SENT, formatTimeStamp(messageHistory.getPostTime()));
                        messageList.add(message);
                    } else if (messageFormat.getMessageType() == 2){
                        // TODO 卡片消息的处理
                        if (messageFormat.getCardDescription() ==null){
                            messageFormat.setCardDescription("No Description by seller");
                        }
                        message = new Message(messageFormat.getCardTitle(),messageFormat.getCardDescription(), messageHistory.getFromUserAvatar(), messageFormat.getCardImageUrl(),  SENTCARD, formatTimeStamp(messageHistory.getPostTime()));
                        messageList.add(message);
                    }


                }else {
                    if (messageFormat.getMessageType() == 1){
                        message = new Message(messageFormat.getMessageText(), messageHistory.getFromUserAvatar(), RECEIVED, formatTimeStamp(messageHistory.getPostTime()));
                        messageList.add(message);
                    } else if (messageFormat.getMessageType() == 2){
                        // TODO 卡片消息的处理

                        if (messageFormat.getCardDescription() ==null){
                            messageFormat.setCardDescription("No Description by seller");
                        }
                        message = new Message(messageFormat.getCardTitle(),messageFormat.getCardDescription(), messageHistory.getFromUserAvatar(), messageFormat.getCardImageUrl(),  RECEIVEDCARD, formatTimeStamp(messageHistory.getPostTime()));
                        messageList.add(message);
                    }

                }


            }

            // 通知adapter数据更新
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 更新Adapter的数据
                    chatAdapter.setMessages(messageList);
                    // 在UI线程上更新Adapter的数据
                    chatAdapter.notifyDataSetChanged();

                    if (messageList.size() >= 5){
                        chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                    }

                }
            });


        } else {
            Log.d(TAG, "errowwwwwww");
        }
    }

    private  void sendMessage(View view) {


        String sentString = sendContent.getText().toString();
        SendMessage sendMessage = new SendMessage();

        // 设置发送目标的ID
        sendMessage.setTo(receiverId);

        // 对消息内容本身进行一个包装
        // 此条消息为一条普通消息
        MessageFormat messageFormat = new MessageFormat(1, sentString);
        String formatString =  JSON.toJSONString(messageFormat);

        sendMessage.setText(formatString);

        String jsonString = JSON.toJSONString(sendMessage);

        // 发送消息到服务器
        webSocket.send(jsonString);

        sendContent.setText("");

        // set send message

        Message newSentMessage = new Message(sentString,userAvatarUrl , SENT, formatTimeStamp(System.currentTimeMillis()));
        chatAdapter.addMessage(newSentMessage);
        chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1); // 滚动到最新的消息


    }

    private String formatTimeStamp(Long currentTimeMillis){

        // 创建一个 Date 对象
        Date date = new Date(currentTimeMillis);

        // 使用 SimpleDateFormat 来格式化日期为12小时制
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
        String formattedTime = sdf.format(date);

        return formattedTime;

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
                startHeartbeat();
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                // Message received from server
                Log.d("Websocket message", "服务器发送的消息：" + text);

                ReceivedMessage receivedMessage = JSON.parseObject(text, ReceivedMessage.class);

                /**
                 * ID = 自己的ID 为心跳回复
                 */
                if(receivedMessage.getFrom() == userId){
                    Log.d("Websocket chat心跳", receivedMessage.getText());

                }else {

                    // set received message

                    /**
                     * 对收到对消息内容做一个拆装，原本为JSON String
                     */
                    // 将JSON字符串转换回MessageFormat对象
                    MessageFormat messageFormat = JSON.parseObject(receivedMessage.getText(), MessageFormat.class);


                    // 通知adapter数据更新
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Message newSentMessage = null;
                            // 如果type为1，则代表为一条普通消息
                            if (messageFormat.getMessageType() == 1){

                                newSentMessage = new Message(messageFormat.getMessageText(),remoteUserAvatarUrl, Message.MessageType.RECEIVED, formatTimeStamp(System.currentTimeMillis()));
                            }else if (messageFormat.getMessageType() == 2){
                                /**
                                 * 收到的消息类型为2，则代表为一条卡片消息
                                 *
                                 */
                                if (messageFormat.getCardDescription() == null){
                                    messageFormat.setCardDescription("The seller has no description");
                                }
                                newSentMessage = new Message(messageFormat.getCardTitle(), messageFormat.getCardDescription(),remoteUserAvatarUrl,messageFormat.getCardImageUrl() , RECEIVEDCARD, formatTimeStamp(System.currentTimeMillis()));
                            }

                            // 更新Adapter的数据
                            chatAdapter.addMessage(newSentMessage);
                            chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1); // 滚动到最新的消息

                            try {

                                if (sharedPreferences.getBoolean("messageToneOpen", false)){
                                    // 获取设置的音频
                                    Ringtone ringtone = getUserSettingTone();

                                    assert ringtone != null;
                                    ringtone.play();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    });
                }



            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                // Handle connection failures
                stopHeartbeat(); // WebSocket 关闭时停止发送心跳包
                Log.e("Websocket onFailure", "Connection failed: " + t.getMessage());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                // The remote peer initiated a graceful shutdown
                stopHeartbeat(); // WebSocket 关闭时停止发送心跳包
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

    private Ringtone getUserSettingTone() {
        Ringtone ringtone;
        String toneChoice = sharedPreferences.getString("chooseTone", null);

        switch (toneChoice) {
            // 获取系统默认的通知音频的 URI
            case "playful":
                Uri customSound2 = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.meng);
                ringtone = RingtoneManager.getRingtone(getActivity(), customSound2);
                break;

            case "crisp":
                Uri customSound = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.y831);
                ringtone = RingtoneManager.getRingtone(getActivity(), customSound);
                break;

            case "electronic":
                Uri customSound3 = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.y831);
                ringtone = RingtoneManager.getRingtone(getActivity(), customSound3);
                break;

            default:
                Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                ringtone = RingtoneManager.getRingtone(getActivity(), defaultSound);
                break;
        }


        return ringtone;
    }


    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private void startHeartbeat() {
        final Runnable heartbeatSender = new Runnable() {
            public void run() { sendHeartbeat(); }
        };

        // 定时任务，每30秒发送一次心跳
        scheduler.scheduleAtFixedRate(heartbeatSender, 30, 30, TimeUnit.SECONDS);
    }

    private void stopHeartbeat() {
        scheduler.shutdown();
    }

    private void sendHeartbeat() {
        if (webSocket != null) {
            // 发送心跳包 "ping"

            SendMessage sendMessage = new SendMessage();

            // 设置id为当前ID
            sendMessage.setTo(userId);
            sendMessage.setText("ping");

            String jsonString = JSON.toJSONString(sendMessage);

            // 发送消息到服务器
            webSocket.send(jsonString);
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        // 用户即将离开Fragment，可以在这里保存数据或者释放资源

        /**
         *
         * 离开这个页面的时候，开启消息弹窗推送,设置全局变量通知service
         */
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("userIsInCheating", false);
        editor.apply();

        // 获取底部导航栏的引用
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);

        // 重新显示底部导航栏
        bottomNavigationView.setVisibility(View.VISIBLE);

    }


}
