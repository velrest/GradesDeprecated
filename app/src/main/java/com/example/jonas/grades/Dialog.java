package com.example.jonas.grades;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.*;

import com.example.jonas.grades.Models.Exam;

/**
 * Created by jonas on 25.08.16.
 */
public abstract class Dialog extends android.app.Dialog{

    public static final int WEIGHT_DIALOG = 0;
    public static final int GRADE_DIALOG = 1;
    public static final int NAME_DIALOG = 2;

    private Context ActivityContext;
    private Exam CurrentExam;
    private Button Cancel;
    private Button Save;


    public Dialog(Context context, String info, int type, Exam currentExam) {
        super(context);
        CurrentExam = currentExam;
        ActivityContext = context;



        switch (type){
            case Dialog.GRADE_DIALOG: gradeDialog();
                break;

            case Dialog.WEIGHT_DIALOG: weightDialog();
                break;

            case Dialog.NAME_DIALOG: nameDialog();
                break;
        }

        Save = (Button)findViewById(R.id.save);
        Save.setText(Utilities.getTexts().getString(R.string.ok));
        Cancel = (Button)findViewById(R.id.cancel);
        Cancel.setText(Utilities.getTexts().getString(R.string.cancel));

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DB.update(CurrentExam.ID, String.valueOf(numberPicker.getValue()), GradesContract.ExamEntry.TABLE_NAME, GradesContract.ExamEntry.COLUMN_NAME_WEIGHT, GradesContract.ExamEntry._ID);
//                CurrentExam.Weight = numberPicker.getValue();
                Dialog.this.onClick();
                dismiss();
            }
        });
    }

    private void weightDialog(){
        setContentView(R.layout.weight_dialog);
        final NumberPicker numberPicker = (NumberPicker) findViewById(R.id.picker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);
        numberPicker.setValue(CurrentExam.Weight);
    }

    private void gradeDialog(){
        setContentView(R.layout.input_dialog);
        EditText value = (EditText) findViewById(R.id.value);
        value.setText(String.valueOf(CurrentExam.Grade));
        value.setInputType(InputType.TYPE_CLASS_NUMBER);

    }

    private void nameDialog(){
        setContentView(R.layout.input_dialog);
        EditText value = (EditText) findViewById(R.id.value);
        value.setText(CurrentExam.Name);;
    }

    public abstract void onClick();
}
