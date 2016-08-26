package com.example.jonas.grades.Adapters;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.jonas.grades.Utilities.*;
import com.example.jonas.grades.DB.DB;
import com.example.jonas.grades.DB.GradesContract;
import com.example.jonas.grades.Dialog;
import com.example.jonas.grades.R;
import com.example.jonas.grades.Models.Subject;

/**
 * Created by jonas on 03.08.16.
 */
public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder>{

    private Subject CurrentSubject;
    private Context ActivityContext;
    private AppBarLayout Bar;

    public ExamAdapter(Subject currentSubject, AppBarLayout bar) {
        CurrentSubject = currentSubject;
        Bar = bar;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ActivityContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ActivityContext);
        View contView = inflater.inflate(R.layout.list_exams_prefab, parent, false);
        return new ViewHolder(contView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.ExamName.setText(CurrentSubject.Exams.get(position).Name);
        holder.ExamGrade.setText(String.valueOf(CurrentSubject.Exams.get(position).Grade));
        holder.ExamGrade.setTextColor(colorFromGrade(CurrentSubject.Exams.get(position).Grade));
        holder.ExamWeight.setText(String.valueOf(CurrentSubject.Exams.get(position).Weight));

        holder.ExamGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Dialog(ActivityContext, getTexts().getString(R.string.dialog_info_grade), Dialog.MULTI_PICKER_DIALOG, String.valueOf(CurrentSubject.Exams.get(position).Grade)) {

                    @Override
                    public void onClick() {
                        double gradeValue = Double.valueOf(Input.getText().toString());
                        if (gradeValue >= 1 && gradeValue <= 6) {
                            DB.update(CurrentSubject.Exams.get(position).ID, String.valueOf(gradeValue), GradesContract.ExamEntry.TABLE_NAME, GradesContract.ExamEntry.COLUMN_NAME_GRADE, GradesContract.ExamEntry._ID);
                            CurrentSubject.Exams.get(position).Grade = gradeValue;
                            notifyDataSetChanged();
                            Toast.makeText(ActivityContext, getTexts().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ActivityContext, getTexts().getString(R.string.error_grade_input), Toast.LENGTH_LONG).show();
                        }
                    }
                }.show();
            }
        });
        holder.ExamWeightBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Dialog(ActivityContext, getTexts().getString(R.string.dialog_info_weight), Dialog.PICKER_DIALOG, String.valueOf(CurrentSubject.Exams.get(position).Weight)) {

                    @Override
                    public void onClick() {
                        int weightValue = Picker.getValue();
                        DB.update(CurrentSubject.Exams.get(position).ID, String.valueOf(weightValue), GradesContract.ExamEntry.TABLE_NAME, GradesContract.ExamEntry.COLUMN_NAME_WEIGHT, GradesContract.ExamEntry._ID);
                        CurrentSubject.Exams.get(position).Weight = weightValue;
                        notifyDataSetChanged();
                        Toast.makeText(ActivityContext, getTexts().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                    }
                }.show();
            }
        });
        holder.ExamName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Dialog(ActivityContext, getTexts().getString(R.string.dialog_info_name), Dialog.STRING_INPUT_DIALOG, CurrentSubject.Exams.get(position).Name) {

                    @Override
                    public void onClick() {
                        String name = Input.getText().toString();
                        DB.update(CurrentSubject.Exams.get(position).ID, name, GradesContract.ExamEntry.TABLE_NAME, GradesContract.ExamEntry.COLUMN_NAME_NAME, GradesContract.ExamEntry._ID);
                        CurrentSubject.Exams.get(position).Name = name;
                        notifyDataSetChanged();
                        Toast.makeText(ActivityContext, getTexts().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                    }
                }.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return CurrentSubject.Exams.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView ExamName;
        public TextView ExamGrade;
        public TextView ExamWeight;
        public LinearLayout ExamWeightBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ExamName = (TextView)itemView.findViewById(R.id.exam_name);
            ExamGrade = (TextView)itemView.findViewById(R.id.exam_grade);
            ExamWeight = (TextView)itemView.findViewById(R.id.exam_weight);
            ExamWeightBox = (LinearLayout)itemView.findViewById(R.id.exam_weight_box);
        }
    }
}
