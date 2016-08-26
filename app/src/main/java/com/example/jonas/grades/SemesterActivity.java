package com.example.jonas.grades;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jonas.grades.Adapters.SubjectAdapter;
import com.example.jonas.grades.DB.DB;
import com.example.jonas.grades.DB.GradesContract.*;
import com.example.jonas.grades.Models.Exam;
import com.example.jonas.grades.Models.Semester;
import com.example.jonas.grades.Models.Subject;
import static com.example.jonas.grades.Utilities.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class SemesterActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Semester CurrentSemester;
    private Context ActivityContext = this;
    private Resources Texts;
    private SubjectAdapter SubjectAdapterObj;
    private AppBarLayout BarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester);

        Texts = getResources();
        setTexts(Texts);
        DB.setDatabase(ActivityContext);
        // TODO Change to current Subject.
        CurrentSemester = DB.getAllSemesters().get(0);
        CurrentSemester.Subjects = DB.getSubjectsFromSemester(CurrentSemester.ID);
        BarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(CurrentSemester.Name);

        setBarInfo(CurrentSemester.getSemesterAverage(), BarLayout);
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
                CurrentSemester.Subjects.add(new Subject(subjectID, Texts.getString(R.string.new_subject), new ArrayList<Exam>()));
                // TODO I dont know why i regenerated the view here instead of notifyDataSetChanged
//                    generateOverview();
                SubjectAdapterObj.notifyDataSetChanged();
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

    @Override
    protected void onResume() {
        super.onResume();
        CurrentSemester.Subjects = DB.getSubjectsFromSemester(CurrentSemester.ID);
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
        RecyclerView subjectListView = (RecyclerView) findViewById(R.id.subject_list_view);
        SubjectAdapterObj = new SubjectAdapter(CurrentSemester.Subjects);
        subjectListView.setAdapter(SubjectAdapterObj);
        subjectListView.setLayoutManager(new GridLayoutManager(ActivityContext, 2));

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // TODO remove on Swipe
                Subject swipedSubject = CurrentSemester.Subjects.get(viewHolder.getAdapterPosition());
                DB.delete(swipedSubject.ID, SubjectEntry.TABLE_NAME, SubjectEntry._ID);
                CurrentSemester.Subjects.remove(swipedSubject);
                Toast.makeText(ActivityContext, MessageFormat.format(Texts.getString(R.string.delete_info) ,swipedSubject.Name), Toast.LENGTH_SHORT).show();
                setBarInfo(CurrentSemester.getSemesterAverage(), BarLayout);
                SubjectAdapterObj.notifyDataSetChanged();
//                makeDialog(ActivityContext, MessageFormat.format(Texts.getString(R.string.dialog_info_delete_subject),CurrentSubject.Exams.get(viewHolder.getAdapterPosition()).Name), );
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(subjectListView);
    }
}
