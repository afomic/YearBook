package com.afomic.yearbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.afomic.yearbook.data.Constant;
import com.afomic.yearbook.data.PreferenceManager;
import com.afomic.yearbook.fragment.CreatePersonalDetailsFragment;
import com.afomic.yearbook.fragment.CreateSchoolExperienceFragment;
import com.afomic.yearbook.fragment.StatusFragment;
import com.afomic.yearbook.model.Profile;
import com.afomic.yearbook.model.Token;
import com.afomic.yearbook.util.GlideApp;
import com.afomic.yearbook.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateProfileActivity extends AppCompatActivity implements
        CreateSchoolExperienceFragment.CreateSchoolListener,CreatePersonalDetailsFragment.CreatePersonalProfileCallback {

    private ProgressDialog mProgressDialog;

    private FragmentManager fm;

    private Profile currentProfile;
    private String departmentName;
    private String tokenId;
    private PreferenceManager mPreferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        ButterKnife.bind(this);
        mPreferenceManager=new PreferenceManager(getApplicationContext());

        departmentName=getIntent().getStringExtra(Constant.EXTRA_DEPARTMENT_NAME);
        tokenId=getIntent().getStringExtra(Constant.EXTRA_TOKEN_ID);
        fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.main_container);
        currentProfile=new Profile();
        if(fragment==null){
            CreatePersonalDetailsFragment frag=CreatePersonalDetailsFragment.newInstance(currentProfile);
            fm.beginTransaction().add(R.id.main_container,frag).commit();
        }

        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("Creating profile");
        mProgressDialog.setCancelable(false);

    }

    @Override
    public void onNext(Profile profile) {
        CreateSchoolExperienceFragment fragment=CreateSchoolExperienceFragment.newInstance(profile);
        displayFragment(fragment);

    }

    @Override
    public void onSubmit(final Profile profile) {
        mProgressDialog.show();
      DatabaseReference userRef=  FirebaseDatabase.getInstance()
                .getReference("user")
                .child(departmentName);
      final String userId=userRef.push().getKey();
      profile.setId(userId);
      userRef.child(userId)
              .setValue(profile)
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                      Token token=new Token();
                      token.setDepartment(departmentName);
                      token.setUsed(true);
                      token.setUserId(userId);
                      token.setPictureUrl(profile.getPictureUrl());
                      token.setId(tokenId);
                      FirebaseDatabase.getInstance()
                              .getReference("access")
                              .child(tokenId)
                              .setValue(token)
                              .addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      if(task.isSuccessful()){
                                          signUp(profile);
                                      }else {
                                          mProgressDialog.dismiss();
                                          Util.makeToast(CreateProfileActivity.this,"An Error has occurred");
                                      }
                                  }
                              });

                  }
              }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
              Util.makeToast(CreateProfileActivity.this,"Please try Again Later");
              mProgressDialog.dismiss();
          }
      });

    }

    @Override
    public void onPreviousClick() {
        CreatePersonalDetailsFragment fragment=CreatePersonalDetailsFragment.newInstance(currentProfile);
        displayFragment(fragment);
    }
    public void displayFragment(Fragment frag){
        fm.beginTransaction().replace(R.id.main_container,frag).commit();

    }
    public void signUp(final Profile profile){
        FirebaseAuth.getInstance()
                .signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mPreferenceManager.setDepartment(departmentName);
                            mPreferenceManager.setIconUrl(currentProfile.getPictureUrl());
                            mPreferenceManager.setUserId(currentProfile.getId());
                            mPreferenceManager.setUserLogin(true);
                            mPreferenceManager.setUsername(profile.getName());
                            Intent intent=new Intent(CreateProfileActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            mProgressDialog.dismiss();
                            Util.makeToast(CreateProfileActivity.this,"An Error has occurred");
                        }
                    }
                });
    }
}
