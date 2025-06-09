package com.example.webapp.entity;

public class ApiResponse {
    private boolean success;
    private String message;
    private Object data;

    // 생성자, getter, setter
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}