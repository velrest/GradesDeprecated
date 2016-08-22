package com.example.jonas.grades.Adapters;

import android.app.Dialog;
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
import com.example.jonas.grades.DB.GradeManagerContract;
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

        holder.ExamGrade.setOnClickListener(new SubjectOnClickListener(getTexts().getString(R.string.dialog_info_grade)) {
            @Override
            void setDialogValue() {
                try {
                    double gradeValue = Double.valueOf(((TextView)EditDialog.findViewById(R.id.value)).getText().toString());
                    if (gradeValue >= 1 && gradeValue <= 6) {
                        DB.update(CurrentSubject.Exams.get(position).ID, String.valueOf(gradeValue), GradeManagerContract.ExamEntry.TABLE_NAME, GradeManagerContract.ExamEntry.COLUMN_NAME_GRADE, GradeManagerContract.ExamEntry._ID);
                        CurrentSubject.Exams.get(position).Grade = gradeValue;
                        notifyDataSetChanged();
                        EditDialog.dismiss();
                        Toast.makeText(ActivityContext, getTexts().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ActivityContext, getTexts().getString(R.string.error_grade_input), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ActivityContext, getTexts().getString(R.string.error_grade_input), Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.ExamWeightBox.setOnClickListener(new SubjectOnClickListener(getTexts().getString(R.string.dialog_info_weight)) {
            @Override
            void setDialogValue() {
                try {
                    int weightValue = Integer.valueOf(((TextView)EditDialog.findViewById(R.id.value)).getText().toString());
                    if (weightValue >= 1 && weightValue <= 100) {
                        DB.update(CurrentSubject.Exams.get(position).ID, String.valueOf(weightValue), GradeManagerContract.ExamEntry.TABLE_NAME, GradeManagerContract.ExamEntry.COLUMN_NAME_WEIGHT, GradeManagerContract.ExamEntry._ID);
                        CurrentSubject.Exams.get(position).Weight = weightValue;
                        notifyDataSetChanged();
                        EditDialog.dismiss();
                        Toast.makeText(ActivityContext, getTexts().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ActivityContext, getTexts().getString(R.string.error_weight_input), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ActivityContext, getTexts().getString(R.string.error_weight_input), Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.ExamName.setOnClickListener(new SubjectOnClickListener(getTexts().getString(R.string.dialog_info_test)) {
            @Override
            void setDialogValue() {
                String name = ((TextView)EditDialog.findViewById(R.id.value)).getText().toString();
                DB.update(CurrentSubject.Exams.get(position).ID, name, GradeManagerContract.ExamEntry.TABLE_NAME, GradeManagerContract.ExamEntry.COLUMN_NAME_NAME, GradeManagerContract.ExamEntry._ID);
                CurrentSubject.Exams.get(position).Name = name;
                notifyDataSetChanged();
                EditDialog.dismiss();
                Toast.makeText(ActivityContext, getTexts().getString(R.string.saved), Toast.LENGTH_SHORT).show();
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

    abstract class SubjectOnClickListener implements View.OnClickListener{

        private String Text;
        public Dialog EditDialog;


        public SubjectOnClickListener(String text) {
            Text = text;
        }

        @Override
        public void onClick(View view) {
            TextView clickedView;
            if (view.getClass() == LinearLayout.class) clickedView = (TextView) view.findViewById(R.id.exam_weight);
            else clickedView = (TextView)view;
            EditDialog = makeDialog(ActivityContext, Text, clickedView.getText().toString());
            EditDialog.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDialogValue();
                    setBarInfo(CurrentSubject.getSubjectAverage(), Bar);
                }
            });
            EditDialog.show();
        }

        abstract void setDialogValue();
    }
}
