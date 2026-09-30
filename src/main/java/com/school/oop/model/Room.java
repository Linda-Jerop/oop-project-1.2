package com.school.oop.model;

public class Room {
    private String roomId;
    private String building;
    private int capacity;
    private String type;

    // This constructor stores the basic room details.
    public Room(String roomId, String building, int capacity, String type) {
        this.roomId = roomId;
        this.building = building;
        this.capacity = capacity;
        this.type = type;
    }

    // This method gives the room ID.
    public String getRoomId() {
        return roomId;
    }

    // This method gives the building name.
    public String getBuilding() {
        return building;
    }

    // This method gives the room capacity.
    public int getCapacity() {
        return capacity;
    }

    // This method gives the type of room.
    public String getType() {
        return type;
    }
}
