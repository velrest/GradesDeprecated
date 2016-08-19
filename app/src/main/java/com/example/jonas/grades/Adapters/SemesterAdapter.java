package com.example.jonas.grades.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jonas.grades.R;
import com.example.jonas.grades.Models.Semester;

import java.util.ArrayList;

/**
 * Created by jonas on 03.08.16.
 */
public class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.ViewHolder>{

    private ArrayList<Semester> Semesters;

    public SemesterAdapter(ArrayList<Semester> semesters) {
        Semesters = semesters;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contView = inflater.inflate(R.layout.list_semester_prefab, parent, false);
        return new ViewHolder(contView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.SemesterName.setText(Semesters.get(position).Name);
        holder.SemesterAverage.setText(String.valueOf(Semesters.get(position).getSemesterAverage()));
    }

    @Override
    public int getItemCount() {
        return Semesters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView SemesterName;
        public TextView SemesterAverage;

        public ViewHolder(View itemView) {
            super(itemView);
            SemesterName = (TextView)itemView.findViewById(R.id.semester_name);
            SemesterAverage = (TextView)itemView.findViewById(R.id.semester_average);
        }
    }
}
