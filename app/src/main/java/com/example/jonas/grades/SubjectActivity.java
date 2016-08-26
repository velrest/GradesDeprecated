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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.jonas.grades.Adapters.ExamAdapter;
import com.example.jonas.grades.DB.DB;
import com.example.jonas.grades.Models.Exam;
import com.example.jonas.grades.Models.Subject;
import com.google.gson.Gson;

import java.text.MessageFormat;
import java.util.HashMap;

import com.example.jonas.grades.DB.GradesContract.*;
import static com.example.jonas.grades.Utilities.*;


public class SubjectActivity extends AppCompatActivity {

    private Subject CurrentSubject;
    private Context ActivityContext = this;
    private Resources Texts;
    private ExamAdapter ExamAdapterObj;
    private AppBarLayout BarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        Texts = getResources();
        Gson gson = new Gson();
        CurrentSubject = gson.fromJson(getIntent().getExtras().get("Subject").toString(), Subject.class);
        BarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(CurrentSubject.Name);
        setBarInfo(CurrentSubject.getSubjectAverage(), BarLayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long gradeID = DB.insert(ExamEntry.TABLE_NAME, new HashMap<String, String>(){{
                        put(ExamEntry.COLUMN_NAME_NAME, Texts.getString(R.string.new_test));
                        put(ExamEntry.COLUMN_NAME_GRADE, String.valueOf(0.0));
                        put(ExamEntry.COLUMN_NAME_WEIGHT, String.valueOf(0));
                        put(ExamEntry.COLUMN_NAME_SUBJECT, String.valueOf(CurrentSubject.ID));
                    }});
                    CurrentSubject.Exams.add(new Exam(gradeID, Texts.getString(R.string.new_test), 0.0, 0));
                    ExamAdapterObj.notifyDataSetChanged();
                }
            });
        }
        generateSubjectView();
    }

    private void generateSubjectView(){
        RecyclerView examListView = (RecyclerView) findViewById(R.id.exam_list_view);
        ExamAdapterObj = new ExamAdapter(CurrentSubject, BarLayout);
        assert examListView != null;
        examListView.setAdapter(ExamAdapterObj);
        examListView.setLayoutManager(new GridLayoutManager(ActivityContext, 2));

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // TODO remove on Swipe
                Exam swipedExam = CurrentSubject.Exams.get(viewHolder.getAdapterPosition());
                DB.delete(swipedExam.ID, ExamEntry.TABLE_NAME, ExamEntry._ID);
                CurrentSubject.Exams.remove(swipedExam);
                Toast.makeText(ActivityContext, MessageFormat.format(Texts.getString(R.string.delete_info) ,swipedExam.Name), Toast.LENGTH_SHORT).show();
                setBarInfo(CurrentSubject.getSubjectAverage(), BarLayout);
                ExamAdapterObj.notifyDataSetChanged();
//                makeDialog(ActivityContext, MessageFormat.format(Texts.getString(R.string.dialog_info_delete_subject),CurrentSubject.Exams.get(viewHolder.getAdapterPosition()).Name), );
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(examListView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
