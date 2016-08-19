package com.example.jonas.grades;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.jonas.grades.Adapters.SubjectAdapter;
import com.example.jonas.grades.DB.DB;
import com.example.jonas.grades.DB.GradeManagerContract.*;
import com.example.jonas.grades.Models.Exam;
import com.example.jonas.grades.Models.Semester;
import com.example.jonas.grades.Models.Subject;

import java.util.ArrayList;
import java.util.HashMap;

public class SemesterActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Semester CurrentSemester;
    private ArrayList<Subject> Subjects = new ArrayList<>();
    private Context ActivityContext = this;
    private Resources Texts;
    SubjectAdapter subjectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester);

        Texts = getResources();
        Utilities.setTexts(Texts);
        DB.setDatabase(ActivityContext);
        // TODO For testing purposes.
        // DB.generateTestData();
        // TODO Change to current Subject.
        CurrentSemester = DB.getAllSemesters().get(0);
        Subjects = DB.getSubjectsFromSemester(CurrentSemester.ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(CurrentSemester.Name);
        toolbar.setBackgroundColor(Utilities.colorFromGrade(CurrentSemester.getSemesterAverage()));
        ((TextView)findViewById(R.id.semester_average)).setText(String.valueOf(CurrentSemester.getSemesterAverage()));
        findViewById(R.id.toolbar_layout).setBackgroundColor(Utilities.colorFromGrade(CurrentSemester.getSemesterAverage()));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long subjectID = DB.insert(SubjectEntry.TABLE_NAME, new HashMap<String, String>(){{
                    put(SubjectEntry.COLUMN_NAME_NAME, Texts.getString(R.string.new_subject));
                    put(SubjectEntry.COLUMN_NAME_SEMESTER, String.valueOf(CurrentSemester.ID));
                }});
                Subjects.add(new Subject(subjectID, Texts.getString(R.string.new_subject), new ArrayList<Exam>()));
                // TODO I dont know why i regenerated the view here instead of notifyDataSetChanged
//                    generateOverview();
                subjectAdapter.notifyDataSetChanged();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this); // Setup.

        // TODO generate Drawer data.
generateOveriew();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Subjects = DB.getSubjectsFromSemester(CurrentSemester.ID);
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

    private void generateOveriew(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.subject_list_view);
        subjectAdapter = new SubjectAdapter(Subjects);
        recyclerView.setAdapter(subjectAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(ActivityContext, 2));
    }
}
