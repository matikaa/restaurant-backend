package com.restaurant.app.response;

public class ConstantValues {

    public static final long TIME = 1000 * (long) 60 * 30;
    public static final String INVALID_REQUEST_BODY = "Invalid request body";
    public static final String EMPTY_STRING = "";

    public static final String CATEGORY_NOT_EXISTS = "Category does not exists";
    public static final String CATEGORY_POSITION_EXISTS = "Category with this position already exists";

    public static final String CONTACT_NOT_EXISTS = "Contact does not exists";
    public static final String CONTACT_EXISTS = "Contact already exists";

    public static final String CATEGORY_NOT_EXISTS_TO_ADD_FOOD = "Category doest not exists while adding food";
    public static final String FOOD_POSITION_EXISTS = "Food with this position already exists";
    public static final String FOOD_WITH_CATEGORY_NOT_EXISTS = "Category does not exists while getting food";


    public static final String EMAIL_EXISTS = "Email already exists";
    public static final String USER_NOT_EXISTS = "User does not exists";
    public static final String NO_ACCESS = "User does not have permissions to access that resource";
    public static final String INCORRECT_LOG_DATA = "Wrong email or password";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final int TOKEN_PREFIX_LENGTH = TOKEN_PREFIX.length();
    public static final String AUTHORIZATION = "Authorization";
    public static final String INCORRECT_PASSWORD = "Wrong password or new is the same as previous";
    public static final String WRONG_EMAIL = "Email should be like: [username]@[domain].[extension]";
}
