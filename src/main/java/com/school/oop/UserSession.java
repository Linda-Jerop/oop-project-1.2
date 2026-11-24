package com.school.oop;

// UserSession remembers who is logged in while the app is open.
// The variables are public static, so every screen can read them without creating an object.
public class UserSession {

    // The role of the logged in user: "Admin", "Lecturer", "ClassRep" or "Student".
    public static String currentRole = "";

    // The six-digit Stratizen ID that the user typed on the login screen.
    public static String currentUserId = "";

    // The name saved with a booking (for example "Lecturer 654321"), so we know who made it
    // and so that only that person can delete it.
    public static String currentUserName = "";

    // This method says if the logged in user is allowed to book a room.
    // Only a lecturer or a class rep can book. A student cannot.
    public static boolean canBookRooms() {
        return currentRole.equals("Lecturer") || currentRole.equals("ClassRep");
    }

    // This method forgets the user. We call it when someone logs out.
    public static void clear() {
        currentRole = "";
        currentUserId = "";
        currentUserName = "";
    }
}
