package com.usersus.constants;

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
            "/users/edit"
    };

    public static final String[] USER_AUTH_REQUIRED_PATTERNS = {
            "/users/listAllDevices",
            "/users/getSelfDetails"
    };

    public static final String[] ALLOWED_ORIGINS = {
            "http://localhost:3000",
            "http://localhost:5173"
    };


}
