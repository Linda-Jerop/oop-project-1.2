package com.school.oop.logic;

import com.school.oop.model.Room;

public class RoomResult {
    private Room room;
    private String availability;

    // This constructor stores a room and its availability status.
    public RoomResult(Room room, String availability) {
        this.room = room;
        this.availability = availability;
    }

    // This method gives the room object.
    public Room getRoom() {
        return room;
    }

    // This method gives the availability status.
    public String getAvailability() {
        return availability;
    }
}
