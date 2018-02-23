package com.afomic.yearbook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afomic.yearbook.R;
import com.afomic.yearbook.model.Profile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by afomic on 12/18/17.
 *
 */

public class SchoolDetailsFragment extends Fragment {
    @BindView(R.id.tv_best_course)
    TextView bestCourseTextView;
    @BindView(R.id.tv_best_lecturer)
    TextView bestLecturerTextView;
    @BindView(R.id.tv_best_moment)
    TextView bestMomentTextView;
    @BindView(R.id.tv_best_location)
    TextView bestLocationTextView;
    @BindView(R.id.tv_leadership_position)
    TextView leadershipTextView;
    @BindView(R.id.tv_class_crush)
    TextView classCrushTextView;
    @BindView(R.id.leadership_layout)
    LinearLayout leadershipLayout;
    @BindView(R.id.class_crush_layout)
    LinearLayout classCrushLayout;
    private static final String BUNDLE_PROFILE="profile";

    private Profile currentProfile;
    private Unbinder mUnbinder;
    public static SchoolDetailsFragment newInstance(Profile profile){
        SchoolDetailsFragment fragment=new SchoolDetailsFragment();
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
        View v=inflater.inflate(R.layout.fragment_school_experience,container,false);
        mUnbinder= ButterKnife.bind(this,v);
        bestCourseTextView.setText(currentProfile.getBestCourse());
        bestLecturerTextView.setText(currentProfile.getBestLecturer());
        bestLocationTextView.setText(currentProfile.getBestLocation());
        bestMomentTextView.setText(currentProfile.getBestMoment());
        if(!currentProfile.getClassCrush().equals("")){
            classCrushTextView.setText(currentProfile.getClassCrush());
        }else {
            classCrushLayout.setVisibility(View.GONE);
        }
        if(!currentProfile.getPostHeld().equals("")){
            leadershipTextView.setText(currentProfile.getPostHeld());
        }else {
            leadershipLayout.setVisibility(View.GONE);
        }
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
