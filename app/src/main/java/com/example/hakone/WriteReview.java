package com.example.hakone;

//import static com.naver.maps.map.g.a.t;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class WriteReview extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    float rate;
    private AlertDialog dialog;
    String reviewContent;

    EditText mEditTextreview, mEditTexttitle;

    private ImageView userImage;

    private Uri imageUri;
    private MultipartBody.Part userImagePart;
    private RequestBody rateBody;
    private RequestBody reviewContentBody;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        long user_id = sharedPreferences.getLong("user_id", 0);
        long academyId = sharedPreferences.getLong("academyId", 0);

        Log.d("TAG", "write user_id" + user_id);
        Log.d("TAG", "write academyId" + academyId);


        Button btnUploadPicture = findViewById(R.id.btn_UploadPicture);
        RatingBar reviewRating=(RatingBar)findViewById(R.id.reviewRating);
        EditText reviewEdit=(EditText) findViewById(R.id.reviewEdit);
        mEditTextreview = (EditText) findViewById(R.id.reviewEdit);
        Button cancelButton=(Button) findViewById(R.id.cancelButton);
        Button okButton=(Button)findViewById(R.id.okButton);



        btnUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            }

        });

        //취소버튼 클릭 시
        cancelButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( WriteReview.this,ReviewList.class );
                startActivity( intent );
            }
        });

        reviewRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = rating;
                Log.d("TAG", "write rate" + rate);
            }



        });

        //확인 버튼 클릭 시
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder  = new AlertDialog.Builder(WriteReview.this);
                reviewContent = mEditTextreview.getText().toString();
                Log.d("TAG", "write 전체 userImage" +  userImage);
                Log.d("TAG", "write 전체 rate" +  rate);
                Log.d("TAG", "write 전체 reviewContent" +  reviewContent);

                // rate
                rateBody = RequestBody.create(MediaType.parse("multipart/form-data"), Float.toString(rate));

                // reviewContent
                reviewContentBody = RequestBody.create(MediaType.parse("multipart/form-data"), reviewContent);

                if (userImagePart != null && rateBody != null && reviewContentBody != null) {
                    MultipartBody.Builder builder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addPart(userImagePart)
                            .addPart(rateBody)
                            .addPart(reviewContentBody);

                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<RequestBody> call = apiInterface.postReview(userImagePart, rateBody, reviewContentBody, user_id, academyId);
                    call.enqueue(new Callback<RequestBody>() {
                        @Override
                        public void onResponse(Call<RequestBody> call, Response<RequestBody> response){
                            Log.d("TAG", "성공" + response.code());
                            if (response.code() == 409) {
                                // response가 409일 경우에만 Toast 메시지 출력
                                Toast.makeText(WriteReview.this, "이미 리뷰를 등록한 회원입니다", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(WriteReview.this)
                                        .setTitle("리뷰 작성 완료")
                                        .setMessage("리뷰작성이 완료되었습니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });

                                dialog = builder.create();
                                dialog.show();

                                Toast.makeText(WriteReview.this, "등록되었습니다", Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "성공");
                                Log.d("TAG", "성공" + response);

                            }
                        }
                        @Override
                        public void onFailure(Call<RequestBody> call, Throwable t) {
                            // 서버에 전달 중 오류가 발생했을 때의 처리
                            Log.d("TAG", "실패" + t);

                        }
                    });


                    /*
                    dialog = new AlertDialog.Builder(WriteReview.this)
                            .setTitle("리뷰 작성 완료")
                            .setMessage("리뷰작성이 완료되었습니다.")
                            .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                                    Call<RequestBody> call = apiInterface.postReview(userImagePart, rateBody, reviewContentBody, user_id, academyId);
                                    call.enqueue(new Callback<RequestBody>() {

                                        @Override
                                        public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {

                                            Log.d("TAG", "성공" + response.code());
                                            if (response.code() == 409) {
                                                // response가 409일 경우에만 Toast 메시지 출력
                                                Toast.makeText(WriteReview.this, "이미 리뷰를 등록한 회원입니다", Toast.LENGTH_SHORT).show();
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(WriteReview.this)
                                                        .setTitle("리뷰 작성 완료")
                                                        .setMessage("리뷰작성이 완료되었습니다.")
                                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                finish();
                                                            }
                                                        });

                                                dialog = builder.create();
                                                dialog.show();

                                                Toast.makeText(WriteReview.this, "등록되었습니다", Toast.LENGTH_SHORT).show();
                                                Log.d("TAG", "성공");
                                                Log.d("TAG", "성공" + response);


                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<RequestBody> call, Throwable t) {
                                            // 서버에 전달 중 오류가 발생했을 때의 처리
                                            Log.d("TAG", "실패" + t);

                                        }
                                    });
                                    finish();
                                }
                            })
                            .create();

                     */

                } else {
                    // userImagePart, rateBody, reviewContentBody 중 하나라도 null이면 AlertDialog를 보여주지 않고 Toast 메시지를 띄웁니다.
                    Toast.makeText(WriteReview.this, "이미지를 업로드해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            // 선택한 이미지를 ImageView에 설정
            userImage = findViewById(R.id.user_image);
            userImage.setImageURI(imageUri);
            Log.d("TAG", "write userImage" + userImage);

            String filePath = getRealPathFromURI(imageUri);
            File file = new File(filePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            userImagePart = MultipartBody.Part.createFormData("receipt", file.getName(), requestBody);
            Log.d("TAG", "성공"+file.getName());

        }

    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        cursor.close();
        return filePath;
    }

}

