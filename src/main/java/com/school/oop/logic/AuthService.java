package com.school.oop.logic;

public class AuthService {

    // PLACEHOLDER: replace with real logic later
    public boolean login(String staffId, String password) {
        if ("L001".equals(staffId) && "1234".equals(password)) {
            return true;
        }
        return false;
    }
}
