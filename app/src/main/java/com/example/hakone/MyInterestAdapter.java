package com.example.hakone;

import static com.example.hakone.ApiClient.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyInterestAdapter extends RecyclerView.Adapter<MyInterestAdapter.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private List<HakOneList> hakOneList1;
    private Context context;

    //private List<HakOneList> hakOneItemLists;
    private List<String> subjects;


    private long user_id;
    private boolean isStar;

    private long academyId;

    private final ApiInterface apiInterface;

    private SharedPreferences sharedPreferences;


    public MyInterestAdapter(List<HakOneList> hakOneList1, Context context, List<String> subjects, RecyclerViewInterface recyclerViewInterface, long user_id, long academyId, boolean isStar) {
        this.hakOneList1 = hakOneList1;
        this.context = context;
        this.subjects = subjects;
        this.recyclerViewInterface = recyclerViewInterface;
        this.user_id = user_id;
        this.academyId = academyId;
        this.isStar = isStar;

        /*
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("user_id", user_id);
        editor.putLong("academyId", academyId);
        editor.putBoolean("isStar", isStar);
        editor.apply();

         */

        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
        apiInterface = retrofit.create(ApiInterface.class);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, recyclerViewInterface);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HakOneList hakone = hakOneList1.get(position);
        boolean isStar = hakone.isStar();
        holder.bind(hakone);

        if (subjects != null) {
            String subject = subjects.get(position);
            holder.subjectList.setText(subject);
            Log.d("TAG", "MyInterest subject" + subject);
            Log.d("TAG", "MyInterest subjects" + subjects);
        }
        if (isStar== true) {
            holder.Interest.setImageResource(R.drawable.baseline_star_24);
            Log.d("TAG", "Star isStar" + isStar);
        } else {
            holder.Interest.setImageResource(R.drawable.ic_baseline_star_outline_24);
            Log.d("TAG", "Star isStar" + isStar);
        }
    }

    @Override
    public int getItemCount() {
        return hakOneList1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView academyName;
        TextView avgTuition;
        TextView subjectList;
        ImageButton Interest;
        TextView avg_score;
        TextView review_count;

        // ...

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            academyName = itemView.findViewById(R.id.HakOne_name);
            avgTuition = itemView.findViewById(R.id.avgTuition);
            subjectList = itemView.findViewById(R.id.Subject_list);
            Interest = itemView.findViewById(R.id.Interest);
            avg_score = itemView.findViewById(R.id.avg_score);
            review_count = itemView.findViewById(R.id.review_count);


            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        long clickedAcademyId = hakOneList1.get(position).getAcademyId();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("academyId", clickedAcademyId);
                        editor.apply();

                        Intent intent = new Intent(context, HakOneItem.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });


            Interest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    HakOneList hakone = hakOneList1.get(position);
                    long academyId = hakone.getAcademyId();

                    if (hakone.isStar()){
                        // 등록 취소
                        Call<Void> call = apiInterface.deleteStarAcademy(user_id, academyId);


                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.d("TAG", "Star academyId" + academyId);
                                // 서버에 delete 요청이 성공한 경우
                                hakone.setStar(false);
                                Interest.setImageResource(R.drawable.ic_baseline_star_outline_24);
                                Log.d("Tag", "관심 취소 성공");
                                Log.d("Tag", Long.toString(user_id));
                                Log.d("Tag", Long.toString(academyId));
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                // 서버에 delete 요청이 실패한 경우
                                Toast.makeText(context, "관심 취소 실패", Toast.LENGTH_SHORT).show();
                                Log.e("TAG", "관심 취소 실패", t);
                                Log.d("Tag", Long.toString(user_id));
                                Log.d("Tag", Long.toString(academyId));
                            }
                        });
                    }

                }
            });

        }
        void bind(HakOneList hakone) {
            academyName.setText(hakone.getAcademyName());
            avgTuition.setText(String.valueOf(hakone.getAvgTuition()));
            subjectList.setText(String.valueOf(hakone.getSubjects()));
            avg_score.setText(String.valueOf(hakone.getAvg_score()));
            review_count.setText(String.valueOf(hakone.getReview_count()));
        }
    }
}

