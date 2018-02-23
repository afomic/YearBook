package com.afomic.yearbook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afomic.yearbook.model.Status;

/**
 * Created by afomic on 12/15/17.
 */

public class VideoFragment extends Fragment {

    private static final String BUNDLE_STATUS="status";
    private Status currentStatus;
    public static VideoFragment newInstance(Status status){
        VideoFragment fragment=new VideoFragment();
        Bundle args=new Bundle();
        args.putParcelable(BUNDLE_STATUS,status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentStatus=getArguments().getParcelable(BUNDLE_STATUS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
