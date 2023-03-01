package com.example.hakone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
public class MyPage extends AppCompatActivity {

    private ListView listView;
    BottomNavigationView bottomNavigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        listView = (ListView)findViewById(R.id.mypage_list);

        List<String> data = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);


        data.add("          개인정보 수정");
        data.add("          내가 작성한 리뷰");
        data.add("          로그아웃");
        data.add("          회원탈퇴");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position==0)
                {
                    Intent intent = new Intent(MyPage.this, ChangeProfile.class);
                    startActivity(intent);
                }
                if (position==1)
                {
                    Intent intent = new Intent(MyPage.this, MyReview.class);
                    startActivity(intent);
                }
                if (position==2)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyPage.this);
                    builder.setTitle("로그아웃");
                    builder.setMessage("정말 로그아웃 하시겠습니까?");
                    builder.setPositiveButton("예", null);
                    builder.setNegativeButton("취소", null);
                    builder.create().show();
                }
            }
        });

        adapter.notifyDataSetChanged();


        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setSelectedItemId(R.id.action_my);

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
                        return true;
                }
                return false;
            }
        });
    }
}