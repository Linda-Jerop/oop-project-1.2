package com.school.oop.model;

// These are the choices shown in the dropdown boxes. Keeping them here means every screen uses the same lists.
public class TimetableOptions {

    public static final String[] DAYS = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };

    // The times when a lesson can start or end. Strathmore lessons start at quarter past the hour.
    public static final String[] TIMES = { "08:15", "09:15", "10:15", "11:15", "12:15", "13:15", "14:15",
            "15:15", "16:15", "17:15", "18:15", "19:15", "20:15" };
}
