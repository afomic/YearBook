package com.afomic.yearbook.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by afomic on 12/13/17.
 *
 */

public class Status implements Parcelable{
    private String id;
    private String fileUrl;
    private int type;
    private String title;
    private String caption;
    private String thumbnailUrl;

    public Status(){

    }


    protected Status(Parcel in) {
        fileUrl = in.readString();
        type = in.readInt();
        title = in.readString();
        caption = in.readString();
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(fileUrl);
        dest.writeInt(type);
        dest.writeString(title);
        dest.writeString(caption);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public class Type{
        public static final int video=0;
        public static final int picture=1;
    }

}
