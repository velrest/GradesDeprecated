package com.example.jonas.grades.Models;

/**
 * Created by jonas on 13.07.16.
 */
public class Exam {
    public final long ID;
    public String Name;
    public double Grade;
    public int Weight;

    public Exam(long id, String name, double grade, int weight) {
        ID = id;
        Name = name;
        Grade = grade;
        Weight = weight;
    }
}
