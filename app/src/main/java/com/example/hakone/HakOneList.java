package com.example.hakone;

import static com.example.hakone.Home.subjects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class HakOneList {

    private String academyName;
    private int avgTuition;
    private List<String> subjects;

    private String region;
    private boolean korean;
    private boolean english;
    private boolean math;
    private boolean social;
    private boolean science;
    private boolean foreign;
    private boolean essay;
    private boolean art;
    private boolean sub_etc;


    private boolean elementary;
    private boolean middle;
    private boolean high;
    private boolean grade_etc;

    private boolean isStar;
    private long academyId;

    private HashMap<String, Integer> classList;


    public HakOneList(String academyName, int avgTuition, String region, List<String> subjects,
                      boolean korean, boolean english, boolean math, boolean social, boolean science,
                      boolean foreign, boolean essay, boolean art, boolean sub_etc,
                      boolean elementary, boolean middle, boolean high, boolean grade_etc, boolean isStar, long academyId) {
        this.academyName = academyName;
        this.avgTuition = avgTuition;
        this.region = region;
        this.subjects = subjects;

        //세부 과목
        this.korean = korean;
        this.english = english;
        this.math = math;
        this.social = social;
        this.science = science;
        this.foreign = foreign;
        this.essay = essay;
        this.art = art;
        this.sub_etc = sub_etc;

        //세부 학년
        this.elementary = elementary;
        this.middle = middle;
        this.high = high;
        this.grade_etc = grade_etc;

        this.isStar = isStar;
        this.academyId = academyId;

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


    //세부 과목
    //국어
    public boolean isKorean() {
        return korean;
    }

    public void setKorean(boolean korean) {
        this.korean = korean;
    }

    //영어
    public boolean isEnglish () {
        return english;
    }

    public void setEnglish(boolean english) {
        this.english  = english;
    }

    //수학
    public boolean isMath() {
        return math;
    }

    public void setMath(boolean math) {
        this.math = math;
    }

    //사회
    public boolean isSocial() {
        return social;
    }

    public void setSocial(boolean social) {
        this.social = social;
    }

    //과학
    public boolean isScience() {
        return science;
    }

    public void setScience(boolean science) {
        this.science = science;
    }

    //외국어
    public boolean isForeign() {
        return foreign;
    }

    public void setForeign(boolean foreign) {
        this.foreign= foreign;
    }

    //논술
    public boolean isEssay() {
        return essay;
    }

    public void setEssay(boolean essay) {
        this.essay = essay;
    }

    //예능
    public boolean isArt() {
        return art;
    }

    public void setArt(boolean art) {
        this.art = art;
    }

    //기타
    public boolean isSub_etc() {
        return sub_etc;
    }

    public void setSub_etc(boolean sub_etc) {
        this.sub_etc = sub_etc;
    }



    //세부 학년
    //초등
    public boolean isElementary() {
        return elementary;
    }

    public void setElementary(boolean elementary) {
        this.elementary = elementary;
    }

    //중등
    public boolean isMiddle() {
        return middle;
    }

    public void setMiddle(boolean middle) {
        this.middle = middle;
    }

    //고등
    public boolean isHigh() {
        return high;
    }

    public void setHigh(boolean high) {
        this.high = high;
    }

    //기타
    public boolean isGrade_etc() {
        return grade_etc;
    }

    public void setGrade_etc(boolean grade_etc) {
        this.grade_etc = grade_etc;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean isStar) {
        this.isStar = isStar;
    }

    public long getAcademyId(){
        return academyId;
    }
    public void setAcademyId(long academyId) {
        this.academyId = academyId;
    }

    public HashMap<String, Integer> getClassList(){
        return classList;
    }
    public void setClassList(HashMap<String, Integer> classList) {
        this.classList = classList;
    }

}







