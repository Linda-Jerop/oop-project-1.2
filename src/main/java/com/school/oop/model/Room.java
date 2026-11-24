package com.school.oop.model;

// A Room object stores one row of the "rooms" table.
// The status ("Vacant" or "Occupied") is worked out when we search for rooms at a certain day and time.
public class Room {
    private String roomName;
    private String building;
    private int capacity;
    private String roomType;
    private String status;

    // This constructor stores the details of a room.
    public Room(String roomName, String building, int capacity, String roomType, String status) {
        this.roomName = roomName;
        this.building = building;
        this.capacity = capacity;
        this.roomType = roomType;
        this.status = status;
    }

    // These getters let the table read the values. The table finds them by name, for example "roomName" -> getRoomName().
    public String getRoomName() {
        return roomName;
    }

    public String getBuilding() {
        return building;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getStatus() {
        return status;
    }

    // This method gives the text for the Booking column. Only a vacant room can be booked.
    public String getBookingAction() {
        if (status.equals("Vacant")) {
            return "Book";
        }
        return "";
    }
}
