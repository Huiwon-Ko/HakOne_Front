package com.example.hakone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Credentials;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        String serverClientId = getString(R.string.server_client_id); //추가한 부분
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId)
                .requestIdToken(getString(R.string.server_client_id)) //추가한 부분
                .requestEmail() //email 요청 (필요?) //리퀘스트
                .build();

        // Build a GoogleSignInClient with the options specified by gso. -> 위의 옵션 사용해서 GoogleSignIn 객체 생성
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso); //객체 생성

        // 기존에 로그인 했던 계정을 확인
        //GoogleSignIn.getLastSignedInAccount가 null가 아닌 GoogleSignInAccount 객체를 반환하면 사용자가 이미 Google로 앱에 로그인한 것.
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(Login.this);


        // 이미 로그인 되있는 경우
        if (gsa != null && gsa.getId() != null) {
                //이 경우 로그인 성공
            Toast.makeText(this, "이미 로그인 함", Toast.LENGTH_SHORT).show();

        }
    }

    //GoogleSignIn.getLastSignedInAccount 메서드를 사용하여 현재 로그인한 사용자의 프로필 정보를 요청
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
            String idToken = acct.getIdToken(); //추가한 부분


            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName(); //
                String personFamilyName = acct.getFamilyName(); //
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                Log.d(TAG, "handleSignInResult:personName " + personName);
                Log.d(TAG, "handleSignInResult:personGivenName " + personGivenName);
                Log.d(TAG, "handleSignInResult:personEmail " + personEmail);
                Log.d(TAG, "handleSignInResult:personId " + personId);
                Log.d(TAG, "handleSignInResult:personFamilyName " + personFamilyName);
                Log.d(TAG, "handleSignInResult:personPhoto " + personPhoto);


                // TODO(developer): send ID Token to server and validate

                //추가한 부분
                new Thread(() -> {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("https://yourbackend.example.com/tokensignin"); //추후 수정 필요

                    try {
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair("idToken", idToken));
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        HttpResponse response = httpClient.execute(httpPost);
                        int statusCode = response.getStatusLine().getStatusCode();
                        final String responseBody = EntityUtils.toString(response.getEntity());
                        Log.i(TAG, "Signed in as: " + responseBody);
                    } catch (ClientProtocolException e) {
                        Log.e(TAG, "Error sending ID token to backend.", e);
                    } catch (IOException e) {
                        Log.e(TAG, "Error sending ID token to backend.", e);
                    }
                }).start();
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());

            //updateUI(null);
        }
    }




    @Override  //활동의 onClick 메서드에서 getSignInIntent 메서드로 로그인 인텐트를 만들고 startActivityForResult로 인텐트를 시작하여 로그인 버튼 탭을 처리
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_googleLogin: //로그인 버튼
                signIn();
                break;

            case R.id.logoutBt: //로그아웃 버튼
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, task -> {
                            Log.d(TAG, "onClick:logout success ");
                            mGoogleSignInClient.revokeAccess()
                                    .addOnCompleteListener(this, task1 -> Log.d(TAG, "onClick:revokeAccess success "));
                                    //홈 화면으로 이동
                        });
                break;


        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //구글 로그인 인증을 요청 했을 때 결과 값을 되돌려 받는 곳
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            //여기서부터 추가한 부분. 사용자가 성공적으로 로그인하면 getServerAuthCode로 사용자의 인증 코드를 가져옴.
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String authCode = account.getServerAuthCode();

                // Show signed-un UI
                //updateUI(account);
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);

                //추가
                // TODO(developer): send code to server and exchange for access/refresh/ID tokens

                new Thread(() -> {
                    HttpPost httpPost = new HttpPost("http://localhost:8080/google/login");
                    HttpClient httpClient = new DefaultHttpClient();
                    try {
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("authCode", authCode));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


                            HttpResponse response = httpClient.execute(httpPost);


                            int statusCode = response.getStatusLine().getStatusCode();
                            final String responseBody = EntityUtils.toString(response.getEntity());


                    } catch (ClientProtocolException e) {
                        Log.e(TAG, "Error sending auth code to backend.", e);
                    } catch (IOException e) {
                        Log.e(TAG, "Error sending auth code to backend.", e);
                    }
                }).start();



            } catch (ApiException e) {
                Log.w(TAG, "Sign-in failed", e);
                //updateUI(null);
            }
            handleSignInResult(task);


        }
    }

}

