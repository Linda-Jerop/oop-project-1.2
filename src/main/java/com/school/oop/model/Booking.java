package com.school.oop.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Booking {
    private String roomId;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String courseName;

    public Booking(String roomId, DayOfWeek day, LocalTime startTime, LocalTime endTime, String courseName) {
        this.roomId = roomId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.courseName = courseName;
    }

    public String getRoomId() {
        return roomId;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getCourseName() {
        return courseName;
    }
}
