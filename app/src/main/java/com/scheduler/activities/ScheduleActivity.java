package com.scheduler.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.scheduler.R;

import java.util.Date;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    Button dateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        dateButton = findViewById(R.id.start_date_button);
        Date date = new Date();
        dateButton.setText(date.toString());
        dateButton.setOnClickListener(this);
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