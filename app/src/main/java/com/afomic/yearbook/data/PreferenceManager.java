package com.afomic.yearbook.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by afomic on 10/8/17.
 */

public class PreferenceManager {
    private SharedPreferences mSharedPreferences;

    private static final String PREFERENCE_FILE_NAME="com.afomic.martcol";
    private static final String PREF_USER_LOGIN="login";
    private static final String PREF_USER_ID="id";
    private static final String PREF_ICON_URL="icon_url";
    private static final String PREF_USER_NAME="user_name";
    private static final String PREF_DEPARTMENT="department";


   public PreferenceManager(Context context){
       mSharedPreferences=context.getSharedPreferences(PREFERENCE_FILE_NAME,Context.MODE_PRIVATE);
   }
   public void setUserLogin(Boolean isLogin){
       SharedPreferences.Editor mEditor=mSharedPreferences.edit();
       mEditor.putBoolean(PREF_USER_LOGIN,isLogin);
       mEditor.apply();
   }
    public void setDepartment(String department){
        SharedPreferences.Editor mEditor=mSharedPreferences.edit();
        mEditor.putString(PREF_DEPARTMENT,department);
        mEditor.apply();
    }


    public void setUserId(String userId){
        SharedPreferences.Editor mEditor=mSharedPreferences.edit();
        mEditor.putString(PREF_USER_ID,userId);
        mEditor.apply();
    }
    public void setUsername(String username){
        SharedPreferences.Editor mEditor=mSharedPreferences.edit();
        mEditor.putString(PREF_USER_NAME,username);
        mEditor.apply();
    }
    public void setIconUrl(String iconUrl){
        SharedPreferences.Editor mEditor=mSharedPreferences.edit();
        mEditor.putString(PREF_ICON_URL,iconUrl);
        mEditor.apply();
    }
    public String getUserId(){
        return mSharedPreferences.getString(PREF_USER_ID,"");
    }
    public boolean isUserLogin(){
        return mSharedPreferences.getBoolean(PREF_USER_LOGIN,false);
    }
    public String getIconUrl(){
        return mSharedPreferences.getString(PREF_ICON_URL,null);
    }
    public String getDepartment(){
        return mSharedPreferences.getString(PREF_DEPARTMENT,null);
    }
    public String getUsername(){
        return mSharedPreferences.getString(PREF_USER_NAME,null);
    }

}
