package com.afomic.yearbook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.afomic.yearbook.data.Constant;
import com.afomic.yearbook.fragment.StatusDetailsFragment;
import com.afomic.yearbook.model.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusDetailActivity extends AppCompatActivity {
    @BindView(R.id.view_pager)
    ViewPager statusViewPager;
    DatabaseReference statusRef;

    int currentPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_status_detail);
        ButterKnife.bind(this);

        currentPosition=getIntent().getIntExtra(Constant.EXTRA_POSITION,0);


        statusRef= FirebaseDatabase.getInstance().getReference().child("status");
        statusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               ArrayList<Status> mStatuses=new ArrayList<>();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Status item=snapshot.getValue(Status.class);
                    mStatuses.add(item);
                }
                statusViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(),mStatuses));
                statusViewPager.setCurrentItem(currentPosition);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public static class PagerAdapter extends FragmentPagerAdapter{
        private ArrayList<Status> statusList;
        public PagerAdapter(FragmentManager fm,ArrayList<Status> statusList) {
            super(fm);
            this.statusList=statusList;
        }

        @Override
        public Fragment getItem(int position) {
            Status currentStatus=statusList.get(position);
            return StatusDetailsFragment.newInstance(currentStatus);
        }

        @Override
        public int getCount() {
            if(statusList==null){
                return 0;
            }
            return statusList.size();
        }
    }

}
