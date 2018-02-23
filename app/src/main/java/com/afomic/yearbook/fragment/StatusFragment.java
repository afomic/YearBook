package com.afomic.yearbook.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afomic.yearbook.CreateStatusActivity;
import com.afomic.yearbook.R;
import com.afomic.yearbook.adapter.StatusAdapter;
import com.afomic.yearbook.model.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by afomic on 12/13/17.
 */

public class StatusFragment extends Fragment {

    @BindView(R.id.rv_status_list)
    RecyclerView statusList;


    public static StatusFragment newInstance(){
        return  new StatusFragment();
    }

    Unbinder mUnbinder;
    StatusAdapter mStatusAdapter;
    ArrayList<Status> mStatuses;
    DatabaseReference statusRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_status,container,false);
        mUnbinder=ButterKnife.bind(this,v);

        mStatuses=new ArrayList<>();

        mStatusAdapter=new StatusAdapter(getActivity(),mStatuses);

        int columNumber=getColumnNo(getActivity());
        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),columNumber);
        statusList.setLayoutManager(layoutManager);
        statusList.setAdapter(mStatusAdapter);
        statusRef= FirebaseDatabase.getInstance().getReference("status");
        statusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mStatuses.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Status item=snapshot.getValue(Status.class);
                    mStatuses.add(item);
                }
                mStatusAdapter.notifyDataSetChanged();

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
    public static int getColumnNo(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 160;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.status_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.status_menu_add){
            Intent intent=new Intent(getActivity(), CreateStatusActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
