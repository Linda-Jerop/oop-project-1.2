package com.school.oop.logic;

import com.school.oop.model.Room;

public class RoomResult {
    private Room room;
    private String freeUntil;

    // This constructor stores a room and its free time.
    public RoomResult(Room room, String freeUntil) {
        this.room = room;
        this.freeUntil = freeUntil;
    }

    // This method gives the room object.
    public Room getRoom() {
        return room;
    }

    // This method gives the free-time text.
    public String getFreeUntil() {
        return freeUntil;
    }
}
