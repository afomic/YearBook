package com.afomic.yearbook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afomic.yearbook.ProfileDetailsActivity;
import com.afomic.yearbook.R;
import com.afomic.yearbook.UserSearchActivity;
import com.afomic.yearbook.adapter.PersonAdapter;
import com.afomic.yearbook.data.PreferenceManager;
import com.afomic.yearbook.model.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by afomic on 12/15/17.
 *
 */

public class HomeFragment extends Fragment {
    public static HomeFragment newInstance(){
        return new HomeFragment();
    }
    RecyclerView grid;
    private ArrayList<Profile> mProfiles;
    private DatabaseReference profileRef;
    private PreferenceManager mPreferenceManager;
    private ArrayList<Object> searchList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.person_viewer,container,false);
        grid=view.findViewById(R.id.person_grid);
        int screenWidth= getResources().getConfiguration().screenWidthDp;
        int numberOfRows=screenWidth/140;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),numberOfRows);
        grid.setLayoutManager(mLayoutManager);

        grid.setItemAnimator(new DefaultItemAnimator());

        mProfiles=new ArrayList<>();
        mPreferenceManager=new PreferenceManager(getActivity());
        final PersonAdapter adapter=new PersonAdapter(getActivity(),mProfiles);

        grid.setAdapter(adapter);

        profileRef= FirebaseDatabase.getInstance()
                .getReference("user")
                .child(mPreferenceManager.getDepartment());
        profileRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mProfiles.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Profile item=snapshot.getValue(Profile.class);
                            mProfiles.add(item);
                        }
                        if(mProfiles.size()>0){
                            adapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
       

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_user_search){
            Intent intent=new Intent(getActivity(), UserSearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
