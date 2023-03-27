package com.example.hakone;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {


    @GET("/user/{user_id}/academy")
    Call<ResponseBody> getData(@Path("user_id") long user_id);

    @DELETE("/user/{user_id}")
    Call<Void> deleteUser(@Path("user_id") long user_id);

    @POST("/user/{user_id}/star/{academy_id}") //관심 등록
    Call<Void> postStarAcademy(@Path("user_id") long user_id, @Path("academy_id") long academyId);

    @DELETE("/user/{user_id}/star/{academy_id}") //관심 삭제
    Call<Void> deleteStarAcademy(@Path("user_id") long user_id, @Path("academy_id") long academyId);

    @GET("/user/{user_id}/star") //관심 전체 조회
    Call<ResponseBody> allStarAcademy(@Path("user_id") long user_id);

    @GET("/user/{user_id}/academy/{academy_id}") //학원 상세페이지
    Call<ResponseBody> getItem(@Path("user_id") long user_id, @Path("academy_id") long academy_id);

}
