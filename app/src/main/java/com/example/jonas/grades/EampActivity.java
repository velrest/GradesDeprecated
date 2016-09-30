//package com.example.jonas.grades;
//
//import android.content.Context;
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.support.design.widget.AppBarLayout;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.support.v7.widget.helper.ItemTouchHelper;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//
//import com.example.jonas.grades.Adapters.ExamAdapter;
//import com.example.jonas.grades.DB.DB;
//import com.example.jonas.grades.Models.Exam;
//import com.example.jonas.grades.Models.Subject;
//import com.google.gson.Gson;
//
//import java.text.MessageFormat;
//import java.util.HashMap;
//
//import com.example.jonas.grades.DB.GradesContract.*;
//import static com.example.jonas.grades.Utilities.*;
//
//
//public class SubjectActivity extends AppCompatActivity {
//
//    private Subject CurrentSubject;
//    private Context ActivityContext = this;
//    private Resources Texts;
//    private ExamAdapter ExamAdapterObj;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.x_activity_subject);
//
//        Texts = getResources();
//        Gson gson = new Gson();
//        CurrentSubject = gson.fromJson(getIntent().getExtras().get("Subject").toString(), Subject.class);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(CurrentSubject.Name);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
////        updateValues(ActivityContext, (AppBarLayout)findViewById(R.id.app_bar), CurrentSubject.getSubjectAverage());
//
//        generateSubjectView();
//    }
//
//    private void generateSubjectView(){
//        RecyclerView examListView = (RecyclerView) findViewById(R.id.exam_list_view);
//        ExamAdapterObj = new ExamAdapter(CurrentSubject);
//        assert examListView != null;
//        examListView.setAdapter(ExamAdapterObj);
//        examListView.setLayoutManager(new GridLayoutManager(ActivityContext, 2));
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        if (fab != null) {
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    long gradeID = DB.insert(ExamEntry.TABLE_NAME, new HashMap<String, String>(){{
//                        put(ExamEntry.COLUMN_NAME_NAME, Texts.getString(R.string.new_test));
//                        put(ExamEntry.COLUMN_NAME_GRADE, String.valueOf(0.0));
//                        put(ExamEntry.COLUMN_NAME_WEIGHT, String.valueOf(0));
//                        put(ExamEntry.COLUMN_NAME_SUBJECT, String.valueOf(CurrentSubject.ID));
//                    }});
//                    CurrentSubject.Exams.add(0, new Exam(gradeID, Texts.getString(R.string.new_test), 0.0, 0));
//                    ExamAdapterObj.notifyItemInserted(0);
//                }
//            });
//        }
//
//        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                // TODO make a element invisible on swipe
//                Exam swipedExam = CurrentSubject.Exams.get(viewHolder.getAdapterPosition());
//                DB.delete(swipedExam.ID, ExamEntry.TABLE_NAME, ExamEntry._ID);
//                CurrentSubject.Exams.remove(swipedExam);
//                Toast.makeText(ActivityContext, MessageFormat.format(Texts.getString(R.string.delete_info) ,swipedExam.Name), Toast.LENGTH_SHORT).show();
////                setBarInfo(CurrentSubject.getSubjectAverage());
//                ExamAdapterObj.notifyItemRemoved(viewHolder.getAdapterPosition());
//            }
//        };
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
//        itemTouchHelper.attachToRecyclerView(examListView);
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
////        updateValues(ActivityContext, (AppBarLayout)findViewById(R.id.app_bar), CurrentSubject.getSubjectAverage());
//    }
//}
