package com.school.oop.model;

// A TimetableEntry object stores one row of the "timetable" table:
// one class (or one booking) that uses a room at a certain day and time.
public class TimetableEntry {
    private int id;
    private String classGroup;
    private String courseName;
    private String lecturerName;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String room;

    // This constructor stores the details of an entry. Use 0 for the id of a new entry, MySQL gives the real id.
    public TimetableEntry(int id, String classGroup, String courseName, String lecturerName,
            String dayOfWeek, String startTime, String endTime, String room) {
        this.id = id;
        this.classGroup = classGroup;
        this.courseName = courseName;
        this.lecturerName = lecturerName;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
    }

    public int getId() {
        return id;
    }

    public String getClassGroup() {
        return classGroup;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getRoom() {
        return room;
    }

    // This method joins the two times into one text, for example "08:15 - 10:15". The tables show it as the "Time Slot".
    public String getTimeSlot() {
        return startTime + " - " + endTime;
    }
}
