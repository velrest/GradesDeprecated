package com.example.jonas.grades.Models;

import com.example.jonas.grades.Utilities;

import java.util.ArrayList;

/**
 * Created by jonas on 29.06.16.
 */
public class Subject {

    public final long ID;
    public String SubjectName;
    public ArrayList<Exam> exams;

    public Subject(long id, String subjectName, ArrayList<Exam> exams) {
        ID = id;
        SubjectName = subjectName;
        this.exams = exams;
    }

    public double getSubjectAverage(){
        double average = 0;
        for (Exam grade : exams){
            average += grade.Grade*grade.Weight/100;
            average = Utilities.round(average, 2);
        }return average;
    }
}