package com.afomic.yearbook;

import android.*;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.afomic.yearbook.fragment.AboutDepartmentFragment;
import com.afomic.yearbook.fragment.EditProfileFragment;
import com.afomic.yearbook.fragment.HomeFragment;
import com.afomic.yearbook.fragment.StatusFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    DrawerLayout mDrawer;
    FragmentManager fm;
    NavigationView mNavigationView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
//        mPreferenceManager=new PreferenceManager(this);
        ActionBar actionBar=getSupportActionBar();

        if(actionBar!=null){
            actionBar.setTitle("Home");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze);
        }
        fm=getSupportFragmentManager();
        requestPermission();
        Fragment fragment=fm.findFragmentById(R.id.main_container);
        if(fragment==null){
            StatusFragment frag=StatusFragment.newInstance();
            fm.beginTransaction().add(R.id.main_container,frag).commit();
        }

        mNavigationView= findViewById(R.id.navigation_view);
        mDrawer=findViewById(R.id.drawer_layout);
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mDrawer.closeDrawers();
                        switch (menuItem.getItemId()){
                            case R.id.menu_status:
                                StatusFragment frag=StatusFragment.newInstance();
                                displayFragment(frag);
                                break;
                            case R.id.menu_profiles:
                                HomeFragment homeFragment=HomeFragment.newInstance();
                                displayFragment(homeFragment);
                                break;
                            case  R.id.menu_edit_profile:
                                EditProfileFragment editProfileFragment=EditProfileFragment.newInstance();
                                displayFragment(editProfileFragment);
                                break;
                            case R.id.menu_about:
                                AboutDepartmentFragment departmentFragment=AboutDepartmentFragment.newInstance();
                                displayFragment(departmentFragment);
                                break;

                        }
                        return true;
                    }
                });
    }
    public void displayFragment(Fragment frag){
        mDrawer.closeDrawers();
        fm.beginTransaction().replace(R.id.main_container,frag).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawer.openDrawer(Gravity.START,true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void requestPermission(){
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },100);
            }
        }


    }

}
