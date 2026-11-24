package com.school.oop.dao;

import com.school.oop.model.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// RoomDAO (Data Access Object) is the class that talks to the "rooms" table.
public class RoomDAO {

    // This method gets the names of all buildings, for the building dropdown.
    public ArrayList<String> getBuildings() throws SQLException {
        String sql = "SELECT DISTINCT building FROM rooms ORDER BY building";
        ArrayList<String> buildings = new ArrayList<String>();
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                buildings.add(rows.getString("building"));
            }
        }
        return buildings;
    }

    // This method gets the names of all rooms, for the room dropdown of the admin.
    public ArrayList<String> getRoomNames() throws SQLException {
        String sql = "SELECT room_name FROM rooms ORDER BY building, room_name";
        ArrayList<String> roomNames = new ArrayList<String>();
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                roomNames.add(rows.getString("room_name"));
            }
        }
        return roomNames;
    }

    // This method finds the rooms that match the search and marks each one as "Vacant" or "Occupied".
    // building can be "Any" and roomType can be "Any" (it means we do not filter on it).
    public ArrayList<Room> searchRooms(String dayOfWeek, String time, String building, int minCapacity, String roomType)
            throws SQLException {
        // Step 1: find which rooms are in use at that day and time.
        ArrayList<String> occupiedRooms = getOccupiedRoomNames(dayOfWeek, time);

        // Step 2: read the rooms that are big enough, then keep only the ones that match the building and type.
        String sql = "SELECT * FROM rooms WHERE capacity >= ? ORDER BY building, room_name";
        ArrayList<Room> rooms = new ArrayList<Room>();
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, minCapacity);
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    String roomName = rows.getString("room_name");
                    String roomBuilding = rows.getString("building");
                    String type = rows.getString("room_type");
                    int capacity = rows.getInt("capacity");

                    boolean buildingMatches = building.equals("Any") || building.equals(roomBuilding);
                    boolean typeMatches = roomType.equals("Any") || roomType.equals(type);
                    if (buildingMatches && typeMatches) {
                        String status = "Vacant";
                        if (occupiedRooms.contains(roomName)) {
                            status = "Occupied";
                        }
                        rooms.add(new Room(roomName, roomBuilding, capacity, type, status));
                    }
                }
            }
        }
        return rooms;
    }

    // This method gets the names of the rooms that are in use at the given day and time.
    // A room is in use when the time is on or after the start and before the end of one of its timetable rows.
    private ArrayList<String> getOccupiedRoomNames(String dayOfWeek, String time) throws SQLException {
        String sql = "SELECT DISTINCT room FROM timetable WHERE day_of_week = ? AND start_time <= ? AND end_time > ?";
        ArrayList<String> occupiedRooms = new ArrayList<String>();
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dayOfWeek);
            statement.setString(2, time);
            statement.setString(3, time);
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    occupiedRooms.add(rows.getString("room"));
                }
            }
        }
        return occupiedRooms;
    }
}
