package com.example.hakone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

public class MyReview extends AppCompatActivity {

    RecyclerView recyclerView; // 리사이클러뷰 객체 선언
    MyReviewAdapter adapter;

    public long academy_id;
    public long review_id;

    public String academy_name;
    public String content;
    public float user_score;
    public String created_date;


    List<MyReviewContent> myReviewListContent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreview);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        long user_id = sharedPreferences.getLong("user_id", 0);
        //long academy_id = sharedPreferences.getLong("academy_id", 0);


        Log.d("TAG", "MyReview 받아온 결과 user_id:" + user_id);

        recyclerView = findViewById(R.id.MyReviewRecyclerView);
        myReviewListContent = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<ResponseBody> call = apiInterface.getMyReview(user_id);

                try {
                    Response<ResponseBody> response = call.execute();

                    if (response.isSuccessful()){

                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            String json = responseBody.string();
                            //JSONObject jsonObject = new JSONObject(json);

                            JSONArray jsonArray = new JSONArray(json);


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);


                                long academy_id = jsonObject.getLong("academy_id");
                                long review_id = jsonObject.getLong("review_id");
                                String academy_name = jsonObject.getString("academy_name");
                                String content = jsonObject.getString("content");
                                float user_score  = (float) jsonObject.getDouble("user_score");
                                String created_date = jsonObject.getString("created_date");

                                MyReviewContent hakOne = new MyReviewContent(academy_id, review_id, academy_name, content, user_score, created_date);
                                myReviewListContent.add(hakOne);

                                Log.d("TAG", "MyReview 받아온 결과 academy_id:" + academy_id);
                                Log.d("TAG", "MyReview 받아온 결과 review_id:" + review_id);
                                Log.d("TAG", "MyReview 받아온 결과 academy_name:" + academy_name);
                                Log.d("TAG", "MyReview 받아온 결과 content:" + content);

                            }

                        }
                        Log.d("Tag", "MyReview myReviewListContent" + myReviewListContent.toString());


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

        adapter = new MyReviewAdapter(myReviewListContent, this, academy_id, review_id, academy_name, content,user_score, created_date);
        recyclerView.setAdapter(adapter);


    }


}
