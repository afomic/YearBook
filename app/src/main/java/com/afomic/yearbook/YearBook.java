package com.afomic.yearbook;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by afomic on 12/17/17.
 * 
 */

public class YearBook extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseStorage.getInstance().setMaxUploadRetryTimeMillis(1000);
        FirebaseStorage.getInstance().setMaxOperationRetryTimeMillis(1000);

    }
}
