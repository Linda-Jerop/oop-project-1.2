package com.school.oop.dao;

import com.school.oop.model.TimetableEntry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// TimetableDAO (Data Access Object) is the class that talks to the "timetable" table.
// Every method opens a connection, runs one SQL statement and closes the connection again.
// The "try ( ... )" part closes the connection, statement and result for us automatically.
// The methods "throw SQLException": the screen that calls them uses try-catch to show a message if MySQL fails.
public class TimetableDAO {

    // MySQL's FIELD() gives the position of the day in the list, so Monday comes before Tuesday and so on.
    private static final String ORDER_BY_DAY_AND_TIME =
            " ORDER BY FIELD(day_of_week, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'), start_time";

    // This method saves a new timetable entry (INSERT).
    public void addEntry(TimetableEntry entry) throws SQLException {
        String sql = "INSERT INTO timetable (class_group, course_name, lecturer_name, day_of_week, start_time, end_time, room) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            // Each ? in the SQL is filled in here. The number says which ? it is.
            statement.setString(1, entry.getClassGroup());
            statement.setString(2, entry.getCourseName());
            statement.setString(3, entry.getLecturerName());
            statement.setString(4, entry.getDayOfWeek());
            statement.setString(5, entry.getStartTime());
            statement.setString(6, entry.getEndTime());
            statement.setString(7, entry.getRoom());
            statement.executeUpdate();
        }
    }

    // This method deletes the entry with the given id (DELETE). The admin uses it.
    public void deleteEntry(int id) throws SQLException {
        String sql = "DELETE FROM timetable WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    // This method deletes a booking, but only if it is a booking (class_group is 'Booking') made by this user.
    // So a lecturer or class rep can never delete a real class or somebody else's booking.
    // It gives back how many rows were deleted: 1 if it worked, 0 if the booking was not theirs.
    public int deleteBooking(int id, String bookedBy) throws SQLException {
        String sql = "DELETE FROM timetable WHERE id = ? AND class_group = 'Booking' AND lecturer_name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, bookedBy);
            return statement.executeUpdate();
        }
    }

    // This method gets every entry in the timetable (SELECT). The admin uses it.
    public ArrayList<TimetableEntry> getAllEntries() throws SQLException {
        String sql = "SELECT * FROM timetable" + ORDER_BY_DAY_AND_TIME;
        ArrayList<TimetableEntry> entries = new ArrayList<TimetableEntry>();
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                entries.add(readEntry(rows));
            }
        }
        return entries;
    }

    // This method gets the entries of one class group, for example "BICS 1A". The admin's filter uses it.
    public ArrayList<TimetableEntry> getEntriesForGroup(String classGroup) throws SQLException {
        String sql = "SELECT * FROM timetable WHERE class_group = ?" + ORDER_BY_DAY_AND_TIME;
        ArrayList<TimetableEntry> entries = new ArrayList<TimetableEntry>();
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, classGroup);
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    entries.add(readEntry(rows));
                }
            }
        }
        return entries;
    }

    // This method gets the bookings made by one user (a lecturer or a class rep).
    public ArrayList<TimetableEntry> getBookingsForUser(String bookedBy) throws SQLException {
        String sql = "SELECT * FROM timetable WHERE class_group = 'Booking' AND lecturer_name = ?" + ORDER_BY_DAY_AND_TIME;
        ArrayList<TimetableEntry> bookings = new ArrayList<TimetableEntry>();
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, bookedBy);
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    bookings.add(readEntry(rows));
                }
            }
        }
        return bookings;
    }

    // This method gets the names of all the class groups in the timetable (including "Booking"), for the admin's filter.
    public ArrayList<String> getClassGroups() throws SQLException {
        String sql = "SELECT DISTINCT class_group FROM timetable ORDER BY class_group";
        ArrayList<String> groups = new ArrayList<String>();
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                groups.add(rows.getString("class_group"));
            }
        }
        return groups;
    }

    // This method checks if a room is free for the whole period from startTime to endTime.
    // Two periods overlap when each one starts before the other one ends.
    public boolean isRoomFree(String room, String dayOfWeek, String startTime, String endTime) throws SQLException {
        String sql = "SELECT COUNT(*) FROM timetable WHERE room = ? AND day_of_week = ? AND start_time < ? AND end_time > ?";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, room);
            statement.setString(2, dayOfWeek);
            statement.setString(3, endTime);
            statement.setString(4, startTime);
            try (ResultSet rows = statement.executeQuery()) {
                rows.next();
                int overlappingEntries = rows.getInt(1);
                return overlappingEntries == 0;
            }
        }
    }

    // This method turns the current row of a ResultSet into a TimetableEntry object.
    // MySQL gives times like "08:15:00", so we keep only the first 5 characters: "08:15".
    private TimetableEntry readEntry(ResultSet row) throws SQLException {
        int id = row.getInt("id");
        String classGroup = row.getString("class_group");
        String courseName = row.getString("course_name");
        String lecturerName = row.getString("lecturer_name");
        String dayOfWeek = row.getString("day_of_week");
        String startTime = row.getString("start_time").substring(0, 5);
        String endTime = row.getString("end_time").substring(0, 5);
        String room = row.getString("room");
        return new TimetableEntry(id, classGroup, courseName, lecturerName, dayOfWeek, startTime, endTime, room);
    }
}
