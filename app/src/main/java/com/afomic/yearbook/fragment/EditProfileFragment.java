package com.afomic.yearbook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afomic.yearbook.R;

/**
 * Created by afomic on 12/25/17.
 */

public class EditProfileFragment extends Fragment {
    public static EditProfileFragment newInstance(){
        return new EditProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_edit_profile,container,false);
        return view;
    }
}
