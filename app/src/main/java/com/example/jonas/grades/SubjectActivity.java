package com.example.jonas.grades;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.jonas.grades.Adapters.SubjectAdapter;
import com.example.jonas.grades.DB.DB;
import com.example.jonas.grades.DB.GradesContract.*;
import com.example.jonas.grades.Models.Semester;
import com.example.jonas.grades.Models.Subject;


import static com.example.jonas.grades.Utilities.*;

import java.util.ArrayList;
import java.util.HashMap;

public class SubjectActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Semester CurrentSemester;
    private Context ActivityContext = this;
    private Resources Texts;
    private SubjectAdapter SubjectAdapterObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject);

        Texts = getResources();
        setTexts(Texts);
        DB.setDatabase(ActivityContext);
        setActivityContext(this);

        // TODO Change to current Subject.
        try {
            CurrentSemester = DB.getAllSemesters().get(0);
        } catch (IndexOutOfBoundsException e){
            CurrentSemester = new Semester(0, "Semester 1", new ArrayList<Subject>());
            DB.insert(SemesterEntry.TABLE_NAME, new HashMap<String, String>(){{
                put(SemesterEntry.COLUMN_NAME_NAME, CurrentSemester.Name);
            }});
        }
        CurrentSemester.Subjects = DB.getSubjectsFromSemester(CurrentSemester.ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(CurrentSemester.Name);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this); // Setup.



//        updateValues(ActivityContext, (AppBarLayout)findViewById(R.id.app_bar), CurrentSemester.getSemesterAverage());

        // TODO generate Drawer data.
        generateOveriew();
    }

    private void generateOveriew(){
        RecyclerView subjectListView = (RecyclerView) findViewById(R.id.subject_list_view);
        SubjectAdapterObj = new SubjectAdapter(CurrentSemester);
        subjectListView.setAdapter(SubjectAdapterObj);
        subjectListView.setLayoutManager(new LinearLayoutManager(this));

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        assert fab != null;
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                long subjectID = DB.insert(SubjectEntry.TABLE_NAME, new HashMap<String, String>(){{
//                    put(SubjectEntry.COLUMN_NAME_NAME, Texts.getString(R.string.new_subject));
//                    put(SubjectEntry.COLUMN_NAME_SEMESTER, String.valueOf(CurrentSemester.ID));
//                }});
//                CurrentSemester.Subjects.add(0, new Subject(subjectID, Texts.getString(R.string.new_subject), new ArrayList<Exam>()));
//                SubjectAdapterObj.notifyItemInserted(0);
//            }
//        });

//        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                // TODO make element invisible on Swipe
//                Subject swipedSubject = CurrentSemester.Subjects.get(viewHolder.getAdapterPosition());
//                DB.delete(swipedSubject.ID, SubjectEntry.TABLE_NAME, SubjectEntry._ID);
//                CurrentSemester.Subjects.remove(viewHolder.getAdapterPosition());
//                Toast.makeText(ActivityContext, MessageFormat.format(Texts.getString(R.string.delete_info) ,swipedSubject.Name), Toast.LENGTH_SHORT).show();
//                setBarInfo(CurrentSemester.getSemesterAverage());
//                SubjectAdapterObj.notifyItemRemoved(viewHolder.getAdapterPosition());
//            }
//        };
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
//        itemTouchHelper.attachToRecyclerView(subjectListView);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CurrentSemester.Subjects = DB.getSubjectsFromSemester(CurrentSemester.ID);
//        updateValues(ActivityContext, (AppBarLayout)findViewById(R.id.app_bar), CurrentSemester.getSemesterAverage());
        generateOveriew();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    } // Overriden Methods

}
