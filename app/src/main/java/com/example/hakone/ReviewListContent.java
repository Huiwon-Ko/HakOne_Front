package com.example.hakone;

import java.util.HashMap;
import java.util.List;

public class ReviewListContent {
    private long review_id;
    private String profile_pic;
    private String username;
    private float score;
    private String created_date;
    private String content;
    private String academy_name;
    private float user_score;
    // String academy_name, float user_score

    public ReviewListContent(long review_id, String profile_pic,String username,float score, String created_date, String content) {
        this.review_id = review_id;
        this.profile_pic = profile_pic;
        this.username = username;
        this.score = score;
        this.created_date = created_date;
        this.content = content;
        this.academy_name = academy_name;
        this.user_score = user_score;
    }

    public long getReview_id() {
        return review_id;
    }

    public void setReview_id(long review_id) {
        this.review_id = review_id;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAcademy_name() {
        return academy_name;
    }

    public void setAcademy_name(String academy_name) {
        this.academy_name = academy_name;
    }

    public float getUser_score() {
        return user_score;
    }

    public void setUser_score(float user_score) {
        this.user_score = user_score;
    }


}







