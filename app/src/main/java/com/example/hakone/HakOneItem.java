package com.example.hakone;

import static com.example.hakone.ApiClient.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//mport okhttp3.Address;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HakOneItem extends AppCompatActivity {

    public String className;
    public int tuition;
    public long academyId;
    public String academyName;
    public float avg_score;
    public int review_count;
    public List<Boolean> subjectList;

    private MapView mapView;
    private static NaverMap naverMap;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build();
    private ApiInterface apiInterface = retrofit.create(ApiInterface.class);

    BottomNavigationView bottomNavigationView;



    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hakoneitem);

        NaverMapSdk.getInstance(getApplicationContext()).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("msuzzar6hb")
        );

        /*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();

         */
        apiInterface = retrofit.create(ApiInterface.class);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        long user_id = sharedPreferences.getLong("user_id", 0);
        long academyId = sharedPreferences.getLong("academyId", 0);
        boolean isStar = sharedPreferences.getBoolean("isStar", false);

        Log.d("TAG", "hakOneItem user_id"+ user_id);
        Log.d("TAG", "hakOneItem academyId"+ academyId);

        new Thread(new Runnable() {
            @Override
            public void run() {

                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<ResponseBody> call = apiInterface.getItem(user_id, academyId);

                try {
                    Response<ResponseBody> response = call.execute();

                    if (response.isSuccessful()) {

                        // JSON 데이터 파싱
                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            String json = responseBody.string();
                            JSONObject jsonObject = new JSONObject(json);
                            //JSONArray jsonArray = new JSONArray(json);

                            //HashMap<String, Integer> classList = new HashMap<>();
                            ArrayList<String> itemsubjects = new ArrayList<>();


                                // 필요한 데이터 추출
                            String academyName = jsonObject.getString("academyName");
                            String address = jsonObject.getString("address");
                            String tel = jsonObject.getString("tel");
                            int teacher = jsonObject.getInt("teacher");
                            Log.d("TAG", academyName.toString());

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


                            Log.d("TAG", itemsubjects.toString());

                            int avg_tuition = jsonObject.getInt("avg_tuition");


                            //JSONArray classList = jsonObject.getJSONArray("classList");
                            JSONObject classList = jsonObject.getJSONObject("classList");

                            ArrayList<String> classNames = new ArrayList<>();
                            ArrayList<Integer> tuitions = new ArrayList<>();

                            Iterator<String> keys = classList.keys();
                            while (keys.hasNext()) {
                                String className = keys.next();
                                int tuition = classList.getInt(className);

                                classNames.add(className);
                                tuitions.add(tuition);
                            }




                            Log.d("TAG", "classList"+classList);
                            Log.d("TAG", "className"+classNames);
                            Log.d("TAG", "tuition"+tuitions);


                            boolean star = jsonObject.getBoolean("star");
                            float avg_score = (float) jsonObject.getDouble("avg_score");
                            int review_count = jsonObject.getInt("review_count");



                            //Log.d("TAG","보여주려는 academyName"+ academyName);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView academyNameTextView = findViewById(R.id.academyName);
                                    TextView avg_scoreTextView = findViewById(R.id.avg_score);
                                    TextView review_countTextView = findViewById(R.id.review_count);
                                    TextView subjectListTextView = findViewById(R.id.subjectList);
                                    TextView telTextView = findViewById(R.id.tel);
                                    TextView addressTextView = findViewById(R.id.address);
                                    TextView teacherTextView = findViewById(R.id.teacher);
                                    TextView avgTuitionTextView = findViewById(R.id.avg_tuition);

                                    academyNameTextView.setText(academyName);
                                    avg_scoreTextView.setText(String.valueOf(avg_score));
                                    review_countTextView.setText(String.valueOf(review_count));
                                    subjectListTextView.setText(" 과목  " + String.valueOf(itemsubjects));
                                    telTextView.setText(" 전화번호  " +String.valueOf(tel));
                                    addressTextView.setText(" 주소  " +String.valueOf(address));
                                    teacherTextView.setText(" 선생님 수  " +String.valueOf(teacher));
                                    avgTuitionTextView.setText(" 월 평균 수강료  " +String.valueOf(avg_tuition) + "원");


                                    // UI 작업 수행
                                    TableLayout tableLayout = findViewById(R.id.tablelayout);
                                    for (int i = 0; i < classNames.size(); i++) {
                                        String className = classNames.get(i);
                                        int tuition = tuitions.get(i);

                                        TableRow tableRow = new TableRow(getApplicationContext()); // 새 TableRow 객체 생성
                                        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)); // TableRow의 LayoutParams 설정

                                        TextView classNameTextView = new TextView(getApplicationContext()); // 반 이름을 보여줄 TextView 생성
                                        classNameTextView.setText("  " + className); // TextView에 반 이름 설정
                                        classNameTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f)); // TextView의 LayoutParams 설정
                                        tableRow.addView(classNameTextView); // TableRow에 TextView 추가

                                        TextView tuitionTextView = new TextView(getApplicationContext()); // 수강료를 보여줄 TextView 생성
                                        tuitionTextView.setText("  "+ String.valueOf(tuition) + "원"); // TextView에 수강료 설정
                                        tuitionTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f)); // TextView의 LayoutParams 설정
                                        tableRow.addView(tuitionTextView); // TableRow에 TextView 추가

                                        tableLayout.addView(tableRow); // TableLayout에 TableRow 추가
                                    }


                                    try {
                                        Geocoder geocoder = new Geocoder(getApplicationContext());
                                        List<Address> addressList = geocoder.getFromLocationName(address, 1);
                                        if (addressList.size() > 0) {
                                            Address location = addressList.get(0);
                                            double latitude = location.getLatitude();
                                            double longitude = location.getLongitude();
                                            Log.d("Tag", "geocoder" + latitude);
                                            Log.d("Tag", "geocoder" + longitude);
                                            // 네이버 지도 API를 사용하여 마커를 추가하는 코드

                                            MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
                                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                                @Override
                                                public void onMapReady(@NonNull NaverMap naverMap) {
                                                    LatLng latLng = new LatLng(latitude, longitude);
                                                    Marker marker = new Marker();
                                                    marker.setPosition(latLng);
                                                    marker.setMap(naverMap);
                                                    naverMap.moveCamera(CameraUpdate.scrollTo(latLng).animate(CameraAnimation.Fly, 3000));
                                                }
                                            });
                                        } else {
                                            // 주소를 찾을 수 없는 경우에 대한 예외 처리를 합니다.
                                        }
                                    } catch (IOException e) {

                                        System.out.println (e.toString());

                                    }
                                }
                            });


                        }

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



        Button Review = (Button) findViewById(R.id.Review);
        Review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReviewList.class);
                startActivity(intent);
            }
        });



        Button Interest = (Button) findViewById(R.id.Interest);
        Interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //int position = getAdapterPosition();
                //HakOneList hakone = hakOneList.get(position);
                //long academyId = hakone.getAcademyId();

                if (isStar){
                    // 등록 취소
                    Call<Void> call = apiInterface.deleteStarAcademy(user_id, academyId);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.d("TAG", "Star academyId" + academyId);
                            // 서버에 delete 요청이 성공한 경우
                            //Interest.setImageResource(R.drawable.ic_baseline_star_outline_24);
                            Drawable newDrawable = getResources().getDrawable(R.drawable.ic_baseline_star_outline_24);
                            Interest.setCompoundDrawablesWithIntrinsicBounds(null, null, newDrawable, null);
                            Log.d("Tag", "관심 취소 성공");
                            Log.d("Tag", Long.toString(user_id));
                            Log.d("Tag", Long.toString(academyId));
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            // 서버에 delete 요청이 실패한 경우
                            Toast.makeText(getApplicationContext(), "관심 취소 실패", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "관심 취소 실패", t);
                            Log.d("Tag", Long.toString(user_id));
                            Log.d("Tag", Long.toString(academyId));
                        }
                    });

                } else {
                    // 관심 등록을 하지 않은 경우

                    Call<ResponseBody> call = apiInterface.postStarAcademy(user_id, academyId);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d("TAG", "Star academyId" + academyId);
                            // 서버에 post 요청이 성공한 경우
                            //hakone.setStar(true);
                            //Interest.setImageResource(R.drawable.baseline_star_24);
                            Drawable newDrawable = getResources().getDrawable(R.drawable.baseline_star_24);
                            Interest.setCompoundDrawablesWithIntrinsicBounds(null, null, newDrawable, null);
                            Log.d("Tag","관심 등록 성공");
                            Log.d("Tag", Long.toString(user_id));
                            Log.d("Tag", Long.toString(academyId));
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            // 서버에 post 요청이 실패한 경우
                            Toast.makeText(getApplicationContext(), "관심 등록 실패", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "관심 등록 실패", t);
                            Log.d("Tag", Long.toString(user_id));
                            Log.d("Tag", Long.toString(academyId));
                        }
                    });
                }

            }
        });
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


}
