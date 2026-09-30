package com.school.oop.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Booking {
    private String roomId;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String courseName;

    // This constructor stores a lecturer booking.
    public Booking(String roomId, DayOfWeek day, LocalTime startTime, LocalTime endTime, String courseName) {
        this.roomId = roomId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.courseName = courseName;
    }

    // This method gives the room ID for the booking.
    public String getRoomId() {
        return roomId;
    }

    // This method gives the day of the booking.
    public DayOfWeek getDay() {
        return day;
    }

    // This method gives the start time.
    public LocalTime getStartTime() {
        return startTime;
    }

    // This method gives the end time.
    public LocalTime getEndTime() {
        return endTime;
    }

    // This method gives the course name.
    public String getCourseName() {
        return courseName;
    }
}
