package com.example.hakone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private SignInButton btn_google; //구글 로그인 버튼
    private GoogleApiClient googleApiClient; //구글 API 클라이언트 객체
    private static final int RED_SIGN_GOOGLE = 100; //구글 결과 코드

    GoogleSignInClient mGoogleSignInClient; // Google Sign In API와 호출할 구글 로그인 클라이언트
    private final int RC_SIGN_IN = 123;
    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) { //앱이 실행될 떄 처음 수행되는 곳

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_google = findViewById(R.id.btn_googleLogin);
        btn_google.setOnClickListener(this);

        // 로그인 버튼 클릭 시 기본적인 옵션 세팅.  유저의 ID와 기본적인 프로필 정보를 요청
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() //추가로 email 요청 (필요?)
                .build();

        // Build a GoogleSignInClient with the options specified by gso. -> 위의 옵션 사용해서 GoogleSignIn 객체 생성
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso); //객체 생성

        // 기존에 로그인 했던 계정을 확인
        //GoogleSignIn.getLastSignedInAccount가 null가 아닌 GoogleSignInAccount 객체를 반환하면 사용자가 이미 Google로 앱에 로그인한 것.
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(Login.this);

        // 로그인 되있는 경우 (토큰으로 로그인 처리)
        if (gsa != null && gsa.getId() != null) {
                //이 경우 로그인 성공
        }

    }

    //GoogleSignIn.getLastSignedInAccount 메서드를 사용하여 현재 로그인한 사용자의 프로필 정보를 요청
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName(); //
                String personFamilyName = acct.getFamilyName(); //
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                Log.d(TAG, "handleSignInResult:personName "+personName);
                Log.d(TAG, "handleSignInResult:personGivenName "+personGivenName);
                Log.d(TAG, "handleSignInResult:personEmail "+personEmail);
                Log.d(TAG, "handleSignInResult:personId "+personId);
                Log.d(TAG, "handleSignInResult:personFamilyName "+personFamilyName);
                Log.d(TAG, "handleSignInResult:personPhoto "+personPhoto);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }

    @Override  //활동의 onClick 메서드에서 getSignInIntent 메서드로 로그인 인텐트를 만들고 startActivityForResult로 인텐트를 시작하여 로그인 버튼 탭을 처리
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_googleLogin: //로그인 버튼
                signIn();
                break;
            /*
            case R.id.logoutBt: //로그아웃 버튼
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, task -> {
                            Log.d(TAG, "onClick:logout success ");
                            mGoogleSignInClient.revokeAccess()
                                    .addOnCompleteListener(this, task1 -> Log.d(TAG, "onClick:revokeAccess success "));

                        });
                break;
            */
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

}

