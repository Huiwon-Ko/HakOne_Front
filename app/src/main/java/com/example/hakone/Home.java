package com.example.hakone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Home extends AppCompatActivity implements RecyclerViewInterface{
    public static Home instance; //추가
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView; // 리사이클러뷰 객체 선언
    RecyclerAdapter adapter; // 어댑터 객체 선언
    List<HakOneList> hakOneList = new ArrayList<>(); // 데이터 리스트

    private Spinner regionSpinner;
    private Spinner subjectSpinner;
    private Spinner gradeSpinner;
    private Spinner sortSpinner;

    public long academyId;

    public long user_id;

    public boolean isStar;

    public static ArrayList<String> subjects;
    //private List<HakOneList> regionList; //지역 필터링 된 것들 넣어줄 것.

    // 검색시 같은 이름이 있는 아이템이 담길 리스트
    ArrayList<HakOneList> search_list = new ArrayList<>();
    // recyclerView에 추가할 아이템 리스트
    ArrayList<HakOneList> original_list = new ArrayList<>();
    EditText editText;

    public Home(){
        instance = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        long user_id = sharedPreferences.getLong("user_id", 0);

        //long user_id = getIntent().getLongExtra("user_id", 0);

        recyclerView = findViewById(R.id.recyclerView); // 리사이클러뷰 뷰 ID 연결

        hakOneList = new ArrayList<>(); //hakOneList 객체 초기화

        EditText editText = findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });


        //지역 필터링
        regionSpinner = (Spinner)findViewById(R.id.regionSpinner);

        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<HakOneList> regionList = new ArrayList<>();
                String selectedItem = regionSpinner.getSelectedItem().toString();
                Log.d("TAG", selectedItem);

                if (selectedItem.equals("지역")) { //전체 선택 시 전체 데이터 보여주기
                    adapter.regionSelected(hakOneList);
                } else {
                    for (HakOneList region : hakOneList) {
                        if (region.getRegion().equals(selectedItem)) {
                            regionList.add(region);
                        }
                    }
                    adapter.regionSelected(regionList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapter.regionSelected(hakOneList);
            }
        });


        //과목 필터링
        subjectSpinner = (Spinner)findViewById(R.id.subjectSpinner);

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<HakOneList> subjectList = new ArrayList<>();
                String selectedItem = subjectSpinner.getSelectedItem().toString();
                Log.d("TAG", selectedItem);

                if (selectedItem.equals("과목")) { //전체 선택 시 전체 데이터 보여주기
                    adapter.subjectSelected(hakOneList);
                } else {
                    for (HakOneList subject : hakOneList) {
                        if (selectedItem.equals("국어") && subject.isKorean()) {
                            subjectList.add(subject);

                        } else if (selectedItem.equals("영어") && subject.isEnglish()) {
                            subjectList.add(subject);

                        } else if (selectedItem.equals("수학") && subject.isMath()) {
                            subjectList.add(subject);

                        } else if (selectedItem.equals("사회") && subject.isSocial()) {
                            subjectList.add(subject);

                        } else if (selectedItem.equals("과학") && subject.isScience()) {
                            subjectList.add(subject);

                        } else if (selectedItem.equals("외국어") && subject.isForeign()) {
                            subjectList.add(subject);

                        } else if (selectedItem.equals("논술") && subject.isEssay()) {
                            subjectList.add(subject);

                        } else if (selectedItem.equals("예능") && subject.isArt()) {
                            subjectList.add(subject);

                        } else if (selectedItem.equals("기타") && subject.isSub_etc()) {
                            subjectList.add(subject);
                        }
                    }adapter.subjectSelected(subjectList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapter.subjectSelected(hakOneList);
            }
        });



        //학년 필터링
        gradeSpinner = (Spinner)findViewById(R.id.gradeSpinner);

        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<HakOneList> gradeList = new ArrayList<>();
                String selectedItem = gradeSpinner.getSelectedItem().toString();
                Log.d("TAG", selectedItem);

                if (selectedItem.equals("학년")) { //전체 선택 시 전체 데이터 보여주기
                    adapter.gradeSelected(hakOneList);
                } else {
                    for (HakOneList grade : hakOneList) {
                        if (selectedItem.equals("초등") && grade.isElementary()) {
                            gradeList.add(grade);

                        } else if (selectedItem.equals("중등") && grade.isMiddle()) {
                            gradeList.add(grade);

                        } else if (selectedItem.equals("고등") && grade.isHigh()) {
                            gradeList.add(grade);

                        } else if (selectedItem.equals("기타") && grade.isGrade_etc()) {
                            gradeList.add(grade);
                        }
                    }adapter.gradeSelected(gradeList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapter.gradeSelected(hakOneList);
            }
        });


        //가격 정렬
        sortSpinner = (Spinner)findViewById(R.id.sortSpinner);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<HakOneList> sortList = new ArrayList<>();
                String selectedItem = sortSpinner.getSelectedItem().toString();
                Log.d("TAG", selectedItem);

                if (selectedItem.equals("정렬")) { //전체 선택 시 전체 데이터 보여주기
                    adapter.sortSelected(hakOneList);
                } else {
                    if (selectedItem.equals("최저")) {
                        Collections.sort(hakOneList, new Comparator<HakOneList>() {
                            @Override
                            public int compare(HakOneList o1, HakOneList o2) {
                                return Double.compare(o1.getAvgTuition(), o2.getAvgTuition());
                            }
                        });
                        sortList.addAll(hakOneList);

                    } else if (selectedItem.equals("최고")) {
                        Collections.sort(hakOneList, new Comparator<HakOneList>() {
                            @Override
                            public int compare(HakOneList o1, HakOneList o2) {
                                return Double.compare(o2.getAvgTuition(), o1.getAvgTuition());
                            }
                        });
                        sortList.addAll(hakOneList);
                    }
                    adapter.sortSelected(sortList);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapter.sortSelected(hakOneList);
            }
        });



        //새 스레드로
        new Thread(new Runnable() {
            @Override
            public void run() {
                // long user_id = getIntent().getLongExtra("user_id", 0);
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Log.d("TAG", "Home 받아온 결과 user_id:" +user_id);
                Call<ResponseBody> call = apiInterface.getData(user_id);


                try {
                    Response<ResponseBody> response = call.execute();


                    if (response.isSuccessful()) {

                        // InputStream 얻기
                        InputStream inputStream = response.body().byteStream();

                        Log.d("TAG", inputStream.toString());


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
                            subjects = new ArrayList<>();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // 필요한 데이터 추출
                            long academyId = jsonObject.getLong("academyId");
                            String academyName = jsonObject.getString("academyName");
                            String region = jsonObject.getString("region");
                            String tel = jsonObject.getString("tel");
                            int avgTuition = jsonObject.getInt("avgTuition");
                            //Log.d("TAG", "Home academyId" + academyId);

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

                            if (subjectListArray.getBoolean(0)) subjects.add("국어");
                            if (subjectListArray.getBoolean(1)) subjects.add("영어");
                            if (subjectListArray.getBoolean(2)) subjects.add("수학");
                            if (subjectListArray.getBoolean(3)) subjects.add("사회");
                            if (subjectListArray.getBoolean(4)) subjects.add("과학");
                            if (subjectListArray.getBoolean(5)) subjects.add("외국어");
                            if (subjectListArray.getBoolean(6)) subjects.add("논술");
                            if (subjectListArray.getBoolean(7)) subjects.add("예능");
                            if (subjectListArray.getBoolean(8)) subjects.add("기타");

                            //Log.d("TAG", subjects.toString());



                            boolean isStar = jsonObject.getBoolean("star");
                            float avg_score = (float) jsonObject.getDouble("avg_score");
                            //double avg_score = jsonObject.getDouble("avg_score");
                            //float avg_score = jsonObject.getInt("avg_score");
                            int review_count = jsonObject.getInt("review_count");

                            HakOneList hakOne = new HakOneList(academyName, avgTuition, region, subjects,
                                    korean, english, math, social, science, foreign, essay, art, sub_etc,
                                    elementary, middle, high, grace_etc, isStar, academyId);
                            hakOneList.add(hakOne);

                            // ...
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                        // InputStream 닫기
                        inputStream.close();
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


        // 어댑터 객체 생성 및 리사이클러뷰에 연결
        adapter = new RecyclerAdapter(hakOneList, this, subjects, this, user_id, academyId, isStar);
        recyclerView.setAdapter(adapter);


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


    private void filter(String text){
        ArrayList<HakOneList> filteredList = new ArrayList<>();

        for (HakOneList item : hakOneList) {
            if(item.getAcademyName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
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
                ArrayList<String> subjects = new ArrayList<>();

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

                    if (subjectListArray.getBoolean(0)) subjects.add("국어");
                    if (subjectListArray.getBoolean(1)) subjects.add("영어");
                    if (subjectListArray.getBoolean(2)) subjects.add("수학");
                    if (subjectListArray.getBoolean(3)) subjects.add("사회");
                    if (subjectListArray.getBoolean(4)) subjects.add("과학");
                    if (subjectListArray.getBoolean(5)) subjects.add("외국어");
                    if (subjectListArray.getBoolean(6)) subjects.add("논술");
                    if (subjectListArray.getBoolean(7)) subjects.add("예능");
                    if (subjectListArray.getBoolean(8)) subjects.add("기타");

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

