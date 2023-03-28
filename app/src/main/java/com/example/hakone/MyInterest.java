package com.example.hakone;

import static android.content.Context.MODE_PRIVATE;
//import static com.example.hakone.Home.subjects;

import static com.example.hakone.Home.subjects;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class MyInterest extends AppCompatActivity implements RecyclerViewInterface{

    private RecyclerView recyclerView;
    private MyInterestAdapter adapter;
    private List<HakOneList> hakOneItemLists = new ArrayList<>();

    BottomNavigationView bottomNavigationView;
    List<HakOneList> hakOneList1 = Home.hakOneList1;

    public long academyId;

    public long user_id;
    public static ArrayList<String> subjects;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        long user_id = sharedPreferences.getLong("user_id", 0);
        long academyId = sharedPreferences.getLong("academyId", 0);
        boolean isStar = sharedPreferences.getBoolean("isStar", false);

        Log.d("Tag", "MyInterest hakOneList1" + hakOneList1);
        for (HakOneList hakOne : hakOneList1) {
            Log.d("Tag", "MyInterest name: " + hakOne.getAcademyName() + ", MyInterest avgTuition: " + hakOne.getAvgTuition());
        }

        setContentView(R.layout.activity_myinterest);

        recyclerView = findViewById(R.id.InterestRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyInterestAdapter(hakOneList1, this, subjects, this, user_id, academyId, isStar);
        recyclerView.setAdapter(adapter);



        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setSelectedItemId(R.id.action_star);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_star:
                        return true;

                    case R.id.action_list:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0,0);
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

    @Override
    public void onItemClick(int position) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getItem(user_id, academyId);

        try {
            Response<ResponseBody> response = call.execute();

            if (response.isSuccessful()) {

                // JSON 데이터 파싱
                String json = response.toString();
                JSONArray jsonArray = new JSONArray(json);
                HashMap<String, Integer> classList = new HashMap<>();
                ArrayList<String> itemsubjects = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    // 필요한 데이터 추출
                    String academyName = jsonObject.getString("academyName");
                    String address = jsonObject.getString("address");
                    String tel = jsonObject.getString("tel");
                    int teacher = jsonObject.getInt("teacher");

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

                    if (subjectListArray.getBoolean(0)) itemsubjects.add("국어");
                    if (subjectListArray.getBoolean(1)) itemsubjects.add("영어");
                    if (subjectListArray.getBoolean(2)) itemsubjects.add("수학");
                    if (subjectListArray.getBoolean(3)) itemsubjects.add("사회");
                    if (subjectListArray.getBoolean(4)) itemsubjects.add("과학");
                    if (subjectListArray.getBoolean(5)) itemsubjects.add("외국어");
                    if (subjectListArray.getBoolean(6)) itemsubjects.add("논술");
                    if (subjectListArray.getBoolean(7)) itemsubjects.add("예능");
                    if (subjectListArray.getBoolean(8)) itemsubjects.add("기타");

                    int avgTuition = jsonObject.getInt("avgTuition");

                    String className = jsonObject.getString("className");
                    int tuition = jsonObject.getInt("tuition");
                    classList.put(className, tuition);


                    boolean star = jsonObject.getBoolean("star");
                    Log.d("TAG", subjects.toString());
                    float avg_score = (float) jsonObject.getDouble("avg_score");
                    int review_count = jsonObject.getInt("review_count");

                }

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
}