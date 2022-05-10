package com.scheduler.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.room.Room;

import com.scheduler.activities.MainActivity;
import com.scheduler.database.ReminderDAO;
import com.scheduler.database.ReminderRoomDB;
import com.scheduler.models.Reminder;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ReminderRoomDB roomDB = Room.databaseBuilder(context,ReminderRoomDB.class,"ReminderDB").allowMainThreadQueries().build();
        ReminderDAO reminderDAO = roomDB.reminderDAO();
        List<Reminder> reminderList = reminderDAO.getAllReminder();
        for (Reminder reminder : reminderList) {
            String startDateTime = reminder.getStartDate() + " " + reminder.getStartTime();
            try {
                Date date = MainActivity.dateTimeFormat.parse(startDateTime);
                if (date.getTime() <= new Date().getTime()) {
                    reminderList.remove(reminder);
                } else {
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intentID = new Intent(context, AlarmReceiver.class);
                    intent.putExtra("ID", reminder.getId());
                    PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intentID, PendingIntent.FLAG_ONE_SHOT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date.getTime(), alarmIntent);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}