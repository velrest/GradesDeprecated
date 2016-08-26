package com.example.jonas.grades.Models;

import static com.example.jonas.grades.Utilities.*;

import java.util.ArrayList;

/**
 * Created by jonas on 29.06.16.
 */
public class Subject {

    public final long ID;
    public String Name;
    public ArrayList<Exam> Exams;

    public Subject(long id, String name, ArrayList<Exam> exams) {
        ID = id;
        Name = name;
        this.Exams = exams;
    }

    public double getSubjectAverage(){
        if (Exams.size() > 0) {
            double average = 0;
            for (Exam grade : Exams) {
                average += grade.Grade * grade.Weight / 100;
                average = round(average, 2);
            }
            return average;
        } else return 0;
    }
}