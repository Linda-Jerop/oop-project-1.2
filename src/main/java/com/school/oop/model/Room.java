package com.school.oop.model;

public class Room {
    private String roomId;
    private String building;
    private int capacity;
    private String type;

    public Room(String roomId, String building, int capacity, String type) {
        this.roomId = roomId;
        this.building = building;
        this.capacity = capacity;
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getBuilding() {
        return building;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getType() {
        return type;
    }
}
