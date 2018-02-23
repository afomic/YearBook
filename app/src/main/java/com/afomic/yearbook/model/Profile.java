package com.afomic.yearbook.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.afomic.yearbook.util.ColorGenerator;


/**
 * Created by afomic on 17-Oct-16.
 *
 */
public class Profile implements Parcelable{
    private  String name;
    private String telephoneNumber;
    private String email;
    private String pictureUrl;
    private String id;
    private int color;
    private String dateOfBirth;
    private String martialStatus;
    private String address;
    private String bestLocation;
    private String bestMoment;
    private String bestLecturer;
    private String bestCourse;
    private String postHeld;
    private String classCrush;
    private String dislikes;
    private String hobby;
    private String nickName;

    protected Profile(Parcel in) {
        name = in.readString();
        telephoneNumber = in.readString();
        email = in.readString();
        pictureUrl = in.readString();
        id = in.readString();
        color = in.readInt();
        dateOfBirth = in.readString();
        martialStatus = in.readString();
        address = in.readString();
        bestLocation = in.readString();
        bestMoment = in.readString();
        bestLecturer = in.readString();
        bestCourse = in.readString();
        postHeld = in.readString();
        classCrush = in.readString();
        dislikes = in.readString();
        hobby = in.readString();
        nickName=in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMartialStatus() {
        return martialStatus;
    }

    public void setMartialStatus(String martialStatus) {
        this.martialStatus = martialStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBestLocation() {
        return bestLocation;
    }

    public void setBestLocation(String bestLocation) {
        this.bestLocation = bestLocation;
    }

    public String getBestMoment() {
        return bestMoment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(telephoneNumber);
        dest.writeString(email);
        dest.writeString(pictureUrl);
        dest.writeString(id);
        dest.writeInt(color);
        dest.writeString(dateOfBirth);
        dest.writeString(martialStatus);
        dest.writeString(address);
        dest.writeString(bestLocation);
        dest.writeString(bestMoment);
        dest.writeString(bestLecturer);
        dest.writeString(bestCourse);
        dest.writeString(postHeld);
        dest.writeString(classCrush);
        dest.writeString(dislikes);
        dest.writeString(hobby);
        dest.writeString(nickName);
    }

    public void setBestMoment(String bestMoment) {
        this.bestMoment = bestMoment;
    }

    public String getBestLecturer() {
        return bestLecturer;
    }

    public void setBestLecturer(String bestLecturer) {
        this.bestLecturer = bestLecturer;
    }

    public String getBestCourse() {
        return bestCourse;
    }

    public void setBestCourse(String bestCourse) {
        this.bestCourse = bestCourse;
    }

    public String getPostHeld() {
        return postHeld;
    }

    public void setPostHeld(String postHeld) {
        this.postHeld = postHeld;
    }

    public String getClassCrush() {
        return classCrush;
    }

    public void setClassCrush(String classCrush) {
        this.classCrush = classCrush;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public Profile(){
        color = ColorGenerator.MATERIAL.getRandomColor();
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
