package com.usersus.constants;

public class SecurityConstants {
    public static final String[] NO_AUTH_REQUIRED_PATTERNS = {
            "/",
            "/users/authenticate"
    };

    public static final String[] ADMIN_AUTH_REQUIRED_PATTERNS = {
            "/users/registerUser",
            "/users/getUserDetailsByEmail"
    };

    public static final String[] USER_AUTH_REQUIRED_PATTERNS = {
            "/users/listAllDevices",
            "/users/getSelfDetails"
    };


}
