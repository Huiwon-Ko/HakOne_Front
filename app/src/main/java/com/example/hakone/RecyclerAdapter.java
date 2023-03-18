package com.example.hakone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<HakOneList> hakOneList;

    public RecyclerAdapter(List<HakOneList> hakOneList){
        this.hakOneList = hakOneList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HakOneList hakone = hakOneList.get(position);
        holder.bind(hakone);
    }


    @Override
    public int getItemCount() {
        return hakOneList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView academyName;
        TextView avgTuition;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            academyName = itemView.findViewById(R.id.HakOne_name);
            avgTuition = itemView.findViewById(R.id.avgTuition);

        }
        void bind(HakOneList hakone) {
            academyName.setText(hakone.getAcademyName());
            avgTuition.setText(String.valueOf(hakone.getAvgTuition()));
        }

    }
}
