package com.example.hakone;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient extends AppCompatActivity {

    public static String BASE_URL = "http://ec2-52-79-55-255.ap-northeast-2.compute.amazonaws.com:8080/";

    private static Retrofit retrofit;
    public static Retrofit getClient(){

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) //이 converter을 사용해서 데이터 parsing할 예정.
                    .build();
        }
        return retrofit;
    }}




