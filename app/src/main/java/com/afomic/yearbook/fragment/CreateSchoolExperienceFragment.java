package com.afomic.yearbook.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.afomic.yearbook.R;
import com.afomic.yearbook.model.Profile;
import com.afomic.yearbook.util.Util;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by afomic on 12/19/17.
 *
 */

public class CreateSchoolExperienceFragment extends Fragment {
    @BindView(R.id.edt_best_course)
    EditText bestCourseEditText;
    @BindView(R.id.edt_best_lecturer)
    EditText bestLecturerEditText;
    @BindView(R.id.edt_best_moment)
    EditText bestMomentEditText;
    @BindView(R.id.edt_best_location)
    EditText bestLocationEditText;
    @BindView(R.id.edt_leadership_position)
    EditText leadershipEditText;
    @BindView(R.id.edt_class_crush)
    EditText classCrushEditText;

    private static final String BUNDLE_PROFILE="profile";

    private Profile currentProfile;
    private Unbinder mUnbinder;
    private CreateSchoolListener mListener;
    public static CreateSchoolExperienceFragment newInstance(Profile profile){
        CreateSchoolExperienceFragment fragment=new CreateSchoolExperienceFragment();
        Bundle args=new Bundle();
        args.putParcelable(BUNDLE_PROFILE,profile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener=(CreateSchoolListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener=null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentProfile=getArguments().getParcelable(BUNDLE_PROFILE);

    }
    @OnClick(R.id.btn_registration_previous)
    public void onPreviousButtonClicked(){
        mListener.onPreviousClick();

    }
    @OnClick(R.id.btn_registration_submit)
    public void onSubmit(){
        if(isValidEntry()){
            currentProfile.setBestCourse(Util.getString(bestCourseEditText));
            currentProfile.setBestLecturer(Util.getString(bestLecturerEditText));
            currentProfile.setBestLocation(Util.getString(bestLocationEditText));
            currentProfile.setBestMoment(Util.getString(bestMomentEditText));
            currentProfile.setClassCrush(Util.getString(classCrushEditText));
            currentProfile.setPostHeld(Util.getString(leadershipEditText));
            mListener.onSubmit(currentProfile);
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_create_school_details,container,false);
        mUnbinder=ButterKnife.bind(this,v);
        return v;
    }
    public boolean isValidEntry(){
        if(Util.isEmpty(bestCourseEditText)){
            Util.makeToast(getActivity(),"Please Enter your best Course");
            return false;
        }
        if(Util.isEmpty(bestLecturerEditText)){
            Util.makeToast(getActivity(),"Please Enter your Favorite Lecturer");
            return false;
        }
        return true;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
    public interface CreateSchoolListener{
        void onSubmit(Profile profile);
        void onPreviousClick();

    }
}
