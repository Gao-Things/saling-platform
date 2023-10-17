package comp5703.sydney.edu.au.learn.Home.Fragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.DTO.UserMessage;
import comp5703.sydney.edu.au.learn.Home.Adapter.MessageListAdapter;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.userIdVO;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MessagesFragment extends Fragment {

    private MessageListAdapter messageListAdapter;
    private RecyclerView messageListRecyclerView;

    private ChatFragment chatFragment;

    private Integer userId;
    private String token;

    private List<UserMessage> userMessages;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        // get SharedPreferences instance
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);

        // get global userID
        userId = sharedPreferences.getInt("userId", 9999);
        token = sharedPreferences.getString("token", "null");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageListRecyclerView = view.findViewById(R.id.messageListRecyclerView);

        // 创建并设置RecyclerView的LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        messageListRecyclerView.setLayoutManager(layoutManager);


        // 创建并设置RecyclerView的Adapter
        messageListAdapter = new MessageListAdapter(getContext(),new ArrayList<UserMessage>(),userId, clickListener);
        messageListRecyclerView.setAdapter(messageListAdapter);

        getMessageList();

    }

    // send request to get message list
    private void getMessageList() {
        userIdVO userIdVO = new userIdVO();
        userIdVO.setUserId(userId);
        NetworkUtils.getWithParamsRequest( userIdVO, "/normal/message/getMessageListByUserId",token, new Callback() {
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
    }

    private void handleResponse(Response response) throws IOException {
        String responseBody = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");

        // 提取 "records" 数组并转换为List
        JSONArray messageArray = jsonObject.getJSONArray("data");

        List<UserMessage> recordsListUse = messageArray.toJavaList(UserMessage.class);

        if (code == 200) {

            userMessages = recordsListUse;

            // 通知adapter数据更新
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 更新Adapter的数据
                    messageListAdapter.setRecordList(userMessages);
                    // 在UI线程上更新Adapter的数据
                    messageListAdapter.notifyDataSetChanged();
                }
            });


        } else {
            Log.d(TAG, "errowwwwwww");
        }
    }


    MessageListAdapter.OnItemClickListener clickListener = new MessageListAdapter.OnItemClickListener() {
        @Override
        public void onClick(int pos, Integer remoteUserId) {
            // jump to item detail
            if (chatFragment == null){
                chatFragment = new ChatFragment();
            }
            // 准备要传递的数据
            Bundle args = new Bundle();
            args.putInt("userId", userId);
            args.putInt("receiverId", remoteUserId); // 这里的 "key" 是传递数据的键名，"value" 是要传递的值
            chatFragment.setArguments(args);

            // 执行 Fragment 跳转
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_container, chatFragment); // R.id.fragment_container 是用于放置 Fragment 的容器
            transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
            transaction.commitAllowingStateLoss();

        }
    };

}
