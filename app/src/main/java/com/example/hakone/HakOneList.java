package com.example.hakone;

import static com.example.hakone.Home.subjects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class HakOneList {

    private String academyName;
    private int avgTuition;
    private List<String> subjects;

    private String region;

    public HakOneList(String academyName, int avgTuition, String region, List<String> subjects) {
        this.academyName = academyName;
        this.avgTuition = avgTuition;
        this.region = region;
        this.subjects = subjects;
    }

    public String getAcademyName() {
        return academyName;
    }

    public void setAcademyName(String academyName) {
        this.academyName = academyName;
    }

    public int getAvgTuition() {
        return avgTuition;
    }

    public void setAvgTuition(int avgTuition) {
        this.avgTuition = avgTuition;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

}







