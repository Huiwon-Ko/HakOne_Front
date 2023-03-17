package com.example.hakone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyPage extends AppCompatActivity {

    private ListView listView;
    BottomNavigationView bottomNavigationView;

    Button logoutBt;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gOptions;
    GoogleSignInClient gClient;

    Login login = new Login();

    long userId = login.user_id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        listView = (ListView)findViewById(R.id.mypage_list);

        List<String> data = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);


        logoutBt = findViewById(R.id.logoutBt);

        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);

        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);


        logoutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        startActivity(new Intent(MyPage.this, Login.class));

                    }
                });
            }
        });


        data.add("          개인정보 수정");
        data.add("          내가 작성한 리뷰");
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
                    builder.setTitle("회원탈퇴");
                    builder.setMessage("정말 회원탈퇴 하시겠습니까?");

                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int which){
                            Call<Void> call = ApiClient.getClient().create(ApiInterface.class).deleteUser(userId);

                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    finish();
                                    startActivity(new Intent(MyPage.this, Login.class));
                                    Toast.makeText(MyPage.this, "탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });
                        }
                    });


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