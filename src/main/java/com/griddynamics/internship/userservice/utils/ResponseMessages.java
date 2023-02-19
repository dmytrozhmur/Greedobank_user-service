package com.griddynamics.internship.userservice.utils;

public class ResponseMessages {
    public static final String REGISTERED = "Account registered successfully!";
    public static final String FAILURE = "Action wasn't completed.";
    public static final String INVALID_BODY = "Body wasn't properly specified.";
    public static final String INVALID_URL_PARAMS = "Path variables wasn't properly specified";
    public static final String USER_NOT_FOUND = "User doesn't exist";
    public static final String ADMIN_NOT_FOUND = "Admin doesn't exist";
    public static final String PAGE_NOT_FOUND = "Page wasn't found";
    public static final String UNEXPECTED = "Something went wrong.";
    public static final String INCORRECT_FORMAT = "incorrect format";
    public static final String INAPPROPRIATE_PAGE_PARAMS = "Page number and size must be greater than 0";
    public static final String NOT_UNIQUE = "already in use";
    public static final String INVALID_PASSWORD_LENGTH = "length must consists of 8 - 20 characters";
    public static final String INAPPROPRIATE_SIZE = "must contains from 1 till 45 characters";
    public static final String EMPTY_FIELD = "must be specified";
    public static final String EMPTY_RESPONSE = "No data for these filters";
}
