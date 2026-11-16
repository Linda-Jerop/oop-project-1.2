package com.school.oop.logic;

public class AuthService {

    // PLACEHOLDER: replace(d) with real logic later
    public boolean login(String stratizenId, String password) {
        return stratizenId != null
                && stratizenId.matches("[0-9]{6}")
                && password != null;
    }
}
