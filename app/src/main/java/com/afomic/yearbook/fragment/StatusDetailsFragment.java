package com.afomic.yearbook.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.afomic.yearbook.R;
import com.afomic.yearbook.model.Status;
import com.afomic.yearbook.util.GlideApp;
import com.afomic.yearbook.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by afomic on 12/17/17.
 */

public class StatusDetailsFragment extends Fragment implements EasyVideoCallback {
    @BindView(R.id.imv_status_image)
    ImageView statusImage;
    @BindView(R.id.tv_status_description)
    TextView statusDescriptionTextView;
    @BindView(R.id.player)
    EasyVideoPlayer mEasyVideoPlayer;
    @BindView(R.id.picture_layout)
    RelativeLayout pictureLayout;
    @BindView(R.id.progress)
    ProgressBar mProgressBar;

    private Unbinder mUnbinder;

    private Status currentStatus;
    private static final String BUNDLE_STATUS="status";

    public static StatusDetailsFragment newInstance(Status currentStatus){
        StatusDetailsFragment fragment=new StatusDetailsFragment();
        Bundle args=new Bundle();
        args.putParcelable(BUNDLE_STATUS,currentStatus);
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_status_details,container,false);
        mUnbinder= ButterKnife.bind(this,v);
        if(currentStatus.getCaption()!=null){
            statusDescriptionTextView.setText(currentStatus.getCaption());

        }else {
            statusDescriptionTextView.setVisibility(View.GONE);
        }
        if(currentStatus.getType()==Status.Type.picture){
            GlideApp.with(getActivity())
                    .load(currentStatus.getFileUrl())
                    .optionalCenterInside()
                    .into(statusImage);
        }else {
            pictureLayout.setVisibility(View.GONE);
            mEasyVideoPlayer.setVisibility(View.VISIBLE);
            mEasyVideoPlayer.setCallback(this);
            mEasyVideoPlayer.setSource(Uri.parse(currentStatus.getFileUrl()));

        }


        return v;
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        Util.makeToast(getActivity(),"Error loading Video");

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
