package com.example.hakone;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    /*
    @GET("/user/{user_id}/academy")
    Call<List<HakOneList>> getData(@Path("user_id") long user_id);

     */
    @GET("/academy")
    Call<ResponseBody> getData();

    @DELETE("/user/{user_id}")
    Call<Void> deleteUser(@Path("user_id") long user_id);

}
