package com.school.oop.logic;

public class AuthService {

    // PLACEHOLDER: replace(d) with real logic later
    public boolean login(String staffId, String password) {
    return staffId != null
            && staffId.matches("L(?:00[1-9]|0[1-9][0-9]|[1-9][0-9][0-9])")
            && password != null
            && password.matches("[0-9]{4}");
}
}
