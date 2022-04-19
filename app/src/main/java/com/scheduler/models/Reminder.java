package com.scheduler.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Reminder {
    
    @PrimaryKey(autoGenerate = true)
    Integer id;
    String title;
    String description;

    @ColumnInfo(name = "start_date")
    String startDate;

    @ColumnInfo(name = "start_time")
    String startTime;

    @ColumnInfo(name = "end_date")
    String endDate;

    @ColumnInfo(name = "end_time")
    String endTime;

    @ColumnInfo(name = "is_all_day")
    Boolean isAllDay;

    @ColumnInfo(name = "is_event")
    Boolean isEvent;

    @ColumnInfo(name = "people_json")
    String peopleJSON;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Boolean getAllDay() {
        return isAllDay;
    }

    public void setAllDay(Boolean allDay) {
        isAllDay = allDay;
    }

    public String getPeopleJSON() {
        return peopleJSON;
    }

    public void setPeopleJSON(String peopleJSON) {
        this.peopleJSON = peopleJSON;
    }
}
