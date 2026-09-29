package com.school.oop.logic;

import com.school.oop.model.Booking;
import com.school.oop.model.Room;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

public class RoomFinder {

    // PLACEHOLDER: replace with real logic later
    public ArrayList<RoomResult> findFreeRoomsNow() {
        ArrayList<RoomResult> freeRooms = new ArrayList<RoomResult>();
        freeRooms.add(new RoomResult(new Room("A101", "Main Building", 30, "Lecture Theatre"), "14:00"));
        freeRooms.add(new RoomResult(new Room("B204", "Science Block", 20, "Lab"), "All day"));
        return freeRooms;
    }

    // PLACEHOLDER: replace with real logic later
    public ArrayList<RoomResult> findFreeRooms(DayOfWeek day, LocalTime time, String building, int minimumCapacity, String roomType) {
        ArrayList<RoomResult> freeRooms = new ArrayList<RoomResult>();
        freeRooms.add(new RoomResult(new Room("C310", "Engineering Block", 40, "Lecture"), "15:30"));
        return freeRooms;
    }

    // PLACEHOLDER: replace with real logic later
    public ArrayList<Booking> getBookingsForLecturer(String staffId) {
        ArrayList<Booking> bookings = new ArrayList<Booking>();
        bookings.add(new Booking("A101", DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 30), "OOP"));
        return bookings;
    }

    // PLACEHOLDER: replace with real logic later
    public String addBooking(String staffId, String roomId, DayOfWeek day, LocalTime startTime, LocalTime endTime, String courseName) {
        return "OK";
    }

    // PLACEHOLDER: replace with real logic later
    public String cancelBooking(String staffId, String bookingId) {
        return "OK";
    }
}
