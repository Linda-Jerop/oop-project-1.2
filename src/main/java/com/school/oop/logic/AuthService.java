package com.school.oop.logic;

public class AuthService {

    // PLACEHOLDER: replace(d) with real logic later
    public boolean login(String stratizenId, String password) {
    return stratizenId != null
            && stratizenId.matches("S(?:00[1-9]|0[1-9][0-9]|[1-9][0-9][0-9])")
            && password != null
            && password.matches("[0-9]{4}");
}
}
