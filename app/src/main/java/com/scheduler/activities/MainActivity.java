package com.scheduler.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scheduler.R;
import com.scheduler.adapters.ReminderAdapter;
import com.scheduler.database.ReminderDAO;
import com.scheduler.database.ReminderRoomDB;
import com.scheduler.models.Reminder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton floatingActionButton;
    CalendarView calendarView;
    ReminderAdapter reminderAdapter;
    RecyclerView recyclerView;
    List<Reminder> reminderList;
    MaterialToolbar materialToolbar;

    ReminderRoomDB roomDB;
    ReminderDAO reminderDAO;

    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDarkTheme(this);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        roomDB = Room.databaseBuilder(this, ReminderRoomDB.class,"ReminderDB").allowMainThreadQueries().build();
        reminderDAO = roomDB.reminderDAO();
        reminderList = reminderDAO.getReminderByDate(dateFormat.format(new Date()));
        reminderAdapter = new ReminderAdapter(reminderList);
        recyclerView.setAdapter(reminderAdapter);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                reminderList = reminderDAO.getReminderByDate(dateFormat.format(new GregorianCalendar(year, month, dayOfMonth).getTime()));
                reminderAdapter = new ReminderAdapter(reminderList);
                recyclerView.setAdapter(reminderAdapter);
            }
        });

        materialToolbar = findViewById(R.id.toolbar);
        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.settings:
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                }
                return false;
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

    @Override
    protected void onRestart() {
        super.onRestart();
        reminderList = reminderDAO.getReminderByDate(dateFormat.format(new Date(calendarView.getDate())));
        reminderAdapter = new ReminderAdapter(reminderList);
        recyclerView.setAdapter(reminderAdapter);

    }

    public static void setDarkTheme(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String theme = sharedPreferences.getString("theme_list","System Default");
        switch (theme) {
            case "Light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "Dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "System Default":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

}