package com.scheduler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.scheduler.R;

import java.util.Date;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    Button startDateButton, endDateButton, startTimeButton, endTimeButton ;
    MaterialCheckBox isAllDayEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        startDateButton = findViewById(R.id.start_date_button);
        endDateButton = findViewById(R.id.end_date_button);
        startTimeButton = findViewById(R.id.start_time_button);
        endTimeButton = findViewById(R.id.end_time_button);

        Date date = new Date();
        startDateButton.setText(date.toString());
        startDateButton.setOnClickListener(this);

        isAllDayEvent = findViewById(R.id.is_all_day_checkbox);
        isAllDayEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    startTimeButton.setVisibility(View.GONE);
                    endTimeButton.setVisibility(View.GONE);
                } else {
                    startTimeButton.setVisibility(View.VISIBLE);
                    endTimeButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

        datePicker.show(getSupportFragmentManager(),"DATE_PICKER");
    }
}