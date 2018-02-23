package com.afomic.yearbook;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by afomic on 12/17/17.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public static boolean isEmpty(EditText edt){
        return TextUtils.isEmpty(getString(edt));
    }
    public  static String getString(EditText edt){
        return edt.getText().toString();
    }
    public static void makeToast(Context ctx, String message){
        Toast.makeText(ctx,message,Toast.LENGTH_SHORT).show();
    }
}
