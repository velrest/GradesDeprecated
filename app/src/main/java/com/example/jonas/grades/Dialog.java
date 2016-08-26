package com.example.jonas.grades;

import android.content.Context;
import android.os.DeadObjectException;
import android.text.InputType;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jonas on 25.08.16.
 */
public abstract class Dialog extends android.app.Dialog{

    public static final int PICKER_DIALOG = 0;
    public static final int MULTI_PICKER_DIALOG = 1;
    public static final int STRING_INPUT_DIALOG = 2;

    private Button Cancel;
    private Button Save;
    private String Info;
    private int DialogType;
    private String DefaultValue;

    public NumberPicker Picker;
    public NumberPicker PickerDeci;
    public NumberPicker PickerCenti;
    public EditText Input;


    public Dialog(Context context, String info, int dialogType, String defaultValue) {
        super(context);
        Info = info;
        DialogType = dialogType;
        DefaultValue = defaultValue;

//        if (DialogType == Dialog.MULTI_PICKER_DIALOG || DialogType == Dialog.STRING_INPUT_DIALOG){
//            setContentView(R.layout.dialog_input);
//            Input = (EditText) findViewById(R.id.value);
//            Input.setSelectAllOnFocus(true);
//            Input.setText(DefaultValue);
//            if (DialogType == Dialog.MULTI_PICKER_DIALOG) {
//                Input.setInputType(InputType.TYPE_CLASS_NUMBER);
//            }
//        }
//        else {
//            setContentView(R.layout.dialog_picker);
//            Picker = (NumberPicker) findViewById(R.id.picker);
//            Picker.setValue(Integer.valueOf(DefaultValue));
//            Picker.setMinValue(0);
//            Picker.setMaxValue(100);
//        }

        switch (DialogType){
            case PICKER_DIALOG:
                setContentView(R.layout.dialog_picker);
                Picker = (NumberPicker) findViewById(R.id.picker);
                Picker.setValue(Integer.valueOf(DefaultValue));
                Picker.setMinValue(0);
                Picker.setMaxValue(100);
                break;

            case MULTI_PICKER_DIALOG:
                setContentView(R.layout.dialog_multi_picker);
                ArrayList<String> numbs = new ArrayList<>(Arrays.asList(DefaultValue.split("\\.")));
                numbs.remove(0);
                Picker = (NumberPicker) findViewById(R.id.picker);
                Picker.setValue(Integer.valueOf(numbs.get(0)));
                Picker.setMinValue(1);
                Picker.setMaxValue(6);
                Picker.setOnValueChangedListener(new android.widget.NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(android.widget.NumberPicker numberPicker, int i, int i1) {
                        if (i1 == 6 || i1 == 1){
                            PickerDeci.setValue(0);
                            PickerDeci.setEnabled(false);
                            PickerCenti.setValue(0);
                            PickerCenti.setEnabled(false);
                        }
                    }
                });

                PickerDeci = (NumberPicker) findViewById(R.id.picker_deci);
                // .split("")[1] Because first one is on split: ""
                try {
                    PickerDeci.setValue(Integer.valueOf(numbs.get(0).split("")[1]));
                } catch (IndexOutOfBoundsException e){
                    PickerDeci.setValue(0);
                }
                Picker.setMinValue(0);
                Picker.setMaxValue(9);

                PickerCenti = (NumberPicker) findViewById(R.id.picker_centi);
                // .split("")[2] Because first one is on split: ""
                try {
                    PickerCenti.setValue(Integer.valueOf(numbs.get(0).split("")[2]));
                } catch (IndexOutOfBoundsException e){
                    PickerCenti.setValue(0);
                }
                Picker.setMinValue(0);
                Picker.setMaxValue(9);
                break;

            case STRING_INPUT_DIALOG:
                setContentView(R.layout.dialog_input);
                Input = (EditText) findViewById(R.id.value);
                Input.setSelectAllOnFocus(true);
                Input.setText(DefaultValue);
                break;
        }

        ((TextView)findViewById(R.id.info)).setText(Info);

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
                Dialog.this.onClick();
                dismiss();
            }
        });
    }

    public abstract void onClick();
}
