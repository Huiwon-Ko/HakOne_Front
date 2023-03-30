package com.example.hakone;

import static com.example.hakone.ApiClient.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.MyViewHolder>{
    private SharedPreferences sharedPreferences;
    private Context context;

    private final ApiInterface apiInterface;
    private List<ReviewListContent> reviewListContent;
    private long review_id;
    private String profile_pic;
    private String username;
    private float score;
    private String created_date;
    private String content;


    public ReviewListAdapter(List<ReviewListContent> reviewListContent, Context context, long review_id, String profile_pic, String username, float score, String created_date, String content){
        this.reviewListContent = reviewListContent;
        this.context = context;
        this.review_id = review_id;
        this.profile_pic = profile_pic;
        this.username = username;
        this.score = score;
        this.created_date = created_date;
        this.content = content;

        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    @NonNull
    @Override
    public ReviewListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        ReviewListAdapter.MyViewHolder viewHolder = new ReviewListAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewListAdapter.MyViewHolder holder, int position) {
        ReviewListContent hakone = reviewListContent.get(position);
        //boolean isStar = hakone.isStar();
        holder.bind(hakone);


    }

    @Override
    public int getItemCount() {if(reviewListContent==null) return 0;
        return reviewListContent.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nickname;
        TextView star;
        TextView date;
        TextView review_content;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.nickname);
            star = itemView.findViewById(R.id.star);
            date = itemView.findViewById(R.id.date);
            review_content = itemView.findViewById(R.id.review_content);

        }
        void bind(ReviewListContent hakone) {
            nickname.setText(hakone.getUsername());
            star.setText(String.valueOf(hakone.getScore()));
            date.setText(String.valueOf(hakone.getCreated_date()));
            review_content.setText(String.valueOf(hakone.getContent()));
        }
    }


}
