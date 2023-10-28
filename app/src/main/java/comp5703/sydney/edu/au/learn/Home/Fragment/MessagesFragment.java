package comp5703.sydney.edu.au.learn.Home.Fragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import comp5703.sydney.edu.au.learn.DTO.UserMessage;
import comp5703.sydney.edu.au.learn.Home.Adapter.MessageListAdapter;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.userIdVO;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
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



        Toolbar toolbar = view.findViewById(R.id.simple_toolbar);

        TextView toolbar_title = view.findViewById(R.id.toolbar_title);

        // 确保 toolbar_title 不是 null
        if (toolbar_title != null) {
            toolbar_title.setText("Message List");
        } else {
            Log.e("MessagesFragment", "toolbar_title is null");
        }





        messageListRecyclerView = view.findViewById(R.id.messageListRecyclerView);

        // 创建并设置RecyclerView的LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        messageListRecyclerView.setLayoutManager(layoutManager);


        // 创建并设置RecyclerView的Adapter
        messageListAdapter = new MessageListAdapter(getContext(),new ArrayList<UserMessage>(),userId, clickListener);
        messageListRecyclerView.setAdapter(messageListAdapter);

        getMessageList();



        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to delete it ？")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 这里写删除数据的代码
                                messageListAdapter.deleteItem(viewHolder.getAdapterPosition(), token, userId);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 撤消滑动效果
                                messageListAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        })
                        .setCancelable(false);  // 这里设置对话框为不可取消;
                builder.create().show();
            }

            @Override
            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red))
                        .addActionIcon(R.drawable.ic_baseline_delete_24)
                        .addSwipeLeftLabel("Delete")
                        .setSwipeLeftLabelTextSize(1,17)
                        .setSwipeLeftLabelColor(ContextCompat.getColor(getActivity(), R.color.white)) //设置字体颜色
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(messageListRecyclerView);



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
            args.putString("token", token);
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
