package com.skyapi.hellorestapi.entity;

public class Response {

    private String message;

    public Response(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
