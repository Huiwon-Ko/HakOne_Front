package com.example.hakone;

import static android.content.Context.MODE_PRIVATE;
import static com.example.hakone.ApiClient.BASE_URL;
import static com.example.hakone.Home.hakOneList1;

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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;
    private SharedPreferences sharedPreferences;
    private List<HakOneList> hakOneList;
    //private List<HakOneList> hakOneListFull;
    //private List<HakOneList> regionList; //지역 필터링 된 것들 넣어줄 것.

    private Context context;
    private List<String> subjects;

    private long user_id;
    private boolean isStar;

    private long academyId;

    private final ApiInterface apiInterface;


    public RecyclerAdapter(List<HakOneList> hakOneList, Context context, List<String> subjects, RecyclerViewInterface recyclerViewInterface, long user_id, long academyId, boolean isStar){
        this.hakOneList = hakOneList;
        this.context = context;
        this.subjects = subjects;
        this.recyclerViewInterface = recyclerViewInterface;
        this.user_id = user_id;
        this.academyId = academyId;
        this.isStar = isStar;

        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view, recyclerViewInterface);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HakOneList hakone = hakOneList.get(position);
        boolean isStar = hakone.isStar();
        holder.bind(hakone);

        //Log.d("TAG", "RecyclerAdapter subject" + subject);
        Log.d("TAG", "RecyclerAdapter subjects" + subjects);


        if (subjects != null) {
            String subject = subjects.get(position);
            holder.subjectList.setText(subject);
            //Log.d("TAG", "RecyclerAdapter subject" + subject);
            //Log.d("TAG", "RecyclerAdapter subjects" + subjects);
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
    public int getItemCount() {if(hakOneList==null) return 0;
        return hakOneList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView academyName;
        TextView avgTuition;
        TextView subjectList;
        ImageButton Interest;


        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            academyName = itemView.findViewById(R.id.HakOne_name);
            avgTuition = itemView.findViewById(R.id.avgTuition);
            subjectList = itemView.findViewById(R.id.Subject_list);
            Interest = itemView.findViewById(R.id.Interest);


            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        long clickedAcademyId = hakOneList.get(position).getAcademyId();
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
                    HakOneList hakone = hakOneList.get(position);
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

                    } else {
                        // 관심 등록을 하지 않은 경우

                        Call<Void> call = apiInterface.postStarAcademy(user_id, academyId);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.d("TAG", "Star academyId" + academyId);
                                // 서버에 post 요청이 성공한 경우
                                hakone.setStar(true);
                                Interest.setImageResource(R.drawable.baseline_star_24);
                                Log.d("Tag","관심 등록 성공");
                                Log.d("Tag", Long.toString(user_id));
                                Log.d("Tag", Long.toString(academyId));
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                // 서버에 post 요청이 실패한 경우
                                Toast.makeText(context, "관심 등록 실패", Toast.LENGTH_SHORT).show();
                                Log.e("TAG", "관심 등록 실패", t);
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
        }
    }

    public void filterList(ArrayList<HakOneList> filteredList) {
        hakOneList = filteredList;
        notifyDataSetChanged();
    }


    public void regionSelected(List<HakOneList> regionList) {
        hakOneList = regionList;
        notifyDataSetChanged();

    }

    public void subjectSelected(List<HakOneList> subjectList) {
        hakOneList = subjectList;
        notifyDataSetChanged();
    }

    public void gradeSelected(List<HakOneList> gradeList) {
        hakOneList = gradeList;
        notifyDataSetChanged();
    }

    public void sortSelected(List<HakOneList> sortList) {
        hakOneList = sortList;
        notifyDataSetChanged();
    }
}
