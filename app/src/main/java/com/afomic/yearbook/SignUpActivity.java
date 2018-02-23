package com.afomic.yearbook;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.afomic.yearbook.adapter.SpinnerAdapter;
import com.afomic.yearbook.data.Constant;
import com.afomic.yearbook.data.PreferenceManager;
import com.afomic.yearbook.model.Department;
import com.afomic.yearbook.model.Token;
import com.afomic.yearbook.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.edt_access_token)
    EditText tokenEditText;

    PreferenceManager mPreferenceManager;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        requestPermission();
        mPreferenceManager=new PreferenceManager(this);
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("verifying Access Token...");

    }
    @OnClick(R.id.btn_create_account)
    public void createAccount(){
        if(isValidForm()){
            mProgressDialog.show();
            final String accessToken=Util.getString(tokenEditText);
            FirebaseDatabase.getInstance()
                    .getReference("access")
                    .child(accessToken)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                mProgressDialog.dismiss();
                                Token token=dataSnapshot.getValue(Token.class);
                                if(token.isUsed()){
                                    mPreferenceManager.setUserLogin(true);
                                    mPreferenceManager.setDepartment(token.getDepartment());
                                    mPreferenceManager.setUserId(token.getUserId());
                                    mPreferenceManager.setIconUrl(token.getPictureUrl());
                                    Intent intent= new Intent(SignUpActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else {
                                    Intent intent= new Intent(SignUpActivity.this,CreateProfileActivity.class);
                                    intent.putExtra(Constant.EXTRA_DEPARTMENT_NAME,"sample");
                                    intent.putExtra(Constant.EXTRA_TOKEN_ID,accessToken);
                                    startActivity(intent);
                                    finish();
                                }

                            }else {
                                Util.makeToast(SignUpActivity.this,"Invalid Access Token");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


        }
    }
    public boolean isValidForm(){
        if(Util.isEmpty(tokenEditText)||tokenEditText.getText().length()<16){
            Util.makeToast(SignUpActivity.this,"Please provide a valid Access Token");
            return false;
        }
        return true;
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
