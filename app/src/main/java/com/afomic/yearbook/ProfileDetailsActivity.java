package com.afomic.yearbook;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afomic.yearbook.data.Constant;
import com.afomic.yearbook.data.PreferenceManager;
import com.afomic.yearbook.fragment.AddCommentDialog;
import com.afomic.yearbook.fragment.OtherViewsFragment;
import com.afomic.yearbook.fragment.PersonalDetailsFragment;
import com.afomic.yearbook.fragment.SchoolDetailsFragment;
import com.afomic.yearbook.model.Comment;
import com.afomic.yearbook.model.Profile;
import com.afomic.yearbook.util.GlideApp;
import com.afomic.yearbook.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileDetailsActivity extends AppCompatActivity implements AddCommentDialog.CommentDialogListener {
    @BindView(R.id.profile_details_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.profile_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.fab_profile_details)
    FloatingActionButton mFab;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.imv_profile_image)
    ImageView profileImage;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private Profile currentProfile;
    private int selectedPage = 0;
    private PreferenceManager mPreferenceManager;
    private ProgressDialog mProgressDialog;
    private boolean bottomSheetIsShown = false;
    BottomSheetBehavior<LinearLayout> mSheetBehavior;
    @BindView(R.id.bottom_sheet_layout)
    LinearLayout linearBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        ButterKnife.bind(this);
        currentProfile = getIntent().getParcelableExtra(Constant.EXTRA_PROFILE);
        setSupportActionBar(mToolbar);
        requestPermission();
//        mPreferenceManager=new PreferenceManager(this);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(currentProfile.getNickName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mSheetBehavior = BottomSheetBehavior.from(linearBottomSheet);

        mSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        GlideApp.with(this)
                .load(currentProfile.getPictureUrl())
                .placeholder(R.drawable.image_placeholder)
                .into(profileImage);
        if (currentProfile == null) {
            Util.makeToast(this, "fuck up ti wa");
        }
        PagerAdapter adapter = new PagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mProgressDialog = new ProgressDialog(this);

        mPreferenceManager = new PreferenceManager(this);


        mCollapsingToolbarLayout.setContentScrimColor(currentProfile.getColor());
        mCollapsingToolbarLayout.setBackgroundColor(currentProfile.getColor());

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedPage = position;
                if (position == 2) {
                    mFab.setImageResource(R.drawable.ic_edit);
                } else {
                    mFab.setImageResource(R.drawable.ic_phone);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @OnClick(R.id.fab_profile_details)
    public void onFabPressed() {
        if (selectedPage == 2) {
            AddCommentDialog dialog = AddCommentDialog.newInstance();
            dialog.show(getSupportFragmentManager(), null);
        } else {
            mSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottomSheetIsShown = true;
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        Context mContext;

        public PagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PersonalDetailsFragment.newInstance(currentProfile);
                case 1:
                    return SchoolDetailsFragment.newInstance(currentProfile);

                case 2:
                    return OtherViewsFragment.newInstance(currentProfile);
            }
            return null;

        }

        @Override
        public int getCount() {
            return 3;
        }

        private String tabTitles[] = {"ABOUT", "SCHOOL LIFE", "ABOUT ME"};
        private int[] iconIds = {R.drawable.ic_person, R.drawable.ic_school, R.drawable.ic_comment};

        public View getTabView(int position) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.cutom_tab, null);
            TextView title = v.findViewById(R.id.tv_tab_title);
            ImageView tabIcon = v.findViewById(R.id.imv_tab_icon);

            title.setText(tabTitles[position]);
            tabIcon.setImageResource(iconIds[position]);
            return v;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSubmit(String comment) {
        mProgressDialog.setMessage("Posting comment");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        Comment item = new Comment();
        item.setComment(comment);
        item.setPosterName(mPreferenceManager.getUsername());
        item.setPosterPictureUrl(mPreferenceManager.getIconUrl());
        FirebaseDatabase.getInstance().getReference("comments")
                .child(currentProfile.getId())
                .push()
                .setValue(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mProgressDialog.dismiss();
                        Toast.makeText(ProfileDetailsActivity.this,
                                "Comment Added",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressDialog.dismiss();
                Toast.makeText(ProfileDetailsActivity.this,
                        "An Error occurred, please try again",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @OnClick(R.id.layout_call_user)
    public void callUser() {
        Uri number = Uri.parse("tel:" + currentProfile.getTelephoneNumber());
        Intent callIntent = new Intent(Intent.ACTION_CALL, number);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
           requestPermission();
            return;
        }
        startActivity(callIntent);
    }
    @OnClick(R.id.layout_chat_whatsapp)
    public void chatUser(){
       String number =currentProfile.getTelephoneNumber().substring(1);
       openWhatsApp(number);

    }
    @OnClick(R.id.layout_send_email)
    public void sendMail(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto: "+currentProfile.getEmail()));
        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

    private void openWhatsApp(String number) {
        String smsNumber = "234"+number;
        Intent sendIntent = new Intent("android.intent.action.MAIN");
        sendIntent.setAction(Intent.ACTION_VIEW);
        sendIntent.setPackage("com.whatsapp");
        String url = "https://api.whatsapp.com/send?phone=" +smsNumber;
        sendIntent.setData(Uri.parse(url));
        startActivity(sendIntent);
    }
    public void requestPermission(){
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {
                                Manifest.permission.CALL_PHONE
                        },100);
            }
        }


    }

    @Override
    public void onBackPressed() {
        if(bottomSheetIsShown){
            mSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            bottomSheetIsShown = false;
        }else {
            super.onBackPressed();
        }

    }
}
