package com.example.hakone;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class Hak {

    @SerializedName("academyName")
    private String academyName;
    @SerializedName("address")
    private String address;
    @SerializedName("tel")
    private String tel;
    @SerializedName("teacher")
    private int teacher;
    @SerializedName("subjectList")
    private List<Boolean> subjectList ;
    @SerializedName("avg_tuition")
    private int avg_tuition;
    @SerializedName("classList")
    private HashMap<String, Integer> classList ;
    @SerializedName("star")
    private Boolean star;
    @SerializedName("avg_score")
    private float avg_score;
    @SerializedName("review_count")
    private int review_count;

}
