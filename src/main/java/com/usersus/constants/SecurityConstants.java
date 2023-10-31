package com.usersus.constants;

import java.util.Locale;

public class SecurityConstants {
    public static final String[] NO_AUTH_REQUIRED_PATTERNS = {
            "/",
            "/users/authenticate",
            "/users/registerUser"
    };

    public static final String[] ADMIN_AUTH_REQUIRED_PATTERNS = {
            "/users/getUserDetailsByEmail",
            "/users/deleteByEmailAddress",
            "/users/getAllUsersDetails",
            "/users/edit",
            "/users/registerAdmin",
            "/users/getIdByEmail"
    };

    public static final String[] USER_AUTH_REQUIRED_PATTERNS = {
            "/users/listAllDevices",
            "/users/getSelfDetails",
            "/users/getSelfId"
    };

    public static final String[] ALLOWED_ORIGINS = {
            "http://localhost:3000",
            "http://localhost:5173"
    };
}
