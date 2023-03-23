package com.example.hakone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    private List<HakOneList> hakOneList;
    private List<HakOneList> hakOneListFull;
    private List<HakOneList> regionList; //지역 필터링 된 것들 넣어줄 것.

    private Context context;
    private List<String> subjects;


    public RecyclerAdapter(List<HakOneList> hakOneList, Context context, List<String> subjects){
        this.hakOneList = hakOneList;
        this.context = context;
        this.subjects = subjects;
        //hakOneListFull = new ArrayList<>(hakOneList); //같은 내용을 담은 새로운 리스트 생성.
        hakOneListFull = new ArrayList<>(hakOneList);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list, parent, false);
        //RecyclerAdapter adapter = new RecyclerAdapter(hakOneList, context, subjects);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HakOneList hakone = hakOneList.get(position);
        holder.bind(hakone);

        if (subjects != null) {
            String subject = subjects.get(position);
            holder.subjectList.setText(subject);
        }
    }

    @Override
    public int getItemCount() {
        if(hakOneList==null) return 0;
        return hakOneList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView academyName;
        TextView avgTuition;
        TextView subjectList;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            academyName = itemView.findViewById(R.id.HakOne_name);
            avgTuition = itemView.findViewById(R.id.avgTuition);
            subjectList = itemView.findViewById(R.id.Subject_list);

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



    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        regionList = new ArrayList<>();
        for (HakOneList region : hakOneList) {
            if (region.getRegion().equals(selectedItem)) {
                regionList.add(region);
            }
        }
        notifyDataSetChanged();
    }

}
