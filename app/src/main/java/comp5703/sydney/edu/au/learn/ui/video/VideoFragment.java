package comp5703.sydney.edu.au.learn.ui.video;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import comp5703.sydney.edu.au.learn.R;


public class VideoFragment extends Fragment {
    // Add any necessary member variables or methods here
    private Button mBtnListView;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }



}