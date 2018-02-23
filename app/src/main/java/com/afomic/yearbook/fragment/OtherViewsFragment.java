package com.afomic.yearbook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afomic.yearbook.R;
import com.afomic.yearbook.adapter.CommentAdapter;
import com.afomic.yearbook.model.Comment;
import com.afomic.yearbook.model.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by afomic on 12/18/17.
 */

public class OtherViewsFragment extends Fragment {
    @BindView(R.id.rv_others_view)
    RecyclerView othersCommentList;

    CommentAdapter mAdapter;
    ArrayList<Comment> mComments;
    Unbinder mUnbinder;
    private static final String BUNDLE_PROFILE="profile";
    private Profile currentProfile;
    public static OtherViewsFragment newInstance(Profile currentProfile){
        OtherViewsFragment fragment=new OtherViewsFragment();
        Bundle args=new Bundle();
        args.putParcelable(BUNDLE_PROFILE,currentProfile);
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
        View v=inflater.inflate(R.layout.fragment_other_views,container,false);
        mUnbinder= ButterKnife.bind(this,v);

        mComments=new ArrayList<>();
        othersCommentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter=new CommentAdapter(getActivity(),mComments);
        othersCommentList.setAdapter(mAdapter);
        othersCommentList.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        FirebaseDatabase.getInstance().getReference("comments")
                .child(currentProfile.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mComments.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Comment item=snapshot.getValue(Comment.class);
                            mComments.add(item);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
