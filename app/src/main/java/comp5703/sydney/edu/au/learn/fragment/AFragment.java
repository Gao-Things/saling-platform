package comp5703.sydney.edu.au.learn.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import comp5703.sydney.edu.au.learn.R;

public class AFragment extends Fragment {

    private TextView mTvTitle;

    private Activity mActivity;

    private Button mMtnChange, mBtnReset, mBtnMessage;

    private BFragment bFragment;

    private IOMessageClick listener;

    // 调用这个方法相当于生成一个传参到fragment，
    public static AFragment newInstance(String title){
        AFragment fragment = new AFragment();

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        // fragment被重新构造也会被赋值
        fragment.setArguments(bundle);
        return fragment;

    }

    public interface IOMessageClick{
        void onClick(String text);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTvTitle = view.findViewById(R.id.tv_title);
        mMtnChange = view.findViewById(R.id.btn_change);
        mBtnReset = view.findViewById(R.id.btn_reset);
        mBtnMessage = view.findViewById(R.id.btn_message);


        // fragment之间进行切换
        mMtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (bFragment == null){
                   bFragment = new BFragment();
               }
              Fragment fragment = getFragmentManager().findFragmentByTag("a");
               if (fragment !=null){
                   // 直接使用replace会刷新fragment，但使用hide和add不会
                   getFragmentManager().beginTransaction().hide(fragment).add(R.id.fl_container, bFragment).addToBackStack(null).commitAllowingStateLoss();
               }else {
                   getFragmentManager().beginTransaction().replace(R.id.fl_container, bFragment).addToBackStack(null).commitAllowingStateLoss();
               }

            }
        });

        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvTitle.setText("new text");
            }
        });

        // 点击按钮给activity传递值
        mBtnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((ContainerActivity)getActivity()).setData("fragment往activity传值");

                listener.onClick("fragment往activity传值");

            }
        });

        if(getArguments()!=null){
            mTvTitle.setText(getArguments().getString("title"));
        }

        // 获取父activity
        if (getActivity() != null){

        }else {

        }

    }
     // 出现时候触发的事件
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (IOMessageClick) context;
        }catch (ClassCastException e){
            throw new ClassCastException("Activity必须实现 IOMessageClick接口");
        }

    }
//
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        // 取消异步
//    }
}
