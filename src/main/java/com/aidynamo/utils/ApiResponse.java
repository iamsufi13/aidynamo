package com.aidynamo.utils;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private boolean status;
    private String message;
    private T responseData;

    public ApiResponse(boolean status, String message, T responseData) {
        this.status = status;
        this.message = message;
        this.responseData = responseData;
    }

}