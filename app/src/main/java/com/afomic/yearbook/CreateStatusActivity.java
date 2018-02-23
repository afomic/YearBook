package com.afomic.yearbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afomic.yearbook.model.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateStatusActivity extends BaseActivity {
    @BindView(R.id.btn_upload_file)
    Button uploadFileButton;
    @BindView(R.id.edt_status_description)
    EditText statusDescriptionEditText;
    @BindView(R.id.edt_status_title)
    EditText statusTitleEditText;
    @BindView(R.id.tv_file_name)
    TextView fileNameTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private Uri fileUri;

    public static final int REQUEST_FILE_CODE=1023;

    private ProgressDialog mProgressDialog;
    StorageReference mStorageReference;
    DatabaseReference statusRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_status);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
//        mPreferenceManager=new PreferenceManager(this);
        ActionBar actionBar=getSupportActionBar();

        if(actionBar!=null){
            actionBar.setTitle("Create Status");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        requestPermission();

        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        statusRef= FirebaseDatabase.getInstance().getReference("status");
        mStorageReference= FirebaseStorage.getInstance().getReference().child("status");
    }
    public boolean isValidEntry(){
        if(isEmpty(statusTitleEditText)){
            makeToast(this,"Please Provide the status title");
            return false;
        }
        if(fileUri==null){
            makeToast(this,"Please Provide the file to be uploaded");
            return false;
        }
        return  true;

    }
    @OnClick(R.id.btn_upload_file)
    public void uploadFile(){
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/* video/*");
            startActivityForResult(Intent.createChooser(intent,"Select File"),REQUEST_FILE_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*", "video/*"});
            startActivityForResult(intent, REQUEST_FILE_CODE);
        }


    }
    @OnClick(R.id.btn_create_status)
    public void createStatus(){
        if(isValidEntry()){
            mProgressDialog.setMessage("Uploading file....");
            mProgressDialog.show();
            String filepath=fileUri.getPath();

            if(isVideoFile(filepath)){
                Bitmap bMap = ThumbnailUtils.createVideoThumbnail(filepath, MediaStore.Video.Thumbnails.MINI_KIND);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                mStorageReference.child("thumbnail_"+System.currentTimeMillis()+".jpeg")
                        .putBytes(data)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    Status status=new Status();
                                    String thumbnailUrl=task.getResult().getDownloadUrl().toString();
                                    status.setThumbnailUrl(thumbnailUrl);
                                    uploadFile(status);
                                }else {
                                    makeToast(CreateStatusActivity.this,"An error occurred");
                                }
                            }
                        });

            }else{
                Status status=new Status();
                uploadFile(status);

            }



        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==REQUEST_FILE_CODE){
            String filePath="file://"+getRealPathFromURI(data.getData());
            Log.e("tag", "onActivityResult: "+filePath);
            if(filePath!=null&&!isImageFile(filePath)&&!isVideoFile(filePath)){
                makeToast(CreateStatusActivity.this,"Please Select a Valid File");
            } else {
                fileUri=Uri.parse(filePath);
                fileNameTextView.setText(fileUri.getLastPathSegment());
            }



        }
    }
    public boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }
    public boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
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
    public void uploadFile(final Status status){
        UploadTask task=mStorageReference.child(fileUri.getLastPathSegment())
                .putFile(fileUri);
        task.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    int fileType=isImageFile(fileUri.getPath())? Status.Type.picture:Status.Type.video;
                    String fileUrl=task.getResult().getDownloadUrl().toString();
                    status.setFileUrl(fileUrl);
                    status.setType(fileType);
                    status.setCaption(getString(statusDescriptionEditText));
                    status.setTitle(getString(statusTitleEditText));
                    String id=statusRef.push().getKey();
                    status.setId(id);


                    mProgressDialog.setMessage("Creating status..");
                    statusRef.child(id)
                            .setValue(status)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProgressDialog.dismiss();
                                    finish();
                                }
                            });
                }else {
                    mProgressDialog.dismiss();
                    Log.e("Error", "onComplete: ",task.getException() );
                    makeToast(CreateStatusActivity.this,"An error occurred");

                }

            }
        });
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(CreateStatusActivity.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

}
