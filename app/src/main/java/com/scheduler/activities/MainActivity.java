package com.scheduler.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scheduler.R;
import com.scheduler.adapters.ReminderAdapter;
import com.scheduler.database.ReminderDAO;
import com.scheduler.database.ReminderRoomDB;
import com.scheduler.models.Reminder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton floatingActionButton;
    CalendarView calendarView;
    ReminderAdapter reminderAdapter;
    RecyclerView recyclerView;
    List<Reminder> reminderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ReminderRoomDB roomDB = Room.databaseBuilder(this, ReminderRoomDB.class,"ReminderDB").build();
        ReminderDAO reminderDAO = roomDB.reminderDAO();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        reminderList = reminderDAO.getReminderByDate(dateFormat.format(new Date()));

        reminderAdapter = new ReminderAdapter(reminderList);
        recyclerView.setAdapter(reminderAdapter);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                reminderList = reminderDAO.getReminderByDate(year + "/" + month + "/" + dayOfMonth);
                reminderAdapter = new ReminderAdapter(reminderList);
                recyclerView.setAdapter(reminderAdapter);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floating_action_button:
                startActivity(new Intent(this, ScheduleActivity.class));
                break;
        }

    }
}