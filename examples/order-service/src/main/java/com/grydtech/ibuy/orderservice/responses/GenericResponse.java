package com.grydtech.ibuy.orderservice.responses;


import com.grydtech.msstack.core.Response;

public class GenericResponse extends Response {
    private Integer status;
    private String message;

    public GenericResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}