package com.example.jonas.grades;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import java.util.ArrayList;
import java.util.Arrays;
import static com.example.jonas.grades.Utilities.*;

public abstract class Dialog extends android.app.Dialog{

    public static final int PICKER_DIALOG = 0;
    public static final int MULTI_PICKER_DIALOG = 1;
    public static final int STRING_INPUT_DIALOG = 2;

    private Button Cancel;
    private Button Save;
    private String Info;
    private int DialogType;
    private String DefaultValue;
    private Context ActivityContext;

    public NumberPicker Picker;
    public NumberPicker PickerDeci;
    public NumberPicker PickerCenti;
    public EditText Input;


    public Dialog(String info, int dialogType, String defaultValue) {
        super(getActivityContext());
        Info = info;
        DialogType = dialogType;
        DefaultValue = defaultValue;
        ActivityContext = getActivityContext();

        switch (DialogType){
            case PICKER_DIALOG:
                setContentView(R.layout.dialog_picker);
                Picker = (NumberPicker) findViewById(R.id.picker);
                Picker.setMinValue(0);
                Picker.setMaxValue(100);
                Picker.setValue(Integer.valueOf(DefaultValue));
                break;

            case MULTI_PICKER_DIALOG:
                setContentView(R.layout.dialog_multi_picker);
                ArrayList<String> numbs = new ArrayList<>(Arrays.asList(DefaultValue.split("\\.")));
                Picker = (NumberPicker) findViewById(R.id.picker);
                Picker.setMinValue(1);
                Picker.setMaxValue(6);
                Picker.setValue(Integer.valueOf(numbs.get(0)));

                PickerDeci = (NumberPicker) findViewById(R.id.picker_deci);
                PickerDeci.setMinValue(0);
                PickerDeci.setMaxValue(9);
                // .split("")[1] Because first one is on split: ""
                try {
                    PickerDeci.setValue(Integer.valueOf(numbs.get(1).split("")[1]));
                } catch (IndexOutOfBoundsException e){
                    PickerDeci.setValue(0);
                }

                PickerCenti = (NumberPicker) findViewById(R.id.picker_centi);
                PickerCenti.setMinValue(0);
                PickerCenti.setMaxValue(9);
                // .split("")[2] Because first one is on split: ""
                try {
                    PickerCenti.setValue(Integer.valueOf(numbs.get(1).split("")[2]));
                } catch (IndexOutOfBoundsException e){
                    PickerCenti.setValue(0);
                }

                Picker.setOnValueChangedListener(new android.widget.NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(android.widget.NumberPicker numberPicker, int i, int i1) {
                        if (i1 == 6){
                            PickerDeci.setValue(0);
                            PickerDeci.setEnabled(false);
                            PickerCenti.setValue(0);
                            PickerCenti.setEnabled(false);
                        } else {
                            PickerDeci.setEnabled(true);
                            PickerCenti.setEnabled(true);
                        }
                    }
                });

                break;

            case STRING_INPUT_DIALOG:
                setContentView(R.layout.dialog_input);
                Input = (EditText) findViewById(R.id.value);
                Input.setSelectAllOnFocus(true);
                Input.setText(DefaultValue);
                Input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focused) {
                        if (focused) getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                });
                break;
        }

        ((TextView)findViewById(R.id.info)).setText(Info);

        Save = (Button)findViewById(R.id.save);
        Save.setText(getTexts().getString(R.string.ok));
        Cancel = (Button)findViewById(R.id.cancel);
        Cancel.setText(getTexts().getString(R.string.cancel));

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBarInfo(Dialog.this.onClick());
                Toast.makeText(ActivityContext, getTexts().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    public void updateAdapter(RecyclerView.Adapter adapter, int position){
        adapter.notifyItemChanged(position);
    }

    public abstract double onClick();
}
