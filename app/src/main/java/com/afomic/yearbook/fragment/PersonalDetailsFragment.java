package com.afomic.yearbook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afomic.yearbook.R;
import com.afomic.yearbook.model.Profile;
import com.afomic.yearbook.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by afomic on 12/18/17.
 *
 */

public class PersonalDetailsFragment extends Fragment {
    @BindView(R.id.tv_address)
    TextView homeAddressTextView;
    @BindView(R.id.tv_email)
    TextView emailTextView;
    @BindView(R.id.tv_hobby)
    TextView hobbyTextView;
    @BindView(R.id.tv_nick_name)
    TextView nickNameTextView;
    @BindView(R.id.tv_phone)
    TextView phoneTextView;
    @BindView(R.id.tv_dob)
    TextView dobTextView;
    @BindView(R.id.tv_dislikes)
    TextView dislikeTextView;
    @BindView(R.id.tv_name)
    TextView nameTextView;
    @BindView(R.id.tv_martial_status)
    TextView martialStatusTextView;

    private static final String BUNDLE_PROFILE="profile";
    private Profile currentProfile;
    private Unbinder mUnbinder;
    public static PersonalDetailsFragment newInstance(Profile profile){
        PersonalDetailsFragment fragment=new PersonalDetailsFragment();
        Bundle args=new Bundle();
        args.putParcelable(BUNDLE_PROFILE,profile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentProfile=getArguments().getParcelable(BUNDLE_PROFILE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_personal_details,container,false);
        mUnbinder= ButterKnife.bind(this,v);
        hobbyTextView.setText(currentProfile.getHobby());
        homeAddressTextView.setText(currentProfile.getAddress());
        dislikeTextView.setText(currentProfile.getDislikes());
        dobTextView.setText(currentProfile.getDateOfBirth());
        emailTextView.setText(currentProfile.getEmail());
        nameTextView.setText(currentProfile.getName());
        phoneTextView.setText(currentProfile.getTelephoneNumber());
        martialStatusTextView.setText(currentProfile.getMartialStatus());
        nickNameTextView.setText(currentProfile.getNickName());
        return v;
    }

}
