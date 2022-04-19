package com.scheduler.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.scheduler.models.Reminder;

@Database(entities = {Reminder.class}, version = 1)
public abstract class ReminderRoomDB extends RoomDatabase {
    public abstract ReminderDAO reminderDAO();
}
