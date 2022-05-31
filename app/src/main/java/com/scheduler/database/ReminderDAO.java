package com.scheduler.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.scheduler.models.Reminder;

import java.util.List;

@Dao
public interface ReminderDAO {

    @Query("SELECT * FROM Reminder")
    List<Reminder> getAllReminder();

    @Query("SELECT * FROM Reminder WHERE start_date =:startDate")
    List<Reminder> getReminderByDate(String startDate);

    @Query("SELECT * FROM Reminder WHERE id =:id")
    Reminder getReminderById(long id);

    @Insert
    long addReminder(Reminder reminder);

    @Delete
    void deleteReminder(Reminder reminder);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateReminder(Reminder reminder);

}
