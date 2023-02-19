package com.example.hakone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private MyPage myPage;
    private MainList list;
    private MyInterest myInterest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_list:
                        setFrag(0);
                        break;
                }
                switch (menuItem.getItemId()){
                    case R.id.action_my:
                        setFrag(1);
                        break;
                }
                switch (menuItem.getItemId()){
                    case R.id.action_star:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });

        myInterest = new MyInterest();
        myPage = new MyPage();
        list = new MainList();
        setFrag(0); //첫 프래그먼트 화면 지정



    }

    //프래그먼트 교체 이루어짐
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.main_frame, list);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, myPage);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, myInterest);
                ft.commit();
                break;
        }
    }
}

