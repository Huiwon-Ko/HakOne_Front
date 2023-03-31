package com.example.hakone;

import static com.example.hakone.ApiClient.BASE_URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Path;

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.MyViewHolder>{
    private SharedPreferences sharedPreferences;
    private Context context;

    private final ApiInterface apiInterface;
    private List<MyReviewContent> myReviewListContent;
    private long review_id;
    private String created_date;
    private String content;
    private String academy_name;
    private float user_score;
    private long academy_id;



    public MyReviewAdapter(List<MyReviewContent> myReviewListContent, Context context, long academy_id, long review_id, String academy_name, String content, float user_score, String created_date){
        this.myReviewListContent = myReviewListContent;
        this.context = context;
        this.academy_id = academy_id;
        this.review_id = review_id;
        this.academy_name = academy_name;
        this.content = content;
        this.user_score = user_score;
        this.created_date = created_date;




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    @NonNull
    @Override
    public MyReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myreview_item, parent, false);
        MyReviewAdapter.MyViewHolder viewHolder = new MyReviewAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyReviewAdapter.MyViewHolder holder, int position) {
        MyReviewContent hakone = myReviewListContent.get(position);
        holder.bind(hakone);
    }

    @Override
    public int getItemCount() {if(myReviewListContent==null) return 0;
        return myReviewListContent.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView academyName;
        TextView mystar;
        TextView created_date;
        TextView review_content;
        Button delete;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            academyName = itemView.findViewById(R.id.academyName);
            mystar = itemView.findViewById(R.id.mystar);
            created_date = itemView.findViewById(R.id.created_date);
            review_content = itemView.findViewById(R.id.review_content);
            delete = itemView.findViewById(R.id.delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MyReviewContent hakone = myReviewListContent.get(getAdapterPosition());
                    review_id = hakone.getReview_id();
                    academy_id = hakone.getAcademy_id();

                    //sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    //long academy_id = sharedPreferences.getLong("academy_id", 0); //확인 필요
                    //long review_id = sharedPreferences.getLong("review_id", 0);

                    Log.d("Tag", "리뷰삭제 academyId"+academy_id);
                    Log.d("Tag", "리뷰삭제 review_id"+review_id);

                    Call<Void> call = apiInterface.deleteReview(academy_id, review_id);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.d("Tag", "리뷰 삭제 response" + response);
                            Toast.makeText(context, "해당 리뷰가 삭제되었습니다", Toast.LENGTH_SHORT).show();
                            Log.d("Tag", "리뷰 삭제 성공");
                            Log.d("Tag", Long.toString(review_id));
                            Log.d("Tag", Long.toString(academy_id));

                            ((Activity)context).finish(); // 현재 액티비티 종료
                            Intent intent = new Intent(context, MyReview.class); // 새로운 액티비티 열기
                            context.startActivity(intent);

                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            // 서버에 delete 요청이 실패한 경우
                            Log.e("TAG", "리뷰 삭제 실패", t);
                            Log.d("Tag", Long.toString(review_id));
                            Log.d("Tag", Long.toString(academy_id));
                        }
                    });

                }
            });
        }
        void bind(MyReviewContent hakone) {
            academyName.setText(hakone.getAcademy_name());
            mystar.setText(String.valueOf(hakone.getUser_score()));
            created_date.setText(String.valueOf(hakone.getCreated_date()));
            review_content.setText(String.valueOf(hakone.getContent()));
        }

    }

}
