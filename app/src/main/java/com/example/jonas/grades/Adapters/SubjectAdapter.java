package com.example.jonas.grades.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jonas.grades.DB.GradesContract;
import com.example.jonas.grades.Models.Semester;
import com.example.jonas.grades.SubjectActivity;
import static com.example.jonas.grades.Utilities.*;
import com.example.jonas.grades.DB.DB;
import com.example.jonas.grades.Models.Exam;
import com.example.jonas.grades.R;
import com.example.jonas.grades.*;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder>{

    private Semester CurrentSemester;

    public SubjectAdapter(Semester currentSemester) {
        CurrentSemester = currentSemester;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(getActivityContext());
        View contView = inflater.inflate(R.layout.list_subject_prefab, parent, false);
        return new ViewHolder(contView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.SubjectAverage.setTextColor(colorFromGrade(CurrentSemester.Subjects.get(position).getSubjectAverage()));
        holder.SubjectAverage.setText(String.valueOf(round(CurrentSemester.Subjects.get(position).getSubjectAverage(), 1)));
        ((LinearLayout)holder.SubjectAverage.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivityContext(), SubjectActivity.class);
                Gson gson = new Gson();
                String jsonString = gson.toJson(CurrentSemester.Subjects.get(position));
                intent.putExtra("Subject",  jsonString);
                getActivityContext().startActivity(intent);
            }
        });
        ((LinearLayout)holder.SubjectAverage.getParent()).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new Dialog(getTexts().getString(R.string.rename), Dialog.STRING_INPUT_DIALOG, CurrentSemester.Subjects.get(position).Name) {
                    @Override
                    public double onClick() {
                        CurrentSemester.Subjects.get(position).Name = Input.getText().toString();
                        DB.update(
                                CurrentSemester.Subjects.get(position).ID,
                                Input.getText().toString(),
                                GradesContract.SubjectEntry.TABLE_NAME,
                                GradesContract.SubjectEntry.COLUMN_NAME_NAME,
                                GradesContract.SubjectEntry._ID
                        );
                        notifyDataSetChanged();
                        return CurrentSemester.getSemesterAverage();
                    }
                }.show();
                return true;
            }
        });

        holder.SubjectName.setText(CurrentSemester.Subjects.get(position).Name);
        ArrayList<String> grades = new ArrayList<>();
        for (Exam grade:CurrentSemester.Subjects.get(position).Exams) {
            grades.add(String.valueOf(grade.Grade));
        }
    }

    @Override
    public int getItemCount() {
        return CurrentSemester.Subjects.size();
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
