package com.afomic.yearbook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.afomic.yearbook.adapter.SearchAdapter;
import com.afomic.yearbook.data.PreferenceManager;
import com.afomic.yearbook.model.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by afomic on 18-May-17.
 *
 */


public class UserSearchActivity extends AppCompatActivity {
    RecyclerView mSearchList;
    ArrayList<Object> searchResult;
    ArrayList<Profile> allUsers;
    SearchAdapter mAdapter;

    DatabaseReference userRef;

    @BindView(R.id.empty_view_layout)
    LinearLayout emptyViewLayout;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    PreferenceManager mPreferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        ButterKnife.bind(this);
        mPreferenceManager=new PreferenceManager(this);
        userRef= FirebaseDatabase.getInstance().getReference("user")
                .child(mPreferenceManager.getDepartment());
        Toolbar mToolbar=(Toolbar) findViewById(R.id.business_search_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar=getSupportActionBar();
        if(mActionBar!=null){
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        pullBusinessData();
        searchResult=new ArrayList<>();
        mAdapter=new SearchAdapter(this,searchResult);
        mSearchList= findViewById(R.id.business_search_result_list);

        mSearchList.setLayoutManager(new LinearLayoutManager(this));
        mSearchList.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_search,menu);
        MenuItem mItem=menu.findItem(R.id.menu_search);
        SearchView mBusinessSearchView=(SearchView) MenuItemCompat.getActionView(mItem);
        mBusinessSearchView.setIconifiedByDefault(false);
        ImageView mImage= mBusinessSearchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        mImage.setVisibility(View.GONE);
        mImage.setImageDrawable(null);
        mBusinessSearchView.setQueryHint("Search User");

        mBusinessSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.equals("")){
                    showProgressBar();
                    searchResult.clear();
                    mAdapter.notifyDataSetChanged();
                    searchByNickname(newText.toLowerCase());
                    searchByName(newText.toLowerCase());
                    if(searchResult.size()==0){
                        showErrorMessage();
                    }


                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    public void searchByName(String query){
        boolean headAdded=false;
        int count=0;
        int startPosition=searchResult.size()-1;
        for(Profile profile:allUsers){
            if(profile.getName().toLowerCase().contains(query)){
                if(!headAdded){
                    searchResult.add("Result by Name");
                    headAdded=true;
                }
                count++;
                searchResult.add(profile);
            }
        }
        hideProgressBar();
        //check if item is added to the list
        if(count>0){
            hideErrorMessage();
            mAdapter.notifyItemRangeInserted(startPosition,count);
        }
    }
    public void searchByNickname(String query){
        boolean headAdded=false;
        int count=0;
        int startPosition=searchResult.size()-1;
        for(Profile profile:allUsers){
            if(profile.getNickName().toLowerCase().contains(query)){
                if(!headAdded){
                    searchResult.add("Result by Nick Name");
                    headAdded=true;
                }
                count++;
                searchResult.add(profile);
            }
        }
        hideProgressBar();
        //check if item is added to the list
        if(count>0){
            hideErrorMessage();
            mAdapter.notifyItemRangeInserted(startPosition,count);
        }

    }
    public void pullBusinessData(){
        allUsers=new ArrayList<>();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Profile item=snapshot.getValue(Profile.class);
                    allUsers.add(item);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgressBar(){
        mProgressBar.setVisibility(View.GONE);
    }
    private void showErrorMessage(){
        emptyViewLayout.setVisibility(View.VISIBLE);
    }
    private void hideErrorMessage(){
        emptyViewLayout.setVisibility(View.GONE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAdapter!=null){
            mAdapter.unRegister(this);
        }

    }
}
