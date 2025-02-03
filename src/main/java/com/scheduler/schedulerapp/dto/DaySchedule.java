package com.scheduler.schedulerapp.dto;

import com.scheduler.schedulerapp.model.Person;

import java.util.Map;

public class DaySchedule {
    private int dayNumber;
    private String dayName;
    private String date; // in ISO format (yyyy-MM-dd)
    private Map<Long, Person> assignments;

    public DaySchedule(int dayNumber, String dayName, String date, Map<Long, Person> assignments) {
        this.dayNumber = dayNumber;
        this.dayName = dayName;
        this.date = date;
        this.assignments = assignments;
    }

    // Getters and setters
    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<Long, Person> getAssignments() {
        return assignments;
    }

    public void setAssignments(Map<Long, Person> assignments) {
        this.assignments = assignments;
    }
}
