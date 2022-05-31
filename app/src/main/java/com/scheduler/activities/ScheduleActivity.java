package com.scheduler.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.gson.reflect.TypeToken;
import com.scheduler.R;
import com.scheduler.broadcast.AlarmReceiver;
import com.scheduler.database.ReminderDAO;
import com.scheduler.database.ReminderRoomDB;
import com.scheduler.models.People;
import com.scheduler.models.Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    long id = -1;
    AlarmManager alarmManager;
    PendingIntent alarmIntent;

    ReminderRoomDB roomDB;
    ReminderDAO reminderDAO;

    MaterialToolbar materialToolbar;
    Button startDateButton, endDateButton, startTimeButton, endTimeButton, saveButton, addPeopleButton;
    MaterialCheckBox isAllDayEvent;
    TextInputLayout titleTextInputLayout, descriptionTextInputLayout;

    List<People> selectedPeopleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        roomDB = Room.databaseBuilder(getApplicationContext(), ReminderRoomDB.class,"ReminderDB").allowMainThreadQueries().build();
        reminderDAO = roomDB.reminderDAO();

        titleTextInputLayout = findViewById(R.id.title_textinputlayout);
        descriptionTextInputLayout = findViewById(R.id.description_textinputlayout);
        materialToolbar = findViewById(R.id.toolbar);

        startDateButton = findViewById(R.id.start_date_button);
        endDateButton = findViewById(R.id.end_date_button);
        startTimeButton = findViewById(R.id.start_time_button);
        endTimeButton = findViewById(R.id.end_time_button);
        saveButton = findViewById(R.id.save_button);
        addPeopleButton = findViewById(R.id.people_button);

        saveButton.setOnClickListener(this);
        addPeopleButton.setOnClickListener(this);

        Date date = new Date();

        startDateButton.setText(MainActivity.dateFormat.format(date));
        startDateButton.setOnClickListener(this);

        startTimeButton.setText(MainActivity.timeFormat.format(new Date(date.getTime() + 60 * 60 * 1000)));
        startTimeButton.setOnClickListener(this);

        endDateButton.setText(MainActivity.dateFormat.format(date));
        endDateButton.setOnClickListener(this);

        endTimeButton.setText(MainActivity.timeFormat.format(new Date(date.getTime() + 2 * 60 * 60 * 1000)));
        endTimeButton.setOnClickListener(this);

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

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        id = getIntent().getIntExtra("ID", -1);
        if (id != -1) {
            Reminder reminder = reminderDAO.getReminderById(id);

            startDateButton.setText(reminder.getStartDate());
            endDateButton.setText(reminder.getEndDate());
            startTimeButton.setText(reminder.getStartTime());
            endTimeButton.setText(reminder.getEndTime());
            selectedPeopleList = new Gson().fromJson(reminder.getPeopleJSON(), new TypeToken<List<People>>(){}.getType());
            addPeopleButton.setText(peopleListToString(selectedPeopleList));
            isAllDayEvent.setChecked(reminder.getAllDay());
            titleTextInputLayout.getEditText().setText(reminder.getTitle());
            descriptionTextInputLayout.getEditText().setText(reminder.getDescription());

            materialToolbar.inflateMenu(R.menu.schedule_menu);
            materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.delete:
                            reminderDAO.deleteReminder(reminder);
                            finish();
                            return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_date_button:
            case R.id.end_date_button:
                datePicker((Button) view);
                break;
            case R.id.start_time_button:
            case R.id.end_time_button:
                timePicker((Button) view);
                break;
            case R.id.save_button:
                save();
                break;
            case R.id.people_button:
                addPeople();
                break;

        }
    }

    private void save() {


        String title, description, startDate, startTime, endDate, endTime, peopleJSON;
        boolean isAllDay, isEvent;
        title = titleTextInputLayout.getEditText().getText().toString().trim();
        description = descriptionTextInputLayout.getEditText().getText().toString().trim();
        startDate = startDateButton.getText().toString();
        startTime = startTimeButton.getText().toString();
        endDate = endDateButton.getText().toString();
        endTime = endTimeButton.getText().toString();
        isAllDay = isAllDayEvent.isChecked();
        isEvent = true;
        peopleJSON = new Gson().toJson(selectedPeopleList);

        //Validation
        if (title.equals("")) {
            titleTextInputLayout.setError("Title is required");
            return;
        } else {
            titleTextInputLayout.setError(null);
        }
        if (description.equals("")) {
            description = " ";
        }
        if (isAllDay) {
            startTime = (MainActivity.timeFormat.format("00:00 AM "));
            endTime = (MainActivity.timeFormat.format("00:00 AM "));
        }

        Reminder reminder = new Reminder(title, description, startDate, startTime, endDate, endTime, isAllDay, isEvent, peopleJSON);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);

        if (id == -1) {
            id = reminderDAO.addReminder(reminder);
            intent.putExtra("ID", id);
            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) id, intent, PendingIntent.FLAG_ONE_SHOT);
        } else {
            reminder.setId((int) id);
            reminderDAO.updateReminder(reminder);
            intent.putExtra("ID", id);
            alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) id, intent, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.cancel(alarmIntent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Date date = MainActivity.dateTimeFormat.parse(startDate + " " + startTime);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date.getTime(), alarmIntent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        finish();
    }

    private void addPeople() {
        Intent intent = new Intent(this, SelectPeopleActivity.class);
        if (selectedPeopleList != null) {
            intent.putExtra("PEOPLE", new Gson().toJson(selectedPeopleList));
        }
        startActivityForResult(intent, 1);
    }

    private void datePicker(Button button) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                button.setText(MainActivity.dateFormat.format(new Date(selection)));
            }
        });
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void timePicker(Button button) {
        Date date = new Date();
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTitleText("Select time")
                .setHour(date.getHours())
                .setMinute(date.getMinutes())
                .build();
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Date date = new SimpleDateFormat("HH:mm").parse(timePicker.getHour() + ":" + timePicker.getMinute());
                    button.setText(MainActivity.timeFormat.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                    button.setText("Parse Error");
                }
            }
        });
        timePicker.show(getSupportFragmentManager(), "TIME_PICKER");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            String peopleJSON = data.getStringExtra("PEOPLE");
            selectedPeopleList = new Gson().fromJson(peopleJSON, new TypeToken<List<People>>(){}.getType());
            if (selectedPeopleList == null) { return; }
            addPeopleButton.setText(peopleListToString(selectedPeopleList));
        }
    }

    public static String peopleListToString(List<People> peopleList) {
        String text = "";
        if (peopleList.size() == 0) { return "Select people to remind"; }
        for (People people : peopleList) {
            if (!text.equals("")) { text += ", ";}
            text += people.getName();
        }
        if (text.endsWith(", ")) {
            text.substring(0, text.length() - 2);
        }
        return text;
    }
}