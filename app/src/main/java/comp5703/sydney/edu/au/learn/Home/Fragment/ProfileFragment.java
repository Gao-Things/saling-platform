package comp5703.sydney.edu.au.learn.Home.Fragment;

import static android.content.ContentValues.TAG;
import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import comp5703.sydney.edu.au.learn.DTO.User;
import comp5703.sydney.edu.au.learn.Home.ContainerFragment.ProductContainerFragment;
import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.MainActivity;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.userIdVO;
import comp5703.sydney.edu.au.learn.forgotPassword;
import comp5703.sydney.edu.au.learn.service.MyService;
import comp5703.sydney.edu.au.learn.util.DialogUtil;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import comp5703.sydney.edu.au.learn.util.toastUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProfileFragment extends Fragment {
    private View rootView;

    private Integer userId;

    private String token;

    private CardView mySellingCardView;

    private CardView myOfferCardView;
    
    private CardView myMessageCardView;

    private CardView settingCardView;

    private CardView logout;

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;


    private static final int REQUEST_GALLERY_PERMISSION = 3;
    private static final int REQUEST_GALLERY_IMAGE = 4;

    private Uri photoURI;
    private File photoFile;

    private ShapeableImageView shapeableImageView;

    private TextView userName, userEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        // get SharedPreferences instance
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);

        // get global userID
        userId = sharedPreferences.getInt("userId", 9999);
        token = sharedPreferences.getString("token", "null");

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myOfferCardView = view.findViewById(R.id.myOfferCardView);
        myMessageCardView = view.findViewById(R.id.myMessageCardView);
        settingCardView = view.findViewById(R.id.settingCardView);
        mySellingCardView = view.findViewById(R.id.mySellingCardView);

        logout = view.findViewById(R.id.logout);
        shapeableImageView = view.findViewById(R.id.userAvatar);

        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);


        myOfferCardView.setOnClickListener(this::dumpToOfferContainer);
        myMessageCardView.setOnClickListener(this::dumpToMyMessage);
        settingCardView.setOnClickListener(this::dumpToSetting);
        logout.setOnClickListener(this::logout);
        shapeableImageView.setOnClickListener(this::openCameraClick);
        mySellingCardView.setOnClickListener(this::dumpToProductContainer);

        getUserInfoById();

    }


    private void getUserInfoById() {
        userIdVO userIdVO = new userIdVO();
        userIdVO.setUserId(userId);
        NetworkUtils.getWithParamsRequest(userIdVO, "/normal/getUserInfo", token, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(responseBody);
                int code = jsonObject.getIntValue("code");


                if (code == 200) {

                    JSONObject data = jsonObject.getJSONObject("data");

                    User user = data.toJavaObject(User.class);


                    // 在主线程中更新UI
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            userName.setText(user.getName());
                            userEmail.setText(user.getEmail());
                            Picasso.get()
                                    .load(imageURL + user.getAvatarUrl())
                                    .error(R.drawable.avatar3)  // error_image为加载失败时显示的图片
                                    .into(shapeableImageView);
                        }


                    });

                } else {

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }


    private void logout(View view) {

        DialogUtil.showCustomDialog(
                Objects.requireNonNull(getActivity()),
                getContext(),
                "Do you confirm to log out ?",
                v -> {
                    // Handle the logic for logging out
                    clearUserData();
                },null
                );

    }

    private void clearUserData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("comp5703", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userId");
        editor.remove("token");
        editor.apply();


        // 停止消息推送服务
        Intent serviceIntent = new Intent(getActivity(), MyService.class);
        getActivity().stopService(serviceIntent);


        // 重定向到登录页面
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // 关闭当前活动
        Objects.requireNonNull(getActivity()).finish();
    }



    private void dumpToMyMessage(View view) {

        MessagesFragment fragmentB = new MessagesFragment();

        // 准备要传递的数据
        Bundle args = new Bundle();
        args.putInt("userId", userId); // 这里的 "key" 是你传递数据的键名，"value" 是你要传递的值

        // 执行 Fragment 跳转
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, fragmentB); // R.id.fragment_container 是用于放置 Fragment 的容器
        transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
        transaction.commit();
        // 更新Activity中的Toolbar
        ((HomeUseActivity) Objects.requireNonNull(getActivity())).updateToolbar(true, "Message List");
    }

    private void dumpToProductContainer(View view) {
        // 在 FragmentA 中
        ProductContainerFragment fragmentB = new ProductContainerFragment();

//        // 准备要传递的数据
//        Bundle args = new Bundle();
//        args.putString("Category", category); // 这里的 "key" 是你传递数据的键名，"value" 是你要传递的值

        // 执行 Fragment 跳转
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, fragmentB); // R.id.fragment_container 是用于放置 Fragment 的容器
        transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
        transaction.commit();

    }

    private void dumpToOfferContainer(View view) {
        // 在 FragmentA 中
        OfferContainerFragment fragmentB = new OfferContainerFragment();

//        // 准备要传递的数据
//        Bundle args = new Bundle();
//        args.putString("Category", category); // 这里的 "key" 是你传递数据的键名，"value" 是你要传递的值

        // 执行 Fragment 跳转
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, fragmentB); // R.id.fragment_container 是用于放置 Fragment 的容器
        transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
        transaction.commit();
    }

    private void dumpToSetting(View view) {
        // 在 FragmentA 中
        SettingFragment fragmentB = new SettingFragment();

        // 执行 Fragment 跳转
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, fragmentB); // R.id.fragment_container 是用于放置 Fragment 的容器
        transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
        transaction.commit();
    }




    public void openCameraClick(View view){
        CharSequence[] items = {"Take picture", "Select from gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please choose the picture source");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 拍照
                        requestCameraPermission();
                        break;
                    case 1: // 从相册选择
                        requestGalleryPermission();
                        break;
                }
            }
        });
        builder.show();

    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{ Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            launchCamera();
        }
    }

    private void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY_PERMISSION);
        } else {
            launchGallery();
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
                Snackbar.make(rootView, "You need to enable camera permissions for subsequent operations", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        // 请求相册时候结果的处理
        if (requestCode == REQUEST_GALLERY_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchGallery();
            } else {
                // 权限被拒绝，添加额外的处理逻辑。
                Snackbar.make(rootView, "You need to enable album permissions to proceed.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

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

    private void launchGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            uploadPhoto(photoFile);

        }else if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    File file = new File(filePath);

                    uploadPhoto(file);
                }
            }
        }
    }

    private void uploadPhoto(File photoFile){
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
                    String useAvatarUrl = jsonObject.getString("data");
                    // 发送请求更新头像
                    updateUserInfo(useAvatarUrl);

                    // 在主线程中更新UI
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            Picasso.get()
                                    .load(imageURL + useAvatarUrl)
                                    .error(R.drawable.avatar3)  // error_image为加载失败时显示的图片
                                    .into(shapeableImageView);
                        }
                    });
                }
            }
        });

    }

    private void updateUserInfo(String useAvatarUrl) {
        // 将图片url数组转为字符串
        User user = new User();
        user.setId(Long.valueOf(userId));
        user.setAvatarUrl(useAvatarUrl);
        NetworkUtils.postJsonRequest(user, "/normal/updateUserAvatar", token, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(responseBody);
                int code = jsonObject.getIntValue("code");

                if (code == 200) {
                    Snackbar.make(rootView, "Update user information successful", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    private File createImageFile() throws IOException {
        // 创建一个唯一的文件名
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }





}
