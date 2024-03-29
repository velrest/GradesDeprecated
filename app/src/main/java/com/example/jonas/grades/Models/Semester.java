package com.example.jonas.grades.Models;

import static com.example.jonas.grades.Utilities.*;

import java.util.ArrayList;

public class Semester {

    public final long ID;
    public String Name;
    public ArrayList<Subject> Subjects;

    public Semester(long ID, String name, ArrayList<Subject> subjects) {
        this.ID = ID;
        Name = name;
        Subjects = subjects;
    }

    public double getSemesterAverage(){
        if (Subjects.size() > 0) {
            double average = 0;
            for (Subject subject : Subjects) {
                average += subject.getSubjectAverage();
            }
            return round((average / Subjects.size()), 2);
        }else return 0;
    }
}
