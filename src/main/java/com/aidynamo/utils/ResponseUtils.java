package com.aidynamo.utils;

public class ResponseUtils {

    public static <T> ApiResponse<T> createResponse1(T responseData, String message, boolean status) {
        return new ApiResponse<>(status, message, responseData);
    }


}