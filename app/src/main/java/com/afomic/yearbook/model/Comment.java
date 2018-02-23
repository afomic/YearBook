package com.afomic.yearbook.model;

/**
 *
 * Created by afolabi michael on 12/18/17.
 */

public class Comment {
    private String comment;
    private String posterName;
    private String posterPictureUrl;
    private long commentTime;
    public Comment(){
        commentTime=System.currentTimeMillis();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getPosterPictureUrl() {
        return posterPictureUrl;
    }

    public void setPosterPictureUrl(String posterPictureUrl) {
        this.posterPictureUrl = posterPictureUrl;
    }

    public long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }
}
