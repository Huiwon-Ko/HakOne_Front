package com.example.hakone;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.annotations.SerializedName;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient extends AppCompatActivity {

    public static String BASE_URL = "http://172.30.1.72:8080/";

    private static Retrofit retrofit;
    public static Retrofit getClient(){

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //이 converter을 사용해서 데이터 parsing할 예정.
                    .build();
        }
        return retrofit;
    }
/*
    public class HakOneList{ //데이터 가져올 클래스
        @SerializedName("key")
        public string key; //이 과정 반복    } */
}



