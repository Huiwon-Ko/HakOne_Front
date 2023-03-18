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

    public void setAcademyName(String academyName) {
        this.academyName = academyName;
    }

    public int getAvgTuition() {
        return avgTuition;
    }

    public void setAvgTuition(int avgTuition) {
        this.avgTuition = avgTuition;
    }
}







