package com.example.jonas.grades;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by jonas on 30.06.16.
 */
public class Utilities {

    private static Resources Texts;

    public static void setTexts(Resources texts) {
        Texts = texts;
    }
    public static Resources getTexts(){return  Texts;}

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

    public static String decreaseSubjectNameString(String longString){
        if (longString.toCharArray().length > 20) {
            String shortString = "";
            int index = 0;
            while (index < 20) {
                shortString += longString.toCharArray()[index];
                index++;
            }
            shortString += "...";
            return shortString;
        } else return longString;
    }

    public static String decreaseGradeString(String longString){
        if (longString.split(",").length > 4) {
            String shortString = "";
            int index = 0;
            while (index < 4) {
                shortString += longString.split(",")[index]+",";
                index++;
            }
            shortString += " ...";
            return shortString;
        } else return longString;
    }

    public static Dialog makeDialog(Context context, String infoText, String value){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.input_dialog);
        ((TextView)dialog.findViewById(R.id.info)).setText(infoText);
        ((TextView)dialog.findViewById(R.id.value)).setText(value);
        TextView save = ((TextView)dialog.findViewById(R.id.save));
        save.setText(Texts.getString(R.string.ok));
        return dialog;
    }

    public static void setBarInfo(double average, AppBarLayout bar){
        bar.findViewById(R.id.toolbar).setBackgroundColor(colorFromGrade(average));
        bar.findViewById(R.id.toolbar_layout).setBackgroundColor(colorFromGrade(average));
        ((TextView)bar.findViewById(R.id.bar_average)).setText(String.valueOf(average));
    }
}
