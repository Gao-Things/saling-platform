package comp5703.sydney.edu.au.learn.fragment;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import comp5703.sydney.edu.au.learn.R;

public class ContainerActivity extends AppCompatActivity implements AFragment.IOMessageClick {
    private AFragment aFragment;

    private BFragment bFragment;

    private Button btnChange;

    private TextView mTvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        btnChange = findViewById(R.id.btn_change);

        mTvTitle = findViewById(R.id.tv_title);
        // 初始化fragmentA
//        aFragment = new AFragment();
        //  传参方法构建fragment，从activity传值到fragment
        aFragment = AFragment.newInstance("参数方法");

        // fragment添加到Activity中
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, aFragment, "a").commitAllowingStateLoss();

    }


    public void setData(String text){
        mTvTitle.setText(text);
    }

    @Override
    public void onClick(String text) {
        mTvTitle.setText(text);
    }
}