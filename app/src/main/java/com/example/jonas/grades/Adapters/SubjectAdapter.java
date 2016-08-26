package com.example.jonas.grades.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonas.grades.DB.GradesContract;
import com.example.jonas.grades.SubjectActivity;
import static com.example.jonas.grades.Utilities.*;
import com.example.jonas.grades.DB.DB;
import com.example.jonas.grades.Models.Exam;
import com.example.jonas.grades.R;
import com.example.jonas.grades.Models.Subject;
import com.example.jonas.grades.*;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by jonas on 03.08.16.
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder>{

    private ArrayList<Subject> Subjects;
    private Context ActivityContext;

    public SubjectAdapter(ArrayList<Subject> subjects) {
        Subjects = subjects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ActivityContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ActivityContext);
        View contView = inflater.inflate(R.layout.list_subject_prefab, parent, false);
        return new ViewHolder(contView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.SubjectAverage.setTextColor(colorFromGrade(Subjects.get(position).getSubjectAverage()));
        holder.SubjectAverage.setText(String.valueOf(round(Subjects.get(position).getSubjectAverage(), 1)));
        ((LinearLayout)holder.SubjectAverage.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityContext, SubjectActivity.class);
                Gson gson = new Gson();
                String jsonString = gson.toJson(Subjects.get(position));
                intent.putExtra("Subject",  jsonString);
                ActivityContext.startActivity(intent);
            }
        });
        ((LinearLayout)holder.SubjectAverage.getParent()).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new Dialog(ActivityContext, getTexts().getString(R.string.rename), Dialog.STRING_INPUT_DIALOG, Subjects.get(position).Name) {
                    @Override
                    public void onClick() {
                        Subjects.get(position).Name = Input.getText().toString();
                        DB.update(
                                Subjects.get(position).ID,
                                Input.getText().toString(),
                                GradesContract.SubjectEntry.TABLE_NAME,
                                GradesContract.SubjectEntry.COLUMN_NAME_NAME,
                                GradesContract.SubjectEntry._ID
                        );
                        Toast.makeText(ActivityContext, getTexts().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                }.show();
                return true;
            }
        });

        holder.SubjectName.setText(Subjects.get(position).Name);
        ArrayList<String> grades = new ArrayList<>();
        for (Exam grade:Subjects.get(position).Exams) {
            grades.add(String.valueOf(grade.Grade));
        }
    }

    @Override
    public int getItemCount() {
        return Subjects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView SubjectName;
        public TextView SubjectAverage;

        public ViewHolder(View itemView) {
            super(itemView);
            SubjectName = (TextView)itemView.findViewById(R.id.subject_name);
            SubjectAverage = (TextView)itemView.findViewById(R.id.bar_average);
        }
    }
}
