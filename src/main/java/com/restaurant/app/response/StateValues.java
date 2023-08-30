package com.restaurant.app.response;

public class StateValues {

    public static final long TIME = 1000 * (long) 60 * 30;
    public static final String EXISTING_EMAIL = "This email already exists";
    public static final String INVALID_REQUEST_BODY = "Invalid request body";
    public static final String USER_NOT_EXISTS = "User not exists";
    public static final String NO_ACCESS = "User has no permission to access that resource";
    public static final String INCORRECT_LOG_DATA = "Wrong email or password";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final int TOKEN_PREFIX_LENGTH = TOKEN_PREFIX.length();
    public static final String AUTHORIZATION = "Authorization";
    public static final String EMPTY_STRING = "";
    public static final String INCORRECT_PASSWORD = "Wrong password or new is the same as previous";
    public static final String WRONG_EMAIL = "Email should be like: [username]@[domain].[extension]";
}
