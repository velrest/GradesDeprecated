package com.example.jonas.grades;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.jonas.grades.Adapters.ExamAdapter;
import com.example.jonas.grades.DB.DB;
import com.example.jonas.grades.Models.Exam;
import com.example.jonas.grades.Models.Subject;
import com.google.gson.Gson;

import java.util.HashMap;

import com.example.jonas.grades.DB.GradeManagerContract.*;


public class SubjectActivity extends AppCompatActivity {

    private Subject CurrentSubject;
    private Context ActivityContext = this;
    private Resources Texts;
    ExamAdapter examAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        Texts = getResources();
        Gson gson = new Gson();
        CurrentSubject = gson.fromJson(getIntent().getExtras().get("Subject").toString(), Subject.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Utilities.colorFromGrade(CurrentSubject.getSubjectAverage()));
        ((TextView)findViewById(R.id.subject_average)).setText(String.valueOf(CurrentSubject.getSubjectAverage()));
        toolbar.setTitle(CurrentSubject.SubjectName);
        findViewById(R.id.toolbar_layout).setBackgroundColor(Utilities.colorFromGrade(CurrentSubject.getSubjectAverage()));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long gradeID = DB.insert(GradeEntry.TABLE_NAME, new HashMap<String, String>(){{
                        put(GradeEntry.COLUMN_NAME_NAME, Texts.getString(R.string.new_test));
                        put(GradeEntry.COLUMN_NAME_GRADE, String.valueOf(0.0));
                        put(GradeEntry.COLUMN_NAME_WEIGHT, String.valueOf(0));
                        put(GradeEntry.COLUMN_NAME_SUBJECT, String.valueOf(CurrentSubject.ID));
                    }});
                    CurrentSubject.exams.add(new Exam(gradeID, Texts.getString(R.string.new_test), 0.0, 0));
                    // TODO I dont know why i regenerated the view here instead of notifyDataSetChanged
//                    generateSubjectView();
                    examAdapter.notifyDataSetChanged();
                }
            });
        }
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                finish();
            }
        });
        generateSubjectView();
    }

    private void generateSubjectView(){
        RecyclerView examListView = (RecyclerView) findViewById(R.id.exam_list_view);
        examAdapter = new ExamAdapter(CurrentSubject.exams,  CurrentSubject, (AppBarLayout) findViewById(R.id.app_bar), (TextView) findViewById(R.id.subject_average));
        examListView.setAdapter(examAdapter);
        examListView.setLayoutManager(new GridLayoutManager(ActivityContext, 2));
    }
}
