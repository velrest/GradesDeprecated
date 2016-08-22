package com.example.jonas.grades.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by jonas on 19.07.16.
 */
public final class GradeManagerContract {

    public GradeManagerContract(){}

    public static abstract class SemesterEntry implements BaseColumns{
        public static final String TABLE_NAME = "semester";
        public static final String COLUMN_NAME_NAME = "name";
    }

    public static  abstract class SubjectEntry implements BaseColumns{
        public static final String TABLE_NAME = "subject";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SEMESTER = "id_semester";
    }

    public static  abstract class ExamEntry implements BaseColumns{
        public static final String TABLE_NAME = "grade";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_GRADE = "grade";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_SUBJECT = "id_subject";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String FLOAT_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_SEMESTERS =
        "CREATE TABLE " + SemesterEntry.TABLE_NAME + " (" +
        SemesterEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
        SemesterEntry.COLUMN_NAME_NAME + TEXT_TYPE +
        " )";

    private static final String SQL_CREATE_SUBJECTS =
        "CREATE TABLE " + SubjectEntry.TABLE_NAME + " (" +
        SubjectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
        SubjectEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
        SubjectEntry.COLUMN_NAME_SEMESTER + INT_TYPE +
        " )";

    private static final String SQL_CREATE_GRADES =
        "CREATE TABLE " + ExamEntry.TABLE_NAME + " (" +
        ExamEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
        ExamEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
        ExamEntry.COLUMN_NAME_GRADE + FLOAT_TYPE + COMMA_SEP +
        ExamEntry.COLUMN_NAME_WEIGHT + INT_TYPE + COMMA_SEP +
        ExamEntry.COLUMN_NAME_SUBJECT + INT_TYPE +
        " )";

    private static final String SQL_DELETE_SEMESTERS =
            "DROP TABLE IF EXISTS " + SemesterEntry.TABLE_NAME;

    private static final String SQL_DELETE_SUBJECTS =
        "DROP TABLE IF EXISTS " + SubjectEntry.TABLE_NAME;

    private static final String SQL_DELETE_GRADES =
            "DROP TABLE IF EXISTS " + ExamEntry.TABLE_NAME;

    public static class GradeManagerDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "GradeManager.db";

        public GradeManagerDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_SEMESTERS);
            db.execSQL(SQL_CREATE_SUBJECTS);
            db.execSQL(SQL_CREATE_GRADES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_SEMESTERS);
            db.execSQL(SQL_DELETE_SUBJECTS);
            db.execSQL(SQL_DELETE_GRADES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

}
