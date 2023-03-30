package com.example.hakone;

import static com.example.hakone.ApiClient.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewList extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    /*
    long review_id = jsonObject.getLong("review_id");
                                String profile_pic = jsonObject.getString("profile_pic");
                                String username = jsonObject.getString("username");
                                float score = (float) jsonObject.getDouble("score");
                                String created_date = jsonObject.getString("created_date");
                                String content = jsonObject.getString("content");
     */

    public long review_id;
    public String profile_pic;
    public String username;
    public float score;
    public String created_date;
    public String content;
    RecyclerView recyclerView; // 리사이클러뷰 객체 선언
    ReviewListAdapter adapter;

    List<ReviewListContent> reviewListContent = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_list);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        long user_id = sharedPreferences.getLong("user_id", 0);
        long academyId = sharedPreferences.getLong("academyId", 0);

        Log.d("TAG", "ReviewList 받아온 결과 academyId:" + academyId);

        recyclerView = findViewById(R.id.ReviewRecyclerView);
        reviewListContent = new ArrayList<>();


        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<ResponseBody> call = apiInterface.getReview(academyId);

                try {
                    Response<ResponseBody> response = call.execute();
                    Log.d("TAG", "ReviewList 받아온 결과 response:" + response);

                    if (response.isSuccessful()){

                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            String json = responseBody.string();
                            //JSONObject jsonObject = new JSONObject(json);

                            Log.d("TAG", "ReviewList 받아온 결과 json:" + json);

                            JSONArray jsonArray = new JSONArray(json);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                long review_id = jsonObject.getLong("review_id");
                                String profile_pic = jsonObject.getString("profile_pic");
                                String username = jsonObject.getString("username");
                                float score = (float) jsonObject.getDouble("score");
                                String created_date = jsonObject.getString("created_date");
                                String content = jsonObject.getString("content");

                                Log.d("TAG", "ReviewList 받아온 결과 review_id1:" + review_id);

                                ReviewListContent hakOne = new ReviewListContent(review_id, profile_pic, username,score,created_date, content);
                                reviewListContent.add(hakOne);

                                Log.d("TAG", "ReviewList 받아온 결과 review_id2:" + review_id);
                                Log.d("TAG", "ReviewList 받아온 결과 profile_pic:" + profile_pic);
                            }

                        }
                        Log.d("Tag", "ReviewList reviewListContent" + reviewListContent.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    } else {
                        // 실패 처리
                        Log.e("TAG", "에러 발생 메시지");
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.e("TAG", "서버 연결 오류: " + e.getMessage());
                }
            }

        }).start();



        // 리사이클러뷰 레이아웃 매니저 설정
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ReviewListAdapter(reviewListContent, this, review_id, profile_pic, username, score, created_date, content);
        recyclerView.setAdapter(adapter);



        ImageButton review_write = (ImageButton) findViewById(R.id.review_write);
        review_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WriteReview.class);
                startActivity(intent);
            }
        });
    }


}
