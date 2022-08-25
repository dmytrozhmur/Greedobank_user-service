package com.griddynamics.internship.userservice.utils;

public class ResponseMessages {
    public static final String SUCCESS = "User registered successfully!";
    public static final String FAILURE = "Action wasn't completed.";
    public static final String INVALID_BODY = "Body wasn't properly specified.";
    public static final String USER_NOT_FOUND = "User doesn't exist";
    public static final String UNEXPECTED = "Something went wrong.";
    public static final String INCORRECT_FORMAT = "incorrect format";
    public static final String EMAIL_IN_USE = "already in use";
    public static final String INVALID_PASSWORD_LENGTH = "length must consists of 8 - 20 characters";
    public static final String EXCEEDED_SIZE = "size must not exceed 45 characters";
    public static final String EMPTY_FIELD = "must not be empty";
}
