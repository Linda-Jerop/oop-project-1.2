package com.school.oop.logic;

import com.school.oop.model.Room;

public class RoomResult {
    private Room room;
    private String freeUntil;

    public RoomResult(Room room, String freeUntil) {
        this.room = room;
        this.freeUntil = freeUntil;
    }

    public Room getRoom() {
        return room;
    }

    public String getFreeUntil() {
        return freeUntil;
    }
}
