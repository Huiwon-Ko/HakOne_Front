package com.example.hakone;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class HakOneList {

    private String academyName;
    private int avgTuition;

    public HakOneList(String academyName, int avgTuition) {
        this.academyName = academyName;
        this.avgTuition = avgTuition;
    }

    public String getAcademyName() {
        return academyName;
    }

    public int getAvgTuition() {
        return avgTuition;
    }
}


    /*
    @SerializedName("academyId")
    public long academyId;

    @SerializedName("academyName")
    public String academyName;

    @SerializedName("region")
    public String region;

    @SerializedName("tel")
    public String tel;

    @SerializedName("avgTuition")
    public int avgTuition;

    @SerializedName("gradeList")
    public List<Boolean> gradeList;

    @SerializedName("subjectList")
    public List<Boolean> subjectList;

    @SerializedName("star")
    public boolean star;


    public Boolean elementary = gradeList.get(0); //초등
    public Boolean middle = gradeList.get(1); //중등
    public Boolean high = gradeList.get(2); //고등
    public Boolean grade_etc = gradeList.get(3); //기타

    public Boolean korean = subjectList.get(0); //국어
    public Boolean english = subjectList.get(1); //영어
    public Boolean math = subjectList.get(2); //수학
    public Boolean social = subjectList.get(3); //사회
    public Boolean science = subjectList.get(4); //과학
    public Boolean foreign = subjectList.get(5); //외국어
    public Boolean essay = subjectList.get(6); //논술
    public Boolean art = subjectList.get(7); //예능
    public Boolean sub_etc = subjectList.get(8); //기타

    public String getAcademyName(){
        return academyName;
    }

    public int getAvgTuition(){
        return avgTuition;
    }

     */





