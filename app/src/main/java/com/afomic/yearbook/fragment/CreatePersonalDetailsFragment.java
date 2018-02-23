package com.afomic.yearbook.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.afomic.yearbook.R;
import com.afomic.yearbook.model.Profile;
import com.afomic.yearbook.util.GlideApp;
import com.afomic.yearbook.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by afomic on 12/19/17.
 *
 */

public class CreatePersonalDetailsFragment extends Fragment {
    @BindView(R.id.edt_address)
    EditText homeAddressEditText;
    @BindView(R.id.edt_email)
    EditText emailEditText;
    @BindView(R.id.edt_hobby)
    EditText hobbyEditText;
    @BindView(R.id.edt_nick_name)
    EditText nickNameEditText;
    @BindView(R.id.edt_phone_number)
    EditText phoneEditText;
    @BindView(R.id.edt_dob)
    EditText dobEditText;
    @BindView(R.id.edt_dislike)
    EditText dislikeEditText;
    @BindView(R.id.edt_name)
    EditText nameEditText;
    @BindView(R.id.spn_martial_status)
    Spinner martialStatusSpinner;
    @BindView(R.id.imv_profile_image)
    ImageView profileImage;

    private Uri imageUri;
    private ProgressDialog mProgressDialog;

    private CreatePersonalProfileCallback mProfileCallback;
    private static final String BUNDLE_PROFILE="profile";
    private Profile currentProfile;
    private Unbinder mUnbinder;
    private String martialStatus="Single";
    private String[] maritalStatusOption;
    public static CreatePersonalDetailsFragment newInstance(Profile profile){
        CreatePersonalDetailsFragment fragment=new CreatePersonalDetailsFragment();
        Bundle args=new Bundle();
        args.putParcelable(BUNDLE_PROFILE,profile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentProfile=getArguments().getParcelable(BUNDLE_PROFILE);
        mProgressDialog=new ProgressDialog(getActivity());
        maritalStatusOption=getActivity().getResources().getStringArray(R.array.martial_status);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mProfileCallback=(CreatePersonalProfileCallback)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mProfileCallback=null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_create_personal_details,container,false);
        mUnbinder= ButterKnife.bind(this,v);
        martialStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                martialStatus=maritalStatusOption[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        initializeView(currentProfile);
        return v;
    }
    public void initializeView(Profile profile){
        homeAddressEditText.setText(profile.getAddress());
        nameEditText.setText(profile.getName());
        phoneEditText.setText(profile.getTelephoneNumber());
        nickNameEditText.setText(profile.getNickName());
        hobbyEditText.setText(profile.getHobby());
        dislikeEditText.setText(profile.getDislikes());
        dobEditText.setText(profile.getDateOfBirth());
        emailEditText.setText(profile.getEmail());
        martialStatusSpinner.setSelection(findStatus(profile.getMartialStatus()));
        GlideApp.with(getContext())
                .load(profile.getPictureUrl())
                .placeholder(R.drawable.ic_person)
                .into(profileImage);
    }
    @OnClick(R.id.btn_registration_next)
    public void onNext(){
        if(isValidEntry()){
            mProgressDialog.setMessage("uploading Image");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            currentProfile.setAddress(Util.getString(homeAddressEditText));
            currentProfile.setDateOfBirth(Util.getString(dobEditText));
            currentProfile.setEmail(Util.getString(emailEditText));
            currentProfile.setDislikes(Util.getString(dislikeEditText));
            currentProfile.setHobby(Util.getString(hobbyEditText));
            currentProfile.setMartialStatus(martialStatus);
            currentProfile.setNickName(Util.getString(nickNameEditText));
            currentProfile.setTelephoneNumber(Util.getString(phoneEditText));
            currentProfile.setName(Util.getString(nameEditText));
            if(currentProfile.getPictureUrl()!=null){
                mProfileCallback.onNext(currentProfile);
            }else {
                UploadTask profileImageTask= FirebaseStorage.getInstance().getReference("profile")
                        .child(imageUri.getLastPathSegment())
                        .putFile(imageUri);
                profileImageTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        mProgressDialog.dismiss();
                        if(task.isSuccessful()){
                            String pictureUrl=task.getResult().getDownloadUrl().toString();
                            currentProfile.setPictureUrl(pictureUrl);
                            mProfileCallback.onNext(currentProfile);
                        }else {
                            Util.makeToast(getActivity(),"Uploading Image Failed, Try again");
                        }
                    }
                });
            }


        }
    }
    @OnClick(R.id.fab_image)
    public void selectImage(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4,3)
                .start(getContext(),this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
    public interface CreatePersonalProfileCallback{
        void onNext(Profile profile);
    }
    public boolean isValidEntry(){
        if(Util.isEmpty(nameEditText)){
            Util.makeToast(getActivity(),"Please Enter your full name");
            return false;
        }
        if(Util.isEmpty(nickNameEditText)){
            Util.makeToast(getActivity(),"Please Enter your Nick Name");
            return false;
        }
        if(Util.isEmpty(hobbyEditText)){
            Util.makeToast(getActivity(),"Please Enter your Hobbies");
            return false;
        }
        if(Util.isEmpty(dislikeEditText)){
            Util.makeToast(getActivity(),"Please Enter your dislikes");
            return false;
        }
        if(Util.isEmpty(homeAddressEditText)){
            Util.makeToast(getActivity(),"Please Enter your home address");
            return false;
        }
        if(Util.isEmpty(dobEditText)){
            Util.makeToast(getActivity(),"Please Enter your birth date");
            return false;
        }
        if(Util.isEmpty(emailEditText)){
            Util.makeToast(getActivity(),"Please Enter your Email");
            return false;
        }
        if(Util.isEmpty(phoneEditText)){
            Util.makeToast(getActivity(),"Please Enter your Phone Number");
            return false;
        }
        if(imageUri==null&&currentProfile.getPictureUrl()==null){
            Util.makeToast(getActivity(),"Please Select your Image");
            return false;
        }
        return true;

    }
    public int findStatus(String martialStatus){
        for(int i=0;i<maritalStatusOption.length;i++){
            if(maritalStatusOption[i].equals(martialStatus)){
                return i;
            }
        }
        return 0;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                Util.makeToast(getActivity(),"uri "+imageUri.getPath());
                GlideApp.with(getActivity())
                        .load(imageUri)
                        .centerCrop()
                        .placeholder(R.drawable.image_placeholder)
                        .into(profileImage);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
