package com.example.jonas.grades.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.jonas.grades.DB.GradesContract.*;
import com.example.jonas.grades.Models.Exam;
import com.example.jonas.grades.Models.Semester;
import com.example.jonas.grades.Models.Subject;

import java.util.ArrayList;
import java.util.Map;

public class DB {

    public static SQLiteDatabase DB;

    public static void setDatabase(Context context){
        GradeManagerDbHelper DbHelper = new GradeManagerDbHelper(context);
        DB = DbHelper.getWritableDatabase();
    }

    public static ArrayList<Semester> getAllSemesters(){
        ArrayList<Semester> semesters =  new ArrayList<>();

        String[] semesterProjection = {
                SemesterEntry._ID,
                SemesterEntry.COLUMN_NAME_NAME
        };

        Cursor cursor = DB.query(
                SemesterEntry.TABLE_NAME,
                semesterProjection,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            long semesterID = cursor.getInt(cursor.getColumnIndexOrThrow(SemesterEntry._ID));
            Semester semester = new Semester(
                    cursor.getLong(cursor.getColumnIndexOrThrow(SemesterEntry._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SemesterEntry.COLUMN_NAME_NAME)),
                    getSubjectsFromSemester(semesterID)
            );
            semesters.add(semester);
            cursor.moveToNext();
        }
        return semesters;
    }

    public static ArrayList<Subject> getSubjectsFromSemester(long id){
        ArrayList<Subject> subjects =  new ArrayList<>();

        String[] subjectProjection = {
                SubjectEntry._ID,
                SubjectEntry.COLUMN_NAME_NAME,
                SubjectEntry.COLUMN_NAME_SEMESTER
        };

        String selection = SubjectEntry.COLUMN_NAME_SEMESTER + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursorSubject = DB.query(
                SubjectEntry.TABLE_NAME,
                subjectProjection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursorSubject.moveToFirst();
        while(!cursorSubject.isAfterLast()){
            long subjectID = cursorSubject.getInt(cursorSubject.getColumnIndexOrThrow(SubjectEntry._ID));
            Subject subject = new Subject(
                    cursorSubject.getLong(cursorSubject.getColumnIndexOrThrow(SemesterEntry._ID)),
                    cursorSubject.getString(cursorSubject.getColumnIndexOrThrow(SubjectEntry.COLUMN_NAME_NAME)),
                    getGradesFromSubject(subjectID)
            );
            subjects.add(subject);
            cursorSubject.moveToNext();
        }
        return subjects;
    }

    public static ArrayList<Exam> getGradesFromSubject(long id){
        ArrayList<Exam> grades = new ArrayList<>();
        String[] gradeProjection = {
                ExamEntry._ID,
                ExamEntry.COLUMN_NAME_NAME,
                ExamEntry.COLUMN_NAME_GRADE,
                ExamEntry.COLUMN_NAME_WEIGHT
        };

        String selection = ExamEntry.COLUMN_NAME_SUBJECT + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursorGrade = DB.query(
                ExamEntry.TABLE_NAME,
                gradeProjection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        cursorGrade.moveToFirst();
        while(!cursorGrade.isAfterLast()){
            grades.add(
                new Exam(
                    cursorGrade.getLong(cursorGrade.getColumnIndexOrThrow(ExamEntry._ID)),
                    cursorGrade.getString(cursorGrade.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_NAME)),
                    cursorGrade.getDouble(cursorGrade.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_GRADE)),
                    cursorGrade.getInt(cursorGrade.getColumnIndexOrThrow(ExamEntry.COLUMN_NAME_WEIGHT))
                )
            );
            cursorGrade.moveToNext();
        }
        return grades;
    }

    public static long insert(String tableName, Map<String,String> valueMap){
        ContentValues values = new ContentValues();
        for (Map.Entry<String ,String> entry:valueMap.entrySet()) {
            values.put(entry.getKey(), entry.getValue());
        }
        return DB.insert(
                tableName,
                "null",
                values
        );
    }

    public static void update(long id, String newValue, String tableName, String columnToUpdate, String IDColumn){
        ContentValues values = new ContentValues();
        values.put(columnToUpdate, newValue);

        String selection = IDColumn + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        DB.update(
                tableName,
                values,
                selection,
                selectionArgs
        );
    }

    public static void delete(long id, String tableName, String IDColumn){
        String selection = IDColumn + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        DB.delete(
                tableName,
                selection,
                selectionArgs
        );
    }
}
