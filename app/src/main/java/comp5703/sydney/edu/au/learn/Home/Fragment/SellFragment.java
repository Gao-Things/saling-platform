package comp5703.sydney.edu.au.learn.Home.Fragment;

import static android.content.ContentValues.TAG;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.alibaba.fastjson.JSONObject;
import com.itheima.wheelpicker.WheelPicker;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import comp5703.sydney.edu.au.learn.Common.DialogFragment;
import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.MainActivity;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.ItemVO;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SellFragment extends Fragment {

    private FrameLayout openCamera;
    private EditText editTitle;
    private EditText editDescription;
    private EditText editWeight;
    private AppCompatButton btnSubmit;
    private String uploadImageUrl;
    private ImageView coverImage;
    private AutoCompleteTextView autoCompleteTextView;

    private Uri photoURI;
    private File photoFile;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    WheelPicker wheelPicker;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        openCamera = view.findViewById(R.id.openCamera);
        editTitle = view.findViewById(R.id.editTitle);
        editDescription = view.findViewById(R.id.editDescription);
        editWeight = view.findViewById(R.id.editWeight);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        coverImage = view.findViewById(R.id.coverImage);
        wheelPicker =  view.findViewById(R.id.wheel1);

        String[] items = new String[] {"24K", "21K", "18K"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.my_dropdown_item,  // 使用自定义布局
                items
        );

        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(adapter);
        openCamera.setOnClickListener(this::openCameraClick);


        // 设置数据
        List<String> data = Arrays.asList("g", "kg", "oz");
        wheelPicker.setData(data);
        wheelPicker.setItemTextSize(40);
        wheelPicker.setVisibleItemCount(3);  // 假设5是一个合适的可见项数

        wheelPicker.setMaximumWidthTextPosition(2);
        wheelPicker.setItemSpace(20);  // 假设5是一个合适的可见项数
        // 设置选定项目的监听器
        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                // 处理选定项目的事件
                System.out.println(position +":::"+data);
            }
        });

        // submit the form
        btnSubmit.setOnClickListener(this::submitClick);


    }

    private void openCameraClick(View view){
        requestCameraPermission();
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{ Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            launchCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            } else {
                // 权限被拒绝，添加额外的处理逻辑。
            }
        }
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // 错误处理代码
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getActivity(), "comp5703.sydney.edu.au.learn.fileProvider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            NetworkUtils.postFormDataRequest(photoFile, "xxx", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = JSONObject.parseObject(responseBody);
                    int code = jsonObject.getIntValue("code");
                    if (code == 200){
                        String imageUrl = jsonObject.getString("data");
                        uploadImageUrl = imageUrl;

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                openCamera.setVisibility(View.GONE);
                                coverImage.setVisibility(View.VISIBLE);

                                Picasso.get()
                                        .load(imageURL +uploadImageUrl) // 网络图片的URL
                                        .into(coverImage);
                            }
                        });

                    }
                }
            });


        }
    }



    private File createImageFile() throws IOException {
        // 创建一个唯一的文件名
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }


    private void submitClick(View view){
        // title value
        String itemTitle = editTitle.getText().toString();

        // description
        String itemDescription = editDescription.getText().toString();

        // weight
        String itemWeight = editWeight.getText().toString();

        // drop box value
        String selectedValue = autoCompleteTextView.getText().toString();

        // get SharedPreferences instance
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);

        // get global userID
        Integer userId = sharedPreferences.getInt("userId", 9999);

        ItemVO itemVO = new ItemVO();
        itemVO.setItemTitle(itemTitle);
        itemVO.setItemDescription(itemDescription);
        itemVO.setItemWeight(Double.parseDouble(itemWeight));
        itemVO.setSelectedValue(selectedValue);
        itemVO.setImageUrl(uploadImageUrl);
        itemVO.setUserId(userId);

        NetworkUtils.postJsonRequest(itemVO, "/public/product/uploadProduct", new Callback() {
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

  
      private void handleResponse(Response response) throws IOException {
        String responseBody = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");

        if (code == 200) {
            DialogFragment dialogFragment = new DialogFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            dialogFragment.show(transaction, "dialog_fragment_tag");
        } else {

        }
    }

    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }



}
