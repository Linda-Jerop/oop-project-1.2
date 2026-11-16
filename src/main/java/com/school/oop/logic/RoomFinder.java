package com.school.oop.logic;

import com.school.oop.model.Booking;
import com.school.oop.model.Room;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

public class RoomFinder {

    // Search the supplied room inventory; schedule availability awaits database integration.
    public ArrayList<RoomResult> searchRooms(String building, int minimumCapacity, String roomType) {
        ArrayList<RoomResult> matches = new ArrayList<>();
        for (Room room : createRoomInventory()) {
            boolean matchesBuilding = building == null || building.isBlank()
                    || room.getBuilding().equals(building);
            boolean matchesType = roomType == null || roomType.isBlank()
                    || room.getType().equals(roomType);
            boolean matchesCapacity = room.getCapacity() >= minimumCapacity;

            if (matchesBuilding && matchesType && matchesCapacity) {
                matches.add(new RoomResult(room, "Schedule not connected"));
            }
        }
        return matches;
    }

    // Build the temporary inventory from the room and capacity details provided.
    private ArrayList<Room> createRoomInventory() {
        ArrayList<Room> rooms = new ArrayList<>();

        addClassrooms(rooms, "Oval Building", "SLS Shaba", "SLS Zumaridi");

        addClassrooms(rooms, "STMB",
                "F1-02", "F1-03", "F1-04", "F1-05",
                "F2-01", "F2-02", "F2-03", "F2-04", "F2-05",
                "F5-01", "F5-02", "F5-03", "F5-04", "F5-05");

        for (int roomNumber = 1; roomNumber <= 14; roomNumber++) {
            addClassrooms(rooms, "MSB", "MSB " + roomNumber);
        }

        addLabs(rooms, "STC", "Chela Lab");
        addClassrooms(rooms, "SBS", "SBS 1", "SBS 2");
        addLabs(rooms, "Central Building", "Masinga Lab", "Suswa Lab");

        for (int roomNumber = 1; roomNumber <= 4; roomNumber++) {
            addClassrooms(rooms, "Central Building", "LT " + roomNumber);
        }
        for (int roomNumber = 1; roomNumber <= 6; roomNumber++) {
            addClassrooms(rooms, "Central Building", "Rm " + roomNumber);
        }
        return rooms;
    }

    private void addClassrooms(ArrayList<Room> rooms, String building, String... roomIds) {
        for (String roomId : roomIds) {
            rooms.add(new Room(roomId, building, 63, "Classroom"));
        }
    }

    private void addLabs(ArrayList<Room> rooms, String building, String... roomIds) {
        for (String roomId : roomIds) {
            rooms.add(new Room(roomId, building, 42, "Lab"));
        }
    }

    // PLACEHOLDER: replace with real logic later
    public ArrayList<Booking> getBookingsForStratizen(String stratizenId) {
        ArrayList<Booking> bookings = new ArrayList<Booking>();
        bookings.add(new Booking("A101", DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 30), "OOP"));
        return bookings;
    }

    // PLACEHOLDER: replace with real logic later
    public String addBooking(String stratizenId, String roomId, DayOfWeek day, LocalTime startTime, LocalTime endTime, String courseName) {
        return "OK";
    }

    // PLACEHOLDER: replace with real logic later
    public String cancelBooking(String stratizenId, String bookingId) {
        return "OK";
    }
}
