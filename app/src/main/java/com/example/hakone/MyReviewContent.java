package com.example.hakone;

public class MyReviewContent {
    private long review_id;
    private String created_date;
    private String content;
    private String academy_name;
    private float user_score;

    private long academy_id;


    public MyReviewContent(long academy_id, long review_id, String academy_name, String content, float user_score, String created_date) {
        this.academy_id = academy_id;
        this.review_id = review_id;
        this.academy_name = academy_name;
        this.content = content;
        this.user_score = user_score;
        this.created_date = created_date;
    }

    public long getAcademy_id() {
        return academy_id;
    }

    public void setAcademy_id(long academy_id) {
        this.academy_id = academy_id;
    }

    public long getReview_id() {
        return review_id;
    }

    public void setReview_id(long review_id) {
        this.review_id = review_id;
    }

    public String getAcademy_name() {
        return academy_name;
    }

    public void setAcademy_name(String academy_name) {
        this.academy_name = academy_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public float getUser_score() {
        return user_score;
    }

    public void setUser_score(float user_score) {
        this.user_score = user_score;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }


}







