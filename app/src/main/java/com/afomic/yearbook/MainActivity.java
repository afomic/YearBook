package com.afomic.yearbook;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.afomic.yearbook.data.PreferenceManager;
import com.afomic.yearbook.model.Department;
import com.afomic.yearbook.model.Token;
import com.afomic.yearbook.util.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class  MainActivity extends AppCompatActivity {
    PreferenceManager mPreferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPreferenceManager=new PreferenceManager(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mPreferenceManager.isUserLogin()){
                    showHomeActivity();
                }else {
                    showSignUpActivity();
                }
            }
        },3000);
    }
    public void showHomeActivity(){
        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
    public void showSignUpActivity(){
        Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
        startActivity(intent);
        finish();
    }
    public void createAccessToken(){
        DatabaseReference tokenRef= FirebaseDatabase.getInstance().getReference("access");
        for(int i=0;i<100;i++){
            String id= Util.getSaltString();
            Token accessToken =new Token();
            accessToken.setId(id);
            accessToken.setUsed(false);
            tokenRef.child(id)
                    .setValue(accessToken);
        }

    }
    public void createDepartment(){
        DatabaseReference tokenRef= FirebaseDatabase.getInstance().getReference("department");
        for(int i=0;i<100;i++){
            Department department=new Department();
            department.setAcronym("sample");
            department.setName("Department of sampe");
            tokenRef.push()
                    .setValue(department);
        }

    }
}
