package com.example.jonas.grades;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utilities {

    private static Resources Texts;



    private static Context ActivityContext;
    private static AppBarLayout Bar;

    public static double round(double value, int places) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int colorFromGrade(double average) {
        final String[] colors = new String[]{
                "#ff3333",
                "#FF9D00",
                "#83C400"
        };

        String colorString;

        if (average < 4.0) colorString = colors[0];
        else if (average >= 4.0 && average < 5.0) colorString = colors[1];
        else colorString = colors[2];

        return Color.parseColor(colorString);
    }

//    public static void setBarInfo(double average){
//        Bar.findViewById(R.id.toolbar).setBackgroundColor(colorFromGrade(average));
//        Bar.findViewById(R.id.toolbar_layout).setBackgroundColor(colorFromGrade(average));
//        ((TextView)Bar.findViewById(R.id.bar_average)).setText(String.valueOf(average));
//    }
//
//    public static void updateValues(Context activityContext, AppBarLayout appBarLayout, double average){
//        ActivityContext = activityContext;
//        Bar = appBarLayout;
//        setBarInfo(average);
//    }

    public static void setTexts(Resources texts) {
        Texts = texts;
    }
    public static Resources getTexts(){return  Texts;}
    public static Context getActivityContext() { return ActivityContext; }
    public static void setActivityContext(Context activityContext) { ActivityContext = activityContext; }
}
