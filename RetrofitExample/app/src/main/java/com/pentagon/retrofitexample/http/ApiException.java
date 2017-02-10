package com.pentagon.retrofitexample.http;

public class ApiException extends RuntimeException {

    public static final int USER_NOT_EXIST = 100;
    public static final int WRONG_PASSWORD = 101;

    public ApiException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    private static String getApiExceptionMessage(int code){
        String message = "";
        switch (code) {
            case USER_NOT_EXIST:
                message = "User doesn't exist";
                break;
            case WRONG_PASSWORD:
                message = "Wrong password";
                break;
            default:
                message = "Unknown Error";

        }
        return message;
    }
}

