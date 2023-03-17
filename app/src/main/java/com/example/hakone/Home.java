package com.example.hakone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    Login login = new Login();

    long userId = login.user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //새 스레드로
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<List<HakOneList>> call = apiInterface.getData(userId);

                try {
                    Response<List<HakOneList>> response = call.execute();

                    if (response.isSuccessful()) {

                        // InputStream 얻기
                        ResponseBody responseBody = response.raw().body();
                        InputStream inputStream = responseBody.byteStream();

                        // 버퍼 생성
                        byte[] buffer = new byte[inputStream.available()];
                        int bytesRead;
                        StringBuilder sb = new StringBuilder();

                        // 버퍼를 사용하여 데이터를 읽기
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            sb.append(new String(buffer, 0, bytesRead));
                        }

                        // JSON 데이터 파싱
                        String json = sb.toString();
                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            // 필요한 데이터 추출
                            int academyId = jsonObject.getInt("academyId");
                            String academyName = jsonObject.getString("academyName");
                            String region = jsonObject.getString("region");
                            String tel = jsonObject.getString("tel");
                            int avgTuition = jsonObject.getInt("avgTuition");

                            // gradeList 파싱
                            JSONArray gradeListArray = jsonObject.getJSONArray("gradeList");
                            boolean elementary = gradeListArray.getBoolean(0);
                            boolean middle = gradeListArray.getBoolean(1);
                            boolean high = gradeListArray.getBoolean(2);
                            boolean grace_etc = gradeListArray.getBoolean(3);

                            // subjectList 파싱
                            JSONArray subjectListArray = jsonObject.getJSONArray("subjectList");
                            boolean korean = subjectListArray.getBoolean(0);
                            boolean english = subjectListArray.getBoolean(1);
                            boolean math = subjectListArray.getBoolean(2);
                            boolean social = subjectListArray.getBoolean(3);
                            boolean science = subjectListArray.getBoolean(4);
                            boolean foreign = subjectListArray.getBoolean(5);
                            boolean essay = subjectListArray.getBoolean(6);
                            boolean art = subjectListArray.getBoolean(7);
                            boolean sub_etc = subjectListArray.getBoolean(8);


                            boolean isStar = jsonObject.getBoolean("star");
                            // ...
                        }

                        // InputStream 닫기
                        inputStream.close();
                    } else {
                        // 실패 처리
                        Log.e("TAG", "에러 발생 메시지");
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
/*
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<HakOneList>> call = apiInterface.getData(userId);

        // 동기적으로 호출하기
        try {
            Response<List<HakOneList>> response = call.execute();

            if (response.isSuccessful()) {

                // InputStream 얻기
                ResponseBody responseBody = response.raw().body();
                InputStream inputStream = responseBody.byteStream();

                // 버퍼 생성
                byte[] buffer = new byte[inputStream.available()];
                int bytesRead;
                StringBuilder sb = new StringBuilder();

                // 버퍼를 사용하여 데이터를 읽기
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    sb.append(new String(buffer, 0, bytesRead));
                }

                // JSON 데이터 파싱
                String json = sb.toString();
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    // 필요한 데이터 추출
                    int academyId = jsonObject.getInt("academyId");
                    String academyName = jsonObject.getString("academyName");
                    String region = jsonObject.getString("region");
                    String tel = jsonObject.getString("tel");
                    int avgTuition = jsonObject.getInt("avgTuition");

                    // gradeList 파싱
                    JSONArray gradeListArray = jsonObject.getJSONArray("gradeList");
                    boolean elementary = gradeListArray.getBoolean(0);
                    boolean middle = gradeListArray.getBoolean(1);
                    boolean high = gradeListArray.getBoolean(2);
                    boolean grace_etc = gradeListArray.getBoolean(3);

                    // subjectList 파싱
                    JSONArray subjectListArray = jsonObject.getJSONArray("subjectList");
                    boolean korean = subjectListArray.getBoolean(0);
                    boolean english = subjectListArray.getBoolean(1);
                    boolean math = subjectListArray.getBoolean(2);
                    boolean social = subjectListArray.getBoolean(3);
                    boolean science = subjectListArray.getBoolean(4);
                    boolean foreign = subjectListArray.getBoolean(5);
                    boolean essay = subjectListArray.getBoolean(6);
                    boolean art = subjectListArray.getBoolean(7);
                    boolean sub_etc = subjectListArray.getBoolean(8);


                    boolean isStar = jsonObject.getBoolean("star");
                    // ...
                }

                // InputStream 닫기
                inputStream.close();
            } else {
                // 실패 처리
            }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


 */



        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setSelectedItemId(R.id.action_list);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_star:
                        startActivity(new Intent(getApplicationContext(), MyInterest.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.action_list:
                        return true;

                    case R.id.action_my:
                        startActivity(new Intent(getApplicationContext(), MyPage.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}

